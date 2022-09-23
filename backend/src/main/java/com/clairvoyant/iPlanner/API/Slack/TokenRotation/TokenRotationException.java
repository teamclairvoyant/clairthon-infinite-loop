package com.clairvoyant.iPlanner.API.Slack.TokenRotation;

import lombok.Data;

@Data
public class TokenRotationException extends Exception {
    private String message;
    private Throwable cause;

    public TokenRotationException(String message) {
        this(message, null);
    }

    public TokenRotationException(String message, Throwable cause) {
        this.message = message;
        this.cause = cause;
    }
}
