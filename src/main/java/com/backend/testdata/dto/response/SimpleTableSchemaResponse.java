package com.backend.testdata.dto.response;

import com.backend.testdata.dto.TableSchemaDto;
import java.time.LocalDateTime;

public record SimpleTableSchemaResponse(
    String userId,
    String schemaName,
    LocalDateTime modifiedAt
) {

  public static SimpleTableSchemaResponse toSimpleResponse(TableSchemaDto dto) {
    return new SimpleTableSchemaResponse(
        dto.userId(),
        dto.schemaName(),
        dto.modifiedAt()
    );
  }
}
