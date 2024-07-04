package com.example.simple_blog.config.filter;

import com.example.simple_blog.exception.token.AccessTokenExpiredException;
import com.example.simple_blog.exception.token.AccessTokenInvalidException;
import com.example.simple_blog.exception.token.RefreshTokenInvalidException;
import com.example.simple_blog.exception.token.RefreshTokenNotFoundException;
import com.example.simple_blog.security.jwt.JwtUtil;
import com.example.simple_blog.service.token.AccessTokenService;
import com.example.simple_blog.service.token.RefreshTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Arrays;

import static com.example.simple_blog.config.properties.TokenProperties.ACCESS_TOKEN_NAME;
import static com.example.simple_blog.config.properties.TokenProperties.REFRESH_TOKEN_NAME;


@Slf4j
public class CustomLogoutFilter extends GenericFilterBean {

    private final JwtUtil jwtUtil;

    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;

    @Builder
    public CustomLogoutFilter(JwtUtil jwtUtil, AccessTokenService accessTokenService, RefreshTokenService refreshTokenService) {
        this.jwtUtil = jwtUtil;
        this.accessTokenService = accessTokenService;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        this.doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {



        String requestURI = request.getRequestURI();
        if (!requestURI.matches("^\\/chat-blog/user/logout$")){
            filterChain.doFilter(request, response);
            return;
        }

        String method = request.getMethod();
        if (!method.equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        accessTokenSetBlackList(request);
        Cookie cookie = deleteRefreshToken(request);


        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void accessTokenSetBlackList(HttpServletRequest request) {
        String accessToken = request.getHeader(ACCESS_TOKEN_NAME);
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            throw new AccessTokenExpiredException();
        }

        String category = jwtUtil.getCategory(accessToken);
        if (!category.equals(ACCESS_TOKEN_NAME)) {
            throw new AccessTokenInvalidException();
        }
        String address = jwtUtil.getAddress(accessToken);
        accessTokenService.setBlackList(accessToken);
        log.info("set black list access token = {}", accessToken);
    }

    public Cookie deleteRefreshToken(HttpServletRequest request) {

        for (Cookie cookie : request.getCookies()) {
            log.info("cookie {}, {}",cookie.getName(), cookie.getValue());
        }

        String refresh = Arrays.stream(request.getCookies())
                .filter(c -> REFRESH_TOKEN_NAME.equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(RefreshTokenNotFoundException::new);

        log.info("refresh {}",refresh);
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            throw new RefreshTokenInvalidException("만기된 refresh token 입니다.");
        }

        String category = jwtUtil.getCategory(refresh);
        if ((!category.equals(REFRESH_TOKEN_NAME))) {
            throw  new RefreshTokenInvalidException();
        }
        refreshTokenService.deleteByRefresh(refresh);


        //Refresh 토큰 Cookie 값 0
        Cookie cookie = new Cookie(REFRESH_TOKEN_NAME, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        return cookie;
    }
}
