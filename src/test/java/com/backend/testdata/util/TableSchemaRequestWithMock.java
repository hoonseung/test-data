package com.backend.testdata.util;

import com.backend.testdata.dto.request.SchemaFieldRequest;
import com.backend.testdata.dto.request.TableSchemaRequest;
import java.util.List;

public class TableSchemaRequestWithMock {

  public static TableSchemaRequest create(String schemaName,
      List<SchemaFieldRequest> schemaFields) {
    return TableSchemaRequest.of(
        schemaName,
        schemaFields
    );
  }
}
