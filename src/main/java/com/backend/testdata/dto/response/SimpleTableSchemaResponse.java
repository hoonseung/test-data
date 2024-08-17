package com.backend.testdata.dto.response;

import com.backend.testdata.dto.TableSchemaDto;

public record SimpleTableSchemaResponse(
        String userId,
        String schemaName
) {

    public static SimpleTableSchemaResponse toSimpleResponse(TableSchemaDto dto) {
        return new SimpleTableSchemaResponse(
                dto.userId(),
                dto.schemaName()
        );
    }
}
