package com.backend.testdata.dto.response;

import com.backend.testdata.dto.TableSchemaDto;

import java.util.List;

public record TableSchemaResponse(
        String schemaName,
        String userId,
        List<SchemaFieldResponse> schemaFields
) {

    public static TableSchemaResponse toResponse(TableSchemaDto dto) {
        return new TableSchemaResponse(
                dto.schemaName(),
                dto.userId(),
                dto.schemaFields().stream()
                        .map(SchemaFieldResponse::toResponse)
                        .toList()
        );

    }
}
