package com.backend.testdata.dto.security;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public record GithubUser(

    String id,
    String name,
    String email
) implements OAuth2User {


  public static GithubUser create(Map<String, Object> attributes) {
    return new GithubUser(
        String.valueOf(attributes.get("login")),
        String.valueOf(attributes.get("name")),
        String.valueOf(attributes.get("email"))
    );
  }


  @Override
  public String getName() {
    return name.equals("null") ? String.valueOf(id) : name;
  }


  @Override
  public Map<String, Object> getAttributes() {
    return Map.of();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }


}
