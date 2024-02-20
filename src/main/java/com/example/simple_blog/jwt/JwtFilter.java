package com.example.simple_blog.jwt;

import com.example.simple_blog.exception.token.TokenExpiredException;
import com.example.simple_blog.exception.token.TokenNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer" )){
            throw new TokenNotFoundException();
        }

        String token = authorization.split(" ")[1];

        if (jwtUtil.isExpired(token)) {
            throw new TokenExpiredException();
        }

        String address = jwtUtil.getAddress(token);

    }
}
