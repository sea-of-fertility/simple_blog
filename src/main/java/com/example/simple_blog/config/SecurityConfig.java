package com.example.simple_blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/home", "/join", "/login").permitAll()
                        .requestMatchers("/post/*", "/mypage").hasAnyRole("USER", "ADMIN")
                        .anyRequest().denyAll());

        http
                .csrf(AbstractHttpConfigurer::disable);

        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin((AbstractHttpConfigurer::disable));

        return http.build();
    }
}
