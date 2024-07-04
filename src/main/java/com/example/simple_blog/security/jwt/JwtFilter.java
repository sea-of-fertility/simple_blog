package com.example.simple_blog.security.jwt;

import com.example.simple_blog.security.MemberDetail;
import com.example.simple_blog.exception.token.AccessTokenExpiredException;
import com.example.simple_blog.exception.token.AccessTokenInvalidException;
import com.example.simple_blog.service.token.AccessTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.example.simple_blog.config.properties.TokenProperties.ACCESS_TOKEN_NAME;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final AccessTokenService accessTokenService;

    @Builder
    public JwtFilter(JwtUtil jwtUtil, AccessTokenService accessTokenService) {
        this.jwtUtil = jwtUtil;
        this.accessTokenService = accessTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String accessToken = request.getHeader(ACCESS_TOKEN_NAME);
        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if(!accessTokenService.isValidAccessToken(accessToken)){
            filterChain.doFilter(request, response);
            return;
        }

        try{
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            throw new AccessTokenExpiredException();
        }

        String category = jwtUtil.getCategory(accessToken);
        if (!category.equals(ACCESS_TOKEN_NAME)) {
            throw new AccessTokenInvalidException();
        }

        String address = jwtUtil.getAddress(accessToken);
        String role = jwtUtil.getRole(accessToken);

        MemberDetail build = MemberDetail.builder()
                .address(address)
                .role(role)
                .build();

        Authentication authToken = new UsernamePasswordAuthenticationToken(build,
                null,
                build.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }
}
