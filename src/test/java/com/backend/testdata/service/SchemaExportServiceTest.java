package com.backend.testdata.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.backend.testdata.domain.TableSchemaEntity;
import com.backend.testdata.domain.constants.ExportFileType;
import com.backend.testdata.domain.constants.MockDataType;
import com.backend.testdata.dto.SchemaFieldDto;
import com.backend.testdata.dto.TableSchemaDto;
import com.backend.testdata.repository.TableSchemaEntityRepository;
import com.backend.testdata.service.exporter.MockDataFileExporterContext;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("[Service] 스키마 파일 출력 테스트")
@ExtendWith(MockitoExtension.class)
class SchemaExportServiceTest {

    @InjectMocks
    private SchemaExportService sut;

    @Mock
    private MockDataFileExporterContext exporterContext;
    @Mock
    private TableSchemaEntityRepository tableSchemaEntityRepository;


    @DisplayName("회원이 출력 파일 유형과 테이블 스키마, 행수를 전달했을 때 엔티티 출력 여부를 체크하고 해당 파일 포맷의 문자열을 반환한다.")
    @Test
    void givenExportFileTypeAndTableSchemaAndRowCount_whenAuthUserIsExporting_thenInsertingExportDateAndReturnFormattedString() {
        //given
        ExportFileType exportFileType = ExportFileType.CSV;
        TableSchemaEntity tableSchemaPs = TableSchemaEntity.of("schema1", "test_id");
        TableSchemaDto dto = TableSchemaDto.of(
            "schema1",
            "test_id",
            null,
            Set.of(
                SchemaFieldDto.of("id", MockDataType.STRING, 1, 0, null, null),
                SchemaFieldDto.of("name", MockDataType.NAME, 2, 0, null, null),
                SchemaFieldDto.of("age", MockDataType.NUMBER, 3, 0, null, null),
                SchemaFieldDto.of("car", MockDataType.CAR, 4, 0, null, null),
                SchemaFieldDto.of("created_at", MockDataType.DATETIME, 5, 0, null, null)
            )
        );
        int rowCount = 10;
        given(tableSchemaEntityRepository.findByUserIdAndSchemaName(dto.userId(), dto.schemaName()))
            .willReturn(Optional.of(tableSchemaPs));

        given(exporterContext.delegatingExport(exportFileType, dto, rowCount))
            .willReturn("CSV DATA");

        //when
        String result = sut.export(exportFileType, dto, rowCount);

        //then
        assertThat(result).isEqualTo("CSV DATA");
        assertThat(tableSchemaPs.isExported()).isTrue();
        then(tableSchemaEntityRepository).should()
            .findByUserIdAndSchemaName(dto.userId(), dto.schemaName());
        then(exporterContext).should().delegatingExport(exportFileType, dto, rowCount);
    }


    @DisplayName("회원이 아닌 출력 파일 유형과 테이블 스키마, 행수를 전달했을 때 엔티티 출력 여부를 체크하지 않아야한다.")
    @Test
    void givenExportFileTypeAndTableSchemaAndRowCount_whenUnAuthUserIsExporting_thenDoesNotRetrieveTableSchema() {
        //given
        ExportFileType exportFileType = ExportFileType.CSV;
        TableSchemaDto dto = TableSchemaDto.of(
            "schema1",
            null,
            null,
            Set.of(
                SchemaFieldDto.of("id", MockDataType.STRING, 1, 0, null, null),
                SchemaFieldDto.of("name", MockDataType.NAME, 2, 0, null, null),
                SchemaFieldDto.of("age", MockDataType.NUMBER, 3, 0, null, null),
                SchemaFieldDto.of("car", MockDataType.CAR, 4, 0, null, null),
                SchemaFieldDto.of("created_at", MockDataType.DATETIME, 5, 0, null, null)
            )
        );
        int rowCount = 10;

        given(exporterContext.delegatingExport(exportFileType, dto, rowCount))
            .willReturn("CSV DATA");

        //when
        String result = sut.export(exportFileType, dto, rowCount);

        //then
        assertThat(result).isEqualTo("CSV DATA");
        then(tableSchemaEntityRepository).shouldHaveNoInteractions();
        then(exporterContext).should().delegatingExport(exportFileType, dto, rowCount);
    }

}