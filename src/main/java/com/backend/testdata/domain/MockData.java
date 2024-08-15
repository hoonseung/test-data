package com.backend.testdata.domain;


import com.backend.testdata.domain.constants.MockDataType;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class MockData {

    private MockDataType mockDataType;

    private String mockDataValue;
}
