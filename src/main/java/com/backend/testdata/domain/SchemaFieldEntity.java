package com.backend.testdata.domain;

import com.backend.testdata.domain.constants.MockDataType;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class SchemaFieldEntity {
    

    private String fieldName;

    private MockDataType mockDataType;

    private Integer fieldOrder;

    private Integer blankPercent;

    private String typeOptionJson;

    private String forceValue;


}
