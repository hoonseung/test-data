package com.backend.testdata.configuration;

import static org.springframework.security.config.Customizer.withDefaults;

import com.backend.testdata.dto.security.GithubUser;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {


  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
            request -> request.requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .permitAll()
                .requestMatchers(HttpMethod.GET, "/", "/table-schema", "table-schema/export")
                .permitAll()
                .anyRequest().authenticated())
        .oauth2Login(withDefaults())
        .logout(logout -> logout.logoutSuccessUrl("/"));

    return http.build();
  }


  @Bean
  public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
    final DefaultOAuth2UserService delegatingOAuth2UserService = new DefaultOAuth2UserService();

    return userRequest -> GithubUser.create(
        delegatingOAuth2UserService.loadUser(userRequest).getAttributes());
  }
}
