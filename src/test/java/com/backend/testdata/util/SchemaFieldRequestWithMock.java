package com.backend.testdata.util;

import com.backend.testdata.domain.constants.MockDataType;
import com.backend.testdata.dto.request.SchemaFieldRequest;
import com.backend.testdata.dto.request.TableSchemaRequest;

import java.util.List;

public class SchemaFieldRequestWithMock {

    public static SchemaFieldRequest create(String fieldName, MockDataType mockDataType, Integer fieldOrder, Integer blankPercent, String typeOptionJson, String forceValue) {
        return SchemaFieldRequest.of(
                fieldName,
                mockDataType,
                fieldOrder,
                blankPercent,
                typeOptionJson,
                forceValue
        );
    }
}
