package com.backend.testdata.controller;

import com.backend.testdata.configuration.SecurityConfiguration;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Disabled("해당 테스트는 #24 이슈에서 진행되었음, 구현은 진행하지않고 테스트로 기초 스펙만 정의함")
@DisplayName("[Controller] 회원")
@Import(SecurityConfiguration.class)
@AutoConfigureMockMvc
@WebMvcTest
public record UserAccountControllerTest(
        @Autowired MockMvc mvc
) {


    @DisplayName("[GET] 내 정보를 요청하면 내 정보 뷰 (정상) 를 반환한다.")
    @WithMockUser
    @Test
    void whenEnteredMyAccountPage_thenShowMyAccountView() throws Exception {
        //given

        //when
        mvc.perform(get("/my-account"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("my-account"));

        //then
    }
}
