package com.clairvoyant.iPlanner.Services;

import com.clairvoyant.iPlanner.Exceptions.TokenValidationException;
import com.clairvoyant.iPlanner.Utility.Literal;

import javax.servlet.http.HttpServletRequest;

public class TokenService {

    private static TokenService instance;

    /**
     * Singleton class Lazy initialization with Double check locking
     *
     * @return TokenService
     */
    public static TokenService getInstance() {
        synchronized (TokenService.class) {
            if (instance == null) {
                instance = new TokenService();
            }
            return instance;
        }
    }

    /**
     * private constructor for singleton class
     */
    private TokenService() {
    }

    public void validateToken(HttpServletRequest request) throws TokenValidationException {
        if (request.getHeader(Literal.TOKEN) == null) {
            throw new TokenValidationException("TOKEN missing in request header");
        }

        if (request.getHeader(Literal.LOGIN_ID) == null) {
            throw new TokenValidationException("LOGIN_ID missing in request header");
        }

        String TOKEN = request.getHeader(Literal.TOKEN);
        String LOGIN_ID = request.getHeader(Literal.LOGIN_ID);

        // todo :: change this later after signup is implemented for admin access, fetch user token from mongo
        if (LOGIN_ID.equals(Literal.admin) && TOKEN.equals(Literal.iPlanner)) {
            // valid token, don't throw exception
        } else {
            throw new TokenValidationException("Invalid TOKEN for given LOGIN_ID :: "+LOGIN_ID);
        }

    }
}
