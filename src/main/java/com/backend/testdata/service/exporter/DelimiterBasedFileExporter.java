package com.backend.testdata.service.exporter;

import com.backend.testdata.dto.SchemaFieldDto;
import com.backend.testdata.dto.TableSchemaDto;
import com.backend.testdata.service.generator.MockDataGeneratorContext;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public abstract class DelimiterBasedFileExporter implements MockDataFileExporter {

    private final MockDataGeneratorContext mockDataGeneratorContext;


    @Override
    public String export(TableSchemaDto dto, Integer rowCount) {
        String delimiter = getDelimiter();
        StringBuilder sb = new StringBuilder();

        // 헤더
        sb.append(dto.schemaFields().stream()
                .sorted(Comparator.comparing(SchemaFieldDto::fieldOrder))
                .map(SchemaFieldDto::fieldName)
                .collect(Collectors.joining(delimiter)))

            .append("\n");

        // 데이터
        IntStream.range(0, rowCount)
            .forEachOrdered(i -> {
                sb.append(dto.schemaFields().stream()
                    .sorted(Comparator.comparing(SchemaFieldDto::fieldOrder))
                    .map(field -> mockDataGeneratorContext.delegatingGenerate(
                            field.mockDataType(),
                            field.blankPercent(),
                            field.typeOptionJson(),
                            field.forceValue()
                        )
                    )
                    .map(value -> Objects.isNull(value) ? "" : value)
                    .collect(Collectors.joining(delimiter))
                );
                sb.append("\n");
            });

        return sb.toString();
    }


    public abstract String getDelimiter();
}
