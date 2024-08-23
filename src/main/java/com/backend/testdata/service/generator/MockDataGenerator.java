package com.backend.testdata.service.generator;

import com.backend.testdata.domain.constants.MockDataType;

/**
 * @author hoonseung
 */
public interface MockDataGenerator {

    /**
     * 구현체가 처리하는 가짜 데이터 유형을 반호나하는 메서드
     *
     * @return 구현체가 다루는 가짜 데이터 유형
     */
    MockDataType getType();

    /**
     * @param blankPercent   빈 값의 비율 백분율 (0 ~ 100) 모두 빈값은 null 로 표현
     * @param typeOptionJson 부가 옵션을 담은 Json 문자열
     * @param forceValue     강제로 입력할 값
     * @return 가짜 데이터 문자열
     */
    String generate(Integer blankPercent, String typeOptionJson, String forceValue);

}
