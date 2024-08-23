package com.backend.testdata.service.exporter;

import com.backend.testdata.domain.constants.ExportFileType;
import com.backend.testdata.dto.TableSchemaDto;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class MockDataFileExporterContext {

    private final Map<ExportFileType, MockDataFileExporter> mockDataFileExporterMap;


    public MockDataFileExporterContext(List<MockDataFileExporter> mockDataFileExporters) {
        this.mockDataFileExporterMap = mockDataFileExporters.stream()
            .collect(Collectors.toMap(MockDataFileExporter::getType, Function.identity()));
    }


    public String delegatingExport(ExportFileType fileType, TableSchemaDto dto, Integer rowCount) {
        MockDataFileExporter fileExporter = mockDataFileExporterMap.get(fileType);

        return fileExporter.export(dto, rowCount);
    }
}
