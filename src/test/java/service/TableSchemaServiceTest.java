package service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

import com.backend.testdata.domain.TableSchemaEntity;
import com.backend.testdata.dto.TableSchemaDto;
import com.backend.testdata.repository.TableSchemaEntityRepository;
import com.backend.testdata.service.TableSchemaService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@DisplayName("[Service] 테이블 스키마 서비스")
@ExtendWith(MockitoExtension.class)
class TableSchemaServiceTest {

  @InjectMocks
  private TableSchemaService sut;

  @Mock
  private TableSchemaEntityRepository tableSchemaEntityRepository;


  @DisplayName("내 테이블 스키마 목록 조회에서 Page 반환 타입을 List 로 변환")
  @Test
  void whenRetrieving_thenReturnMy_tableSchemas() {
    //given
    String userId = "shlee";
    //when
    when(tableSchemaEntityRepository.findAllByUserId(userId, Pageable.unpaged()))
        .thenReturn(new PageImpl<>(List.of(
            TableSchemaEntity.of("tableSchema1", "user1"),
            TableSchemaEntity.of("tableSchema2", "user2"),
            TableSchemaEntity.of("tableSchema3", "user3"))));

    List<TableSchemaDto> result = sut.loadTableSchemas(userId);

    //then
    assertThat(result)
        .hasSize(3)
        .extracting("schemaName")
        .containsExactly("tableSchema1", "tableSchema2", ("tableSchema3"));

    then(tableSchemaEntityRepository).should().findAllByUserId(userId, Pageable.unpaged());

  }


  @DisplayName("사용자 ID와 스키마 이름이 주어지면 테이블 스키마를 반환한다.")
  @Test
  void givenUserIdAndSchemaName_whenRetrieving_ThenReturnMyTableSchema() {
    //given
    String userId = "shlee";
    String schemaName = "schema1";
    TableSchemaEntity tableSchema = TableSchemaEntity.of(schemaName, userId);
    given(tableSchemaEntityRepository.findByUserIdAndSchemaName(userId, schemaName))
        .willReturn(Optional.of(tableSchema));

    //when
    TableSchemaDto tableSchemaDto = sut.loadMyTableSchema(userId, schemaName);

    //then
    assertThat(tableSchemaDto)
        .hasFieldOrPropertyWithValue("userId", userId)
        .hasFieldOrPropertyWithValue("schemaName", schemaName);

    then(tableSchemaEntityRepository).should().findByUserIdAndSchemaName(userId, schemaName);
  }


  @DisplayName("사용자 ID와 스키마 이름이 주어지지 않으면 테이블 스키마를 반환한다.")
  @Test
  void NotGivenUserIdAndSchemaName_whenRetrieving_ThenThrowException() {
    //given
    String userId = "shlee";
    String schemaName = "schema1";
    given(tableSchemaEntityRepository.findByUserIdAndSchemaName(userId, schemaName))
        .willReturn(Optional.empty());
    //when
    Throwable ex = catchThrowable(() -> sut.loadMyTableSchema(userId, schemaName));

    //then
    assertThat(ex).isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("해당 스키마를 찾을 수 없습니다");

    then(tableSchemaEntityRepository).should().findByUserIdAndSchemaName(userId, schemaName);
  }


}