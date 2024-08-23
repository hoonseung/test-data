package com.backend.testdata.service.exporter;

import com.backend.testdata.domain.constants.ExportFileType;
import com.backend.testdata.service.generator.MockDataGeneratorContext;
import org.springframework.stereotype.Component;


@Component
public class CSVFileExporter extends DelimiterBasedFileExporter {

    private static final String DELIMETER = ",";

    public CSVFileExporter(MockDataGeneratorContext mockDataGeneratorContext) {
        super(mockDataGeneratorContext);
    }

    @Override
    public ExportFileType getType() {
        return ExportFileType.CSV;
    }

    @Override
    public String getDelimiter() {
        return DELIMETER;
    }
}
