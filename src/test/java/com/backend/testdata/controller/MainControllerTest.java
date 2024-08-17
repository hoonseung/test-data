package com.backend.testdata.controller;

import com.backend.testdata.configuration.SecurityConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("[Controller] 메인")
@Import(SecurityConfiguration.class)
@AutoConfigureMockMvc
@WebMvcTest(MainControllerTest.class)
record MainControllerTest(
        @Autowired MockMvc mvc
) {


    @DisplayName("[GET] 메인 페이지를 요청하면 메인 뷰 (정상) 를 반환한다.")
    @Test
    void whenEnteredMainPage_thenShowMainView() throws Exception {
        //given

        //when & then
        mvc.perform(get("/"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("index"))
                .andExpect(status().isOk());
    }
}