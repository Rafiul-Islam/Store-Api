package com.storeapi.auth;

import com.storeapi.users.Role;
import com.storeapi.users.UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final UserDetailsService userDetailsService;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .csrf(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests(auth ->
        auth
          .requestMatchers("/swagger-ui/**").permitAll()
          .requestMatchers("/swagger-ui.html").permitAll()
          .requestMatchers("/v3/api-docs/**").permitAll()
          .requestMatchers("/api/auth/login").permitAll()
          .requestMatchers("/api/auth/refresh").permitAll()
          .requestMatchers("/api/carts/**").permitAll()
          .requestMatchers("/api/admin/**").hasRole(Role.ADMIN.name())
          .requestMatchers(HttpMethod.POST, "/api/checkout/webhook").permitAll()
          .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
          .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
          .requestMatchers(HttpMethod.POST, "/api/products/**").hasRole(Role.ADMIN.name())
          .requestMatchers(HttpMethod.PUT, "/api/products/**").hasRole(Role.ADMIN.name())
          .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole(Role.ADMIN.name())
          .anyRequest().authenticated()
      )
      .addFilterBefore(
        jwtAuthenticationFilter,
        UsernamePasswordAuthenticationFilter.class
      )
      .exceptionHandling(exceptionHandlingConfigurer -> {
        exceptionHandlingConfigurer.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
        exceptionHandlingConfigurer.accessDeniedHandler((request, response, accessDeniedException) -> response.setStatus(HttpStatus.FORBIDDEN.value()));
      });

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
    authenticationProvider.setPasswordEncoder(passwordEncoder());
    return authenticationProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(
    AuthenticationConfiguration configuration
  ) throws Exception {
    return configuration.getAuthenticationManager();
  }
}
