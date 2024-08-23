package com.backend.testdata.service.exporter;

import com.backend.testdata.domain.constants.ExportFileType;
import com.backend.testdata.domain.constants.MockDataType;
import com.backend.testdata.dto.SchemaFieldDto;
import com.backend.testdata.dto.TableSchemaDto;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DisplayName("[IntergrationTest] 파일 출력기 컨텍스트 테스트")
@SpringBootTest
class MockDataFileExporterContextTest {

    @Autowired
    private MockDataFileExporterContext sut;


    @DisplayName("ExportFileType, 테이블 스키마, 행수가 주어지면 이 형식에 맞게 문자열을 반환한다.")
    @Test
    void givenExportFileTypeAndTableSchemaAndRowCount_whenExporting_thenReturnFormattedString() {
        //given
        ExportFileType exportFileType = ExportFileType.TSV;
        TableSchemaDto tableSchema = TableSchemaDto.of(
            "schema1",
            "test_id",
            null,
            Set.of(
                SchemaFieldDto.of("id", MockDataType.STRING, 1, 0, null, null),
                SchemaFieldDto.of("name", MockDataType.STRING, 2, 0, null, null)
            )
        );
        int rowCount = 10;

        //when
        String result = sut.delegatingExport(exportFileType, tableSchema, rowCount);

        //then
        System.out.println(result);
    }

}