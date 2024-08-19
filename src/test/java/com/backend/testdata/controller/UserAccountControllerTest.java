package com.backend.testdata.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.backend.testdata.configuration.SecurityConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

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
        .andExpect(model().attributeExists("nickname"))
        .andExpect(model().attributeExists("email"))
        .andExpect(view().name("my-account"));

    //then
  }
}
