package com.backend.testdata.dto.request;

import com.backend.testdata.dto.TableSchemaDto;

import java.util.List;
import java.util.stream.Collectors;

public record TableSchemaRequest(
        String schemaName,
        String userId,
        List<SchemaFieldRequest> schemaFields
) {

    public TableSchemaDto toDto() {
        return TableSchemaDto.of(
                this.schemaName,
                this.userId,
                null,
                this.schemaFields.stream()
                        .map(SchemaFieldRequest::toDto)
                        .collect(Collectors.toUnmodifiableSet())
        );
    }
}
