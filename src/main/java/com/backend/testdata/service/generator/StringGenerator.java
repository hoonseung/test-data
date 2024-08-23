package com.backend.testdata.service.generator;

import com.backend.testdata.domain.constants.MockDataType;
import com.backend.testdata.dto.MockDataDto;
import com.backend.testdata.repository.MockDataEntityRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Component
public class StringGenerator implements MockDataGenerator {

    private final MockDataEntityRepository mockDataEntityRepository;
    private final ObjectMapper objectMapper;

    private static final Integer DEFAULT_MIN_LENGTH = 1;
    private static final Integer DEFAULT_MAX_LENGTH = 10;

    @Override
    public MockDataType getType() {
        return MockDataType.STRING;
    }

    @Override
    public String generate(Integer blankPercent, String typeOptionJson, String forceValue) {
        List<MockDataDto> mockDataDtos = mockDataEntityRepository.findByMockDataType(getType())
            .stream()
            .map(MockDataDto::toDto)
            .toList();

        String extractBody = extractOnlyKorStringBody(mockDataDtos);

        return generateBody(typeOptionJson, extractBody);
    }


    private String generateBody(String typeOptionJson, String extractBody) {
        RandomGenerator randomGenerator = RandomGenerator.getDefault();
        Option option = getOptionCustomOrDefault(typeOptionJson);

        int offset = randomGenerator.nextInt(extractBody.length() - option.maxLength);
        int diff = option.maxLength - option.minLength;
        int limit = randomGenerator.nextInt((Math.max(1, diff))) + option.minLength;

        return extractBody.substring(offset, offset + limit);
    }


    private String extractOnlyKorStringBody(List<MockDataDto> mockDataDtos) {
        return mockDataDtos.stream()
            .map(MockDataDto::mockDataValue)
            .collect(Collectors.joining(""))
            .replaceAll("[^가-힣]", "");
    }


    private Option getOptionCustomOrDefault(String typeOptionJson) {
        if (StringUtils.hasText(typeOptionJson)) {
            try {
                Option option = objectMapper.readValue(typeOptionJson, Option.class);
                if (option.minLength < 1) {
                    throw new IllegalArgumentException(
                        "[가짜 데이터 생성 옵션 값 오류] minLength must be greater than 0");
                }else if (option.minLength > option.maxLength) {
                    throw new IllegalArgumentException("[가짜 데이터 생성 옵션 값 오류] minLength must be less than or equal to " + option.maxLength);
                }

                return option;
            } catch (JsonProcessingException jpe) {
                log.warn("JSON 옵션 정보를 해석하는 동안 예외가 발생하였습니다. 기본 옵션으로 동작합니다 - 입력 옵션 : {} - 필요 옵션 : {}",
                    typeOptionJson, new Option(1, 10));
            }
        }
        return new Option(DEFAULT_MIN_LENGTH, DEFAULT_MAX_LENGTH);
    }


    public record Option(
        Integer minLength,
        Integer maxLength
    ) {

    }
}
