package com.backend.testdata.util;

import com.backend.testdata.dto.request.SchemaFieldRequest;
import com.backend.testdata.dto.request.TableSchemaRequest;

import java.util.List;

public class TableSchemaRequestWithMock {

    public static TableSchemaRequest create(String schemaName, String userId, List<SchemaFieldRequest> schemaFields) {
        return new TableSchemaRequest(
                schemaName,
                userId,
                schemaFields
        );
    }
}
