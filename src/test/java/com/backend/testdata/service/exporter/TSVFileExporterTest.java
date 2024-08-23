package com.backend.testdata.service.exporter;

import static org.assertj.core.api.Assertions.assertThat;

import com.backend.testdata.domain.constants.MockDataType;
import com.backend.testdata.dto.SchemaFieldDto;
import com.backend.testdata.dto.TableSchemaDto;
import com.backend.testdata.service.generator.MockDataGeneratorContext;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("[Logic] TSV 파일 출력기 테스트")
@ExtendWith(MockitoExtension.class)
class TSVFileExporterTest {

    @Mock
    private MockDataGeneratorContext mockDataGeneratorContext;

    @InjectMocks
    private TSVFileExporter sut;


    @DisplayName("테이블 스키마와 행수가 주어지면 CSV 형식의 문자열을 생성한다.")
    @Test
    void givenTableSchemaAndRowCount_whenExporting_thenReturnCSVFormattedString() {
        //given
        TableSchemaDto tableSchema = TableSchemaDto.of(
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

        //when
        String result = sut.export(tableSchema, rowCount);

        //then
        System.out.println(result);
        assertThat(result).startsWith("id\tname\tage\tcar\tcreated_at");
        assertThat(result).hasLineCount(11);

    }

}