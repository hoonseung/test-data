package com.backend.testdata.dto.request;

import com.backend.testdata.dto.TableSchemaDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class TableSchemaRequest {
    private String schemaName;
    private String userId;
    private List<SchemaFieldRequest> schemaFields;


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
