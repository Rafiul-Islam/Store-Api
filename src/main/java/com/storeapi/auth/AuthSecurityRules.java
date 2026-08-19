package com.storeapi.auth;

import com.storeapi.common.SecurityRules;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class AuthSecurityRules implements SecurityRules {
  @Override
  public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
    registry.requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
      .requestMatchers(HttpMethod.POST, "/api/auth/refresh").permitAll()
      .requestMatchers(HttpMethod.GET, "/api/auth/**").authenticated()
      .requestMatchers(HttpMethod.POST, "/api/auth/**").authenticated()
      .requestMatchers(HttpMethod.PUT, "/api/auth/**").authenticated()
      .requestMatchers(HttpMethod.DELETE, "/api/auth/**").authenticated();
  }
}
