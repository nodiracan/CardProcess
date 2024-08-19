package com.example.demo.dto.authUser;

import lombok.Builder;

@Builder
public record SessionDTO(  String accessToken, String refreshToken, String tokenType,
                           Long refreshTokenExpire,
                           Long issueAt,
                           Long expiresIn) {
}
