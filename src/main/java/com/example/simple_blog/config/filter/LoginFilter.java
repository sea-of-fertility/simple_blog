package com.example.simple_blog.config.filter;

import com.example.simple_blog.config.properties.TokenProperties;
import com.example.simple_blog.domain.Refresh;
import com.example.simple_blog.exception.member.login.LoginFailedException;
import com.example.simple_blog.security.jwt.JwtUtil;
import com.example.simple_blog.service.token.RefreshTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import static com.example.simple_blog.config.properties.TokenProperties.ACCESS_TOKEN_NAME;
import static com.example.simple_blog.config.properties.TokenProperties.REFRESH_TOKEN_NAME;


@Slf4j
public class LoginFilter extends AbstractAuthenticationProcessingFilter{


    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final TokenProperties tokenProperties;
    private final RefreshTokenService refreshTokenService;


    @Builder
    public LoginFilter(String defaultFilterProcessesUrl, String defaultFilterProcessesUrl1, JwtUtil jwtUtil,
                       AuthenticationManager authenticationManager, ObjectMapper objectMapper, TokenProperties tokenProperties,
                       RefreshTokenService refreshTokenService) {
        super(defaultFilterProcessesUrl);
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.objectMapper = objectMapper;
        this.tokenProperties = tokenProperties;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException {

        LoginJson loginJson = objectMapper.readValue(request.getInputStream(), LoginJson.class);
        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken
                .unauthenticated(loginJson.getAddress(),
                        loginJson.getPassword());

        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);

        return this.authenticationManager.authenticate(authRequest);
    }

    protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        log.info("===========LoginFilter============");

        String address = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String access = jwtUtil.createJwt(ACCESS_TOKEN_NAME, address, role,
                tokenProperties.getAccessTokenExpirationMinutes());
        String refresh = jwtUtil.createJwt(REFRESH_TOKEN_NAME, address, role,
                tokenProperties.getRefreshTokenExpirationDays());

        addRefresh(address, refresh, tokenProperties.getRefreshTokenExpirationDays());

        response.setHeader(ACCESS_TOKEN_NAME, access);
        response.addCookie(createCookie(REFRESH_TOKEN_NAME, refresh));
        response.setStatus(HttpStatus.OK.value());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        throw new LoginFailedException();
    }

    private void addRefresh(String address, String refresh, Long expiredMs) {
        Date date = new Date(System.currentTimeMillis() + expiredMs);
        Refresh refreshDomain = Refresh.builder()
                .userAddress(address)
                .refresh(refresh)
                .expired(date.getTime())
                .build();

        refreshTokenService.save(refreshDomain);
    }

    @Getter
    private static class LoginJson {
        private String address;
        private String password;
    }


    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setHttpOnly(true);
        return cookie;
    }
}
