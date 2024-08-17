package com.backend.testdata.dto;

import com.backend.testdata.domain.MockDataEntity;
import com.backend.testdata.domain.constants.MockDataType;

public record MockDataDto(
        Long id,
        MockDataType mockDataType,
        String mockDataValue
) {

    public static MockDataDto toDto(MockDataEntity entity) {
        return new MockDataDto(
                entity.getId(),
                entity.getMockDataType(),
                entity.getMockDataValue()
        );
    }
}
