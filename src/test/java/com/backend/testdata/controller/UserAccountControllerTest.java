package com.backend.testdata.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.backend.testdata.configuration.SecurityConfiguration;
import com.backend.testdata.dto.security.GithubUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("[Controller] 회원")
@Import(SecurityConfiguration.class)
@WebMvcTest(UserAccountController.class)
public record UserAccountControllerTest(
    @Autowired MockMvc mvc
) {


  @DisplayName("[GET] 내 정보를 요청하면 내 정보 뷰 (정상) 를 반환한다.")
  @Test
  void whenEnteredMyAccountPage_thenShowMyAccountView() throws Exception {
    //given
    var githubUser = new GithubUser("test_id", "test_name", "test@email.com");

    //when
    mvc.perform(get("/my-account")
            .with(oauth2Login().oauth2User(githubUser)))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
        .andExpect(model().attribute("nickname", githubUser.getName()))
        .andExpect(model().attribute("email", githubUser.email()))
        .andExpect(view().name("my-account"));

    //then
  }
}
