package com.backend.testdata.dto.request;

import com.backend.testdata.dto.TableSchemaDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class TableSchemaRequest {

  private String schemaName;
  private List<SchemaFieldRequest> schemaFields;


  public TableSchemaDto toDto(String userId) {
    return TableSchemaDto.of(
        this.schemaName,
        userId,
        null,
        this.schemaFields.stream()
            .map(SchemaFieldRequest::toDto)
            .collect(Collectors.toUnmodifiableSet())
    );
  }
}
