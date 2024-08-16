package com.backend.testdata.repository;


import com.backend.testdata.domain.MockDataEntity;
import com.backend.testdata.domain.SchemaFieldEntity;
import com.backend.testdata.domain.TableSchemaEntity;
import com.backend.testdata.domain.constants.MockDataType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@Import(JpaRepositoryTest.TestJpaConfiguration.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 실제 DB 참조
@DisplayName("[Repository] JPA 테스트")
@DataJpaTest
class JpaRepositoryTest {


    @Autowired
    private MockDataEntityRepository mockDataEntityRepository;
    @Autowired
    private SchemaFieldEntityRepository schemaFieldEntityRepository;
    @Autowired
    private TableSchemaEntityRepository tableSchemaEntityRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ObjectMapper mapper;

    private static final String TEST_AUDITOR = "hoonseung";


    @DisplayName("SELECT")
    @Test
    void select_test() {
        //given


        //when
        var mockDataEntities = mockDataEntityRepository.findAll();
        var schemaFieldEntities = schemaFieldEntityRepository.findAll();
        var tableSchemaEntities = tableSchemaEntityRepository.findAll();

        //then
        assertThat(mockDataEntities).hasSizeGreaterThan(1);
        assertThat(schemaFieldEntities)
                .hasSize(4)
                .first()
                .extracting("tableSchema")
                .isEqualTo(tableSchemaEntities.getFirst());

        assertThat(tableSchemaEntities)
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("schemaName", "test_schema1")
                .hasFieldOrPropertyWithValue("userId", "devJohn")
                .extracting("schemaFields", InstanceOfAssertFactories.COLLECTION)
                .hasSize(4);
    }


    @DisplayName("INSERT 후 반환된 데이터에 대한 검증 (연관관계)")
    @Test
    void insert_test() {
        //given
        var tableSchema = TableSchemaEntity.of("test_schema2", "devJohn");
        tableSchema.addAllSchemaFieldAndSetRelation(List.of(
                SchemaFieldEntity.of("id", MockDataType.ROW_NUMBER, 1, 0, null, null),
                SchemaFieldEntity.of("age", MockDataType.ROW_NUMBER, 2, 0, null, null),
                SchemaFieldEntity.of("name", MockDataType.NAME, 3, 0, null, null)
        ));

        //when
        var tableSchemaPs = tableSchemaEntityRepository.save(tableSchema);

        //then
        testEntityManager.clear();
        assertThat(tableSchemaPs.getId()).isNotNull();
        assertThat(tableSchemaEntityRepository.findById(tableSchemaPs.getId()).get())
                .hasFieldOrPropertyWithValue("schemaName", "test_schema2")
                .hasFieldOrPropertyWithValue("userId", "devJohn")
                .hasFieldOrPropertyWithValue("createdBy", TEST_AUDITOR)
                .hasFieldOrProperty("createdAt")
                .hasFieldOrProperty("modifiedAt")
                .extracting("schemaFields", InstanceOfAssertFactories.COLLECTION)
                .hasSize(3)
                .extracting("fieldOrder", Integer.class)
                .containsExactly(1, 2, 3);

        assertThat(tableSchemaPs.getCreatedAt()).isEqualTo(tableSchemaPs.getModifiedAt());
    }

    @DisplayName("UPDATE")
    @Test
    void update_test() {
        //given
        var tableSchemaPs = tableSchemaEntityRepository.findAll().getFirst();
        tableSchemaPs.setSchemaName("test_modified");
        tableSchemaPs.clearSchemaFields();
        var schemaField = SchemaFieldEntity.of("id", MockDataType.ROW_NUMBER, 1, 0,
                json(Map.of("min", 1, "max", 30)), null);
        tableSchemaPs.addSchemaFieldAndSetRelation(schemaField);
        //when
        var tableSchemaPsUpdated = tableSchemaEntityRepository.saveAndFlush(tableSchemaPs);

        //then
        assertThat(tableSchemaPsUpdated)
                .hasFieldOrPropertyWithValue("schemaName", "test_modified")
                .hasFieldOrPropertyWithValue("createdBy", "shlee")
                .hasFieldOrPropertyWithValue("modifiedBy", TEST_AUDITOR)
                .extracting("schemaFields", InstanceOfAssertFactories.COLLECTION)
                .hasSize(1);

        assertThat(tableSchemaPs.getModifiedAt()).isAfter(tableSchemaPsUpdated.getCreatedAt());
    }


    @DisplayName("DELETE")
    @Test
    void delete_test() {
        //given
        var tableSchemaPs = tableSchemaEntityRepository.findAll().getFirst();

        //when
        tableSchemaEntityRepository.delete(tableSchemaPs);

        //then
        assertThat(tableSchemaEntityRepository.count()).isZero();
        assertThat(schemaFieldEntityRepository.count()).isZero();
    }


    @DisplayName("UK 제약조건 동작 검증")
    @Test
    void uk_test() {
        //given
        var mockData1 = MockDataEntity.of(MockDataType.CAR, "제네시스 G70");
        var mockData2 = MockDataEntity.of(MockDataType.CAR, "제네시스 G70");

        //when
        Throwable ex = catchThrowable(() -> mockDataEntityRepository.saveAll(List.of(mockData1, mockData2)));


        //then
        assertThat(ex)
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasCauseInstanceOf(ConstraintViolationException.class)
                .hasRootCauseInstanceOf(SQLIntegrityConstraintViolationException.class)
                .rootCause()
                .hasMessageContaining("Unique index or primary key violation");
    }


    private String json(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException jpe) {
            throw new RuntimeException("테스트에서 Json 파싱 중 에러 발생", jpe);
        }
    }

    @EnableJpaAuditing
    @TestConfiguration
    static class TestJpaConfiguration {

        @Bean
        public AuditorAware<String> auditorAware() {
            return () -> Optional.of(TEST_AUDITOR);
        }

        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }

}
