package com.example.simple_blog.config.schedul;


import com.example.simple_blog.service.token.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefreshTokenCheck {

    private final RefreshTokenService refreshTokenService;


    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void reportCurrentTime() {
        refreshTokenService.deleteExpiredRefreshToken();
    }

}
