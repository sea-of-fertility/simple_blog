package com.example.simple_blog.config;

import com.example.simple_blog.config.filter.LoginFilter;
import com.example.simple_blog.domain.member.Member;
import com.example.simple_blog.jwt.JwtFilter;
import com.example.simple_blog.jwt.JwtUtil;
import com.example.simple_blog.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;
    private final AuthenticationConfiguration authenticationConfiguration;


    @Value("${login.url}")
    private String defaultUrl;

    @Bean
    public PasswordEncoder passwordencoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        http
                .sessionManagement((auth) -> auth.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.
                securityMatcher("/login")
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/join", "/").permitAll()
                        .requestMatchers("/admin", "/myPage").hasAnyRole("ADMIN", "USER")
                        .anyRequest().permitAll());

        http
                .addFilterAt(LoginFilter.builder()
                                .default_url(defaultUrl)
                                .obj(objectMapper)
                                .jwtUtil(jwtUtil)
                                .authenticationManager(authenticationManager(authenticationConfiguration))
                                .build()
                        , UsernamePasswordAuthenticationFilter.class);

        http
                .addFilterAfter(new JwtFilter(jwtUtil), LoginFilter.class);

        http
                .csrf(AbstractHttpConfigurer::disable);

        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin((AbstractHttpConfigurer::disable));

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(MemberRepository memberRepository) {
        return username -> {
            Member user = memberRepository.
                    findByAddress(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Can't find " + username));
            return new MemberDetail(user);
        };
    }
}
