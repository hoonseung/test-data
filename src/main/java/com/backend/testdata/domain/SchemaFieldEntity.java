package com.backend.testdata.domain;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class SchemaFieldEntity {
    

    private String fieldName;

    private String mockDataType;

    private Integer fieldOrder;

    private Integer blankPercent;

    private String typeOptionJson;

    private String forceValue;


}
