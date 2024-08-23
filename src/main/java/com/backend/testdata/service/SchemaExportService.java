package com.backend.testdata.service;

import com.backend.testdata.domain.TableSchemaEntity;
import com.backend.testdata.domain.constants.ExportFileType;
import com.backend.testdata.dto.TableSchemaDto;
import com.backend.testdata.repository.TableSchemaEntityRepository;
import com.backend.testdata.service.exporter.MockDataFileExporterContext;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SchemaExportService {

    private final MockDataFileExporterContext mockDataFileExporterContext;
    private final TableSchemaEntityRepository tableSchemaEntityRepository;


    public String export(ExportFileType fileType, TableSchemaDto dto, Integer rowCount) {
        String userId = dto.userId();
        if (Objects.nonNull(userId)) {
            tableSchemaEntityRepository.findByUserIdAndSchemaName(
                    userId, dto.schemaName())
                .ifPresent(TableSchemaEntity::markExported);
        }

        return mockDataFileExporterContext.delegatingExport(fileType, dto, rowCount);
    }

}
