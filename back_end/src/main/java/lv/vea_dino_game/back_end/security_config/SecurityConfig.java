package lv.vea_dino_game.back_end.security_config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationProvider;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  
  private final AuthenticationProvider authProvider;
  private final JwtAuthFilter jwtFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(request -> request
            .requestMatchers("/api/v1/auth/**", "/h2-console/**", "/api/v1/clans/**", "/api/v1/player/**").permitAll()
            .anyRequest().authenticated())
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // authProvider set up to authenticate users using repo attached to a DB
        .authenticationProvider(authProvider)
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        // h2-console's frames were not getting displayed, not planning to keep h2 but will keep this config as a reminder and reference
        .headers(headers -> headers.frameOptions(frOp -> frOp.sameOrigin()));
    return http.build();
  }
  
}
