package com.backend.testdata.service.generator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.backend.testdata.domain.MockDataEntity;
import com.backend.testdata.domain.constants.MockDataType;
import com.backend.testdata.repository.MockDataEntityRepository;
import com.backend.testdata.service.generator.StringGenerator.Option;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("[Logic] 문자열 데이터 생성기 테스트")
@ExtendWith(MockitoExtension.class)
class StringGeneratorTest {

    @InjectMocks
    private StringGenerator sut;

    @Mock
    private MockDataEntityRepository mockDataEntityRepository;
    @Spy
    private ObjectMapper objectMapper;


    @DisplayName("가짜 데이터 유형에서 문자열 타입을 반환한다.")
    @Test
    void checkMockDataType() {
        //given

        //when
        MockDataType result = sut.getType();

        //then
        assertThat(result).isEqualTo(MockDataType.STRING);
    }


    @DisplayName("가짜 데이터 중 한글만 허용하여 생성하는지 확인한다.")
    @RepeatedTest(10)
    void givenParams_whenGenerateString_thenCheckOnlyKorText() throws JsonProcessingException {
        //given
        Option option = new Option(1, 20);
        String optionToJson = objectMapper.writeValueAsString(option);
        given(mockDataEntityRepository.findByMockDataType(MockDataType.STRING))
            .willReturn(List.of(
                    MockDataEntity.of(MockDataType.STRING,
                        "정거장까지 끌어다주고 그 깜짝 놀란 일 원 오십 전을 정말 제 손에 쥠에, 제 말마따나 십 리나 되는 길을 비를 맞아 가며 질퍽거리고 온 생각은 아니하고, 거저나 얻은 듯이 고마왔다. 졸부나 된 듯이 기뻤다. 제자식 뻘밖에 안되는 어린 손님에게 몇 번 허리를 굽히며, 안녕히 다녀옵시요.라고 깍듯이 재우쳤다."),
                    MockDataEntity.of(MockDataType.STRING,
                        "Then I'll give you whatever you ask for, so go quickly. 그러면 달라는 대로 줄 터이니 빨리 가요. 관대한 어린 손님은 그런 말을 남기고 총총히 옷도 입고 짐도 챙기러 갈 데로 갔다")
                )
            );

        //when
        String result = sut.generate(0, optionToJson, null);

        //then
        System.out.println(result);
        assertThat(result)
            .containsPattern("^[가-힣]{1,20}$");
        then(mockDataEntityRepository).should().findByMockDataType(MockDataType.STRING);
        then(objectMapper).should().writeValueAsString(option);
    }


    @DisplayName("주어진 옵션이 적용되어 올바르게 생성하는지 확인한다.")
    @CsvSource(textBlock = """
            { "minLength" : 1, "maxLength" : 1 } | ^[가-힣]{1,20}$
            { "minLength" : 1, "maxLength" : 5 } | ^[가-힣]{1,5}$
            { "minLength" : 1, "maxLength" : 10 } | ^[가-힣]{1,10}$
            { "minLength" : 1, "maxLength" : 20 } | ^[가-힣]{1,20}$
            { "minLength" : 1, "maxLength" : 30 } | ^[가-힣]{1,30}$
        """, delimiter = '|'
    )
    @ParameterizedTest(name = "{index}. {0} ===> 통과 정규식 : {1}")
    void givenParams_whenGenerateString_thenCheckOnlyKorText(String jsonOption,
        String expectedPattern) {
        //given
        given(mockDataEntityRepository.findByMockDataType(MockDataType.STRING))
            .willReturn(List.of(
                    MockDataEntity.of(MockDataType.STRING,
                        "정거장까지 끌어다주고 그 깜짝 놀란 일 원 오십 전을 정말 제 손에 쥠에, 제 말마따나 십 리나 되는 길을 비를 맞아 가며 질퍽거리고 온 생각은 아니하고, 거저나 얻은 듯이 고마왔다. 졸부나 된 듯이 기뻤다. 제자식 뻘밖에 안되는 어린 손님에게 몇 번 허리를 굽히며, 안녕히 다녀옵시요.라고 깍듯이 재우쳤다."),
                    MockDataEntity.of(MockDataType.STRING,
                        "Then I'll give you whatever you ask for, so go quickly. 그러면 달라는 대로 줄 터이니 빨리 가요. 관대한 어린 손님은 그런 말을 남기고 총총히 옷도 입고 짐도 챙기러 갈 데로 갔다")
                )
            );

        //when
        String result = sut.generate(0, jsonOption, null);

        //then
        System.out.println(result);
        assertThat(result)
            .containsPattern(expectedPattern);
        then(mockDataEntityRepository).should().findByMockDataType(MockDataType.STRING);
    }


    @DisplayName("잘못된 옵션 내용이 주어지면 예외를 발생시킨다.")
    @CsvSource(textBlock = """
            { "minLength" : 0, "maxLength" : 0 }
            { "minLength" : 0, "maxLength" : 5 }
            { "minLength" : 2, "maxLength" : 1 }
            { "minLength" : 10, "maxLength" : 8 }
        """, delimiter = '|'
    )
    @ParameterizedTest(name = "{index}. 잘못된 옵션 : {0}")
    void givenWrongOptionValue_whenGenerating_thenThrowException(String jsonOption)
        throws JsonProcessingException {
        //given

        //when
        Throwable exception = catchThrowable(() -> sut.generate(0, jsonOption, null));

        //then
        assertThat(exception)
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageStartingWith("[가짜 데이터 생성 옵션 값 오류]");

        then(objectMapper).should().readValue(jsonOption, Option.class);
    }


    @DisplayName("옵션 내용이 주어지지 않으면 기본 옵션을 적용시킨다.")
    @Test
    void givenNullOptionValue_whenGenerating_thenApplyDefaultOption() {
        //given
        given(mockDataEntityRepository.findByMockDataType(MockDataType.STRING))
            .willReturn(List.of(
                    MockDataEntity.of(MockDataType.STRING,
                        "정거장까지 끌어다주고 그 깜짝 놀란 일 원 오십 전을 정말 제 손에 쥠에, 제 말마따나 십 리나 되는 길을 비를 맞아 가며 질퍽거리고 온 생각은 아니하고, 거저나 얻은 듯이 고마왔다. 졸부나 된 듯이 기뻤다. 제자식 뻘밖에 안되는 어린 손님에게 몇 번 허리를 굽히며, 안녕히 다녀옵시요.라고 깍듯이 재우쳤다."),
                    MockDataEntity.of(MockDataType.STRING,
                        "Then I'll give you whatever you ask for, so go quickly. 그러면 달라는 대로 줄 터이니 빨리 가요. 관대한 어린 손님은 그런 말을 남기고 총총히 옷도 입고 짐도 챙기러 갈 데로 갔다")
                )
            );

        //when
        String result = sut.generate(0, null, null);

        //then
        System.out.println(result);
        assertThat(result)
            .containsPattern("^[가-힣]{1,10}$");
        then(mockDataEntityRepository).should().findByMockDataType(MockDataType.STRING);
        then(objectMapper).shouldHaveNoInteractions();
    }


}