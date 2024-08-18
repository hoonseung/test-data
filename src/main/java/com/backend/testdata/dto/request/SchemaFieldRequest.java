package com.backend.testdata.dto.request;

import com.backend.testdata.domain.constants.MockDataType;
import com.backend.testdata.dto.SchemaFieldDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class SchemaFieldRequest {
    private String fieldName;
    private MockDataType mockDataType;
    private Integer fieldOrder;
    private Integer blankPercent;
    private String typeOptionJson;
    private String forceValue;


    public SchemaFieldDto toDto() {
        return SchemaFieldDto.of(
                this.fieldName,
                this.mockDataType,
                this.fieldOrder,
                this.blankPercent,
                this.typeOptionJson,
                this.forceValue
        );
    }
}
