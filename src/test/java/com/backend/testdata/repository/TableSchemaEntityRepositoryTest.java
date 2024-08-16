package com.backend.testdata.repository;

import com.backend.testdata.domain.TableSchemaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[Repository] 테이블 스키마 쿼리 메소드 테스트")
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class TableSchemaEntityRepositoryTest {

    @Autowired
    private TableSchemaEntityRepository sut;
    @Autowired
    private TableSchemaEntityRepository tableSchemaEntityRepository;


    @DisplayName("사용자별 테이블 스키마 목록 조회하면 페이징된 테이블 스키마를 반환한다.")
    @Test
    void if_givenUserId_whenRetrieveAllTableSchemas_thenReturnsPagedEntities() {
        //given
        var userId = "devJohn";

        //when
        Page<TableSchemaEntity> tableSchemaPsList = sut.findAllByUserId(userId, Pageable.ofSize(5));

        //then
        assertThat(tableSchemaPsList).isNotEmpty();
        assertThat(tableSchemaPsList.getContent())
                .hasSize(1)
                .extracting("userId", String.class)
                .containsExactly(userId);
        assertThat(tableSchemaPsList.getPageable())
                .hasFieldOrPropertyWithValue("pageSize", 5)
                .hasFieldOrPropertyWithValue("pageNumber", 0);
    }


    @DisplayName("사용자의 테이블 스키마 이름으로 단일 테이블 스키마 조회하면 Optional 타입의 테이블 스키마를 반환한다.")
    @Test
    void if_givenUserIdAndSchemaName_whenRetrieveOneTableSchema_thenReturnsOptionalEntities() {
        //given
        var userId = "devJohn";
        var schemaName = "test_schema1";

        //when
        Optional<TableSchemaEntity> tableSchemaPs = tableSchemaEntityRepository.findByUserIdAndSchemaName(userId, schemaName);

        //then
        assertThat(tableSchemaPs).isNotEmpty();
        assertThat(tableSchemaPs.get().getUserId()).isEqualTo(userId);
        assertThat(tableSchemaPs.get().getSchemaName()).isEqualTo(schemaName);
    }


    @DisplayName("사용자의 테이블 스키마 이름입력하면 테이블 스키마를 삭제한다.")
    @Test
    void if_givenUserIdAndSchemaName_thenDeleteTableSchemaAndDoNothing() {
        //given
        var userId = "devJohn";
        var schemaName = "test_schema1";

        //when
        sut.deleteByUserIdAndSchemaName(userId, schemaName);

        //then
        assertThat(tableSchemaEntityRepository.findByUserIdAndSchemaName(userId, schemaName)).isEmpty();
    }
}