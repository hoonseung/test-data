package com.backend.testdata.service.generator;

import com.backend.testdata.domain.constants.MockDataType;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class MockDataGeneratorContext {

    private final Map<MockDataType, MockDataGenerator> mockDataGeneratorMap;

    public MockDataGeneratorContext(List<MockDataGenerator> mockDataGenerators) {
        this.mockDataGeneratorMap = mockDataGenerators.stream()
            .collect(Collectors.toMap(MockDataGenerator::getType, Function.identity()));
    }


    public String delegatingGenerate(MockDataType dataType, Integer blankPercent,
        String typeOptionJson, String forceValue) {

        MockDataGenerator mockDataGenerator = mockDataGeneratorMap.get(dataType);

        return mockDataGenerator.generate(blankPercent, typeOptionJson, forceValue);
    }
}
