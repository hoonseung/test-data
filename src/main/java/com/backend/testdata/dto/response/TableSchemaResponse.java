package com.backend.testdata.dto.response;

import com.backend.testdata.dto.TableSchemaDto;

import java.util.List;

public record TableSchemaResponse(
        String userId,
        String schemaName,
        List<SchemaFieldResponse> schemaFields
) {

    public static TableSchemaResponse toResponse(TableSchemaDto dto) {
        return new TableSchemaResponse(
                dto.userId(),
                dto.schemaName(),
                dto.schemaFields().stream()
                        .map(SchemaFieldResponse::toResponse)
                        .toList()
        );

    }
}
