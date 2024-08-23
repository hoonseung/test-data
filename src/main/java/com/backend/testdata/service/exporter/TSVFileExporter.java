package com.backend.testdata.service.exporter;

import com.backend.testdata.domain.constants.ExportFileType;
import com.backend.testdata.service.generator.MockDataGeneratorContext;
import org.springframework.stereotype.Component;


@Component
public class TSVFileExporter extends DelimiterBasedFileExporter {

    private static final String DELIMETER = "\t";

    public TSVFileExporter(MockDataGeneratorContext mockDataGeneratorContext) {
        super(mockDataGeneratorContext);
    }

    @Override
    public ExportFileType getType() {
        return ExportFileType.TSV;
    }

    @Override
    public String getDelimiter() {
        return DELIMETER;
    }
}
