package com.lesartilleursapi.auth.security.config;

import com.lesartilleursapi.auth.security.jwt.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class WebSecurityConfig {

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12);
  }

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      JwtFilter jwtFilter,
      AuthEntryPointJwt authEntryPointJwt
  ) throws Exception {

    return http
        .csrf(csrf -> csrf.disable())
        // Pour Angular, on activera CORS proprement ensuite (CorsConfigurationSource)
        .cors(cors -> cors.disable())
        .exceptionHandling(e -> e.authenticationEntryPoint(authEntryPointJwt))
        .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/auth/**").permitAll()
            .requestMatchers("/public/**").permitAll()
            .requestMatchers("/admin/**").permitAll()
            // exemple: /api/v1/admin/** réservé ADMIN/SUPER_ADMIN
            // .requestMatchers("/api/v1/admin/**").hasAnyRole("ADMIN","SUPER_ADMIN")
            .anyRequest().authenticated()
        )
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }
}
