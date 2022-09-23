package com.clairvoyant.iPlanner.API.Slack.TokenRotation;


import com.slack.api.RequestConfigurator;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.oauth.OAuthV2AccessResponse;
import lombok.Data;

import java.io.IOException;
import java.util.Optional;

/**
 * A utility to perform Slack token rotation.
 */
@Data
public class TokenRotator {
    private static MethodsClient DEFAULT_METHODS_CLIENT = Slack.getInstance().methods();
    public static long DEFAULT_MILLISECONDS_BEFORE_EXPIRATION = 1000 * 60 * 60 * 2;

    private static MethodsClient methodsClient;
    private static long millisecondsBeforeExpiration;
    private static final String clientId = "1597172380741.4120852374352";
    private static final String clientSecret = "58edff0f20daf2d7ac731736047c2d62";

    private static TokenRotator instance;

    public static TokenRotator getInstance() {
        if(instance == null) {
            instance = new TokenRotator();
        }
        return instance;
    }

    private TokenRotator() {
        this(DEFAULT_METHODS_CLIENT, DEFAULT_MILLISECONDS_BEFORE_EXPIRATION);
    }

    private TokenRotator(
            MethodsClient methodsClient,
            long millisecondsBeforeExpiration
    ) {
        TokenRotator.methodsClient = methodsClient;
        TokenRotator.millisecondsBeforeExpiration = millisecondsBeforeExpiration;
    }

    /**
     * Performs token rotation for the given set of token and refresh token. This method returns non-empty value
     * only when the rotation is done. If you get the refreshed token data, your datastore must update the database
     * record to have the new ones.
     */
    public Optional<RefreshedToken> performTokenRotation(
            RequestConfigurator<CurrentToken.CurrentTokenBuilder> configurator
    ) throws TokenRotationException {
        return performTokenRotation(configurator.configure(CurrentToken.builder()).build());
    }

    /**
     * Performs token rotation for the given set of token and refresh token. This method returns non-empty value
     * only when the rotation is done. If you get the refreshed token data, your datastore must update the database
     * record to have the new ones.
     */
    public static Optional<RefreshedToken> performTokenRotation(
            CurrentToken current
    ) throws TokenRotationException {
        if (current.getExpiresAt() >
                System.currentTimeMillis() + millisecondsBeforeExpiration) {
            // As the token is still active, no need to refresh the stored token
            return Optional.empty();
        }
        try {
            OAuthV2AccessResponse refreshResponse = methodsClient.oauthV2Access(r -> r
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .refreshToken(current.getRefreshToken())
                    .grantType("refresh_token")
            );
            if (!refreshResponse.isOk()) {
                throw new TokenRotationException(
                        "Received an error code in oauth.v2.access response body: " + refreshResponse.getError());
            }
            RefreshedToken refreshed = new RefreshedToken();
            refreshed.setAccessToken(refreshResponse.getAccessToken());
            refreshed.setRefreshToken(refreshResponse.getRefreshToken());
            refreshed.setExpiresAt(System.currentTimeMillis() + (refreshResponse.getExpiresIn() * 1000));
            return Optional.of(refreshed);
        } catch (IOException e) {
            throw new TokenRotationException(
                    "Failed to connect to the Slack server (error: " + e.getMessage() + ")", e);
        } catch (SlackApiException e) {
            String message = "Received unexpected HTTP status code from the Slack server (code: " +
                    e.getResponse().code() + ", body: " + e.getResponseBody() + ")";
            throw new TokenRotationException(message, e);
        }
    }
}
