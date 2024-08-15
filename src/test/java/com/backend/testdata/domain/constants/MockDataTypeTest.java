package com.backend.testdata.domain.constants;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("[Domain] 테스트 데이터 자료형 테스트")
class MockDataTypeTest {


    @DisplayName("자료형이 주어지면, 해당 원소의 이름을 리턴한다.")
    @Test
    void givenMockDataType_whenReading_thenReturnEnumElementName() {
        //given
        MockDataType mockDataType = MockDataType.STRING;

        //when
        String elementName = mockDataType.toString();

        //then
        Assertions.assertThat(MockDataType.STRING.name()).isEqualTo(elementName);
    }


    @DisplayName("자료형이 주어지면 해당 원소의 데이터를 DTO 로 랩핑해서 리턴한다.")
    @Test
    void givenMockDataType_whenReading_thenReturnEnumElementDto() {
        //given
        MockDataType mockDataType = MockDataType.STRING;

        //when
        MockDataType.MockDataTypeObject expectedObject = mockDataType.toObject();

        //then
        Assertions.assertThat(expectedObject.toString()).contains("name", "options", "baseType");
    }


}