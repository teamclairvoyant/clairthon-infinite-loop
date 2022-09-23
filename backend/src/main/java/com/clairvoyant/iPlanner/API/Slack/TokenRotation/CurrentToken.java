package com.clairvoyant.iPlanner.API.Slack.TokenRotation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentToken {
    private String accessToken;
    private String refreshToken;
    private long expiresAt; // Unix time in millis
}
