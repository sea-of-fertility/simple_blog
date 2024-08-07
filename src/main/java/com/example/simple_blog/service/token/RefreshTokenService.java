package com.example.simple_blog.service.token;


import com.example.simple_blog.config.properties.TokenProperties;
import com.example.simple_blog.domain.token.Refresh;
import com.example.simple_blog.exception.token.RefreshTokenNotFoundException;
import com.example.simple_blog.dto.response.NewToken;
import com.example.simple_blog.security.jwt.JwtUtil;
import com.example.simple_blog.repository.token.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;

import static com.example.simple_blog.config.properties.TokenProperties.ACCESS_TOKEN_NAME;
import static com.example.simple_blog.config.properties.TokenProperties.REFRESH_TOKEN_NAME;


@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshRepository refreshRepository;
    private final JwtUtil jwtUtil;
    private final TokenProperties tokenProperties;




    public void deleteByRefresh(String refresh) {
        refreshRepository.deleteByRefresh(refresh);
    }


    public NewToken checkCookie(Cookie[] cookies) {

        String refresh = Arrays.stream(cookies)
                .filter(cookie -> REFRESH_TOKEN_NAME.equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(RefreshTokenNotFoundException::new);

        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        String category = jwtUtil.getCategory(refresh);

        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            throw new RefreshTokenNotFoundException();
        }

        if (!category.equals(REFRESH_TOKEN_NAME) || !isExist) {
            throw new RefreshTokenNotFoundException();
        }

        String address = jwtUtil.getAddress(refresh);
        String role = jwtUtil.getRole(refresh);

        String newAccess = jwtUtil
                .createJwt(ACCESS_TOKEN_NAME, address, role,
                        tokenProperties.getAccessTokenExpirationMinutes());
        String newRefresh = jwtUtil
                .createJwt(REFRESH_TOKEN_NAME, address, role,
                        tokenProperties.getRefreshTokenExpirationDays());

        refreshRepository.deleteByMemberAddress(address);

        this.save(address, refresh, tokenProperties.getRefreshTokenExpirationDays());
        Cookie newCookies = createCookies(REFRESH_TOKEN_NAME, newRefresh);

        return NewToken.builder()
                .newAccessToken(newAccess)
                .newCookie(newCookies)
                .build();
    }


    private Cookie createCookies(String key, String value){
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        cookie.setHttpOnly(true);
        return cookie;
    }

    public void save(String address, String refresh, Long expiredMs) {
        Date date = new Date(System.currentTimeMillis() + expiredMs);
        Refresh refreshEntity = Refresh.builder()
                .memberAddress(address)
                .refresh(refresh)
                .expired(date.getTime())
                .build();

        refreshRepository.save(refreshEntity);
    }

    public void save(Refresh refresh) {
        if (refreshRepository.existsByMemberAddress(refresh.getMemberAddress())) {
            refreshRepository.deleteByMemberAddress(refresh.getMemberAddress());
        }
            refreshRepository.save(refresh);
    }


    public void deleteExpiredRefreshToken() {
        refreshRepository.findAll().stream()
                .filter((refresh) -> refresh.getExpired() <= System.currentTimeMillis())
                .forEach((domain) -> refreshRepository.deleteById(domain.getId()));
    }

}
