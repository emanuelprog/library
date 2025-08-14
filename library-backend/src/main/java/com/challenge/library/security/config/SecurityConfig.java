package com.challenge.library.security.config;

import com.challenge.library.security.jwt.JwtAuthFilter;
import com.challenge.library.security.jwt.JwtService;
import com.challenge.library.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/v1/auth/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/books/**")
                        .hasAnyRole("READER", "WRITER")
                        .requestMatchers("/v1/genres/**", "/v1/authors/**")
                        .hasRole("WRITER")
                        .anyRequest().authenticated()
                );

        http.addFilterBefore(
                new JwtAuthFilter(jwtService, userDetailsService),
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }
}
