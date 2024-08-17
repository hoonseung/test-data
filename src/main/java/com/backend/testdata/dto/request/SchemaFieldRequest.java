package com.backend.testdata.dto.request;

import com.backend.testdata.domain.constants.MockDataType;
import com.backend.testdata.dto.SchemaFieldDto;

public record SchemaFieldRequest(
        String fieldName,
        MockDataType mockDataType,
        Integer fieldOrder,
        Integer blankPercent,
        String typeOptionJson,
        String forceValue
) {

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
