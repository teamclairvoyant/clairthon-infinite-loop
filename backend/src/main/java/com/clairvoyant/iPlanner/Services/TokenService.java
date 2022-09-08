package com.clairvoyant.iPlanner.Services;

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

    public boolean validateToken(HttpServletRequest request) {
        try {
            if (request.getHeader(Literal.TOKEN) == null || request.getHeader(Literal.LOGIN_ID) == null) {
                // if both token and login_id are not present in header
                return Literal.FALSE;
            } else if (request.getHeader(Literal.LOGIN_ID).equals(Literal.admin) && request.getHeader(Literal.TOKEN).equals(Literal.iPlanner)) {
                // hardcoded token and login_id
                // todo :: change this later when signup is implemented for admin access, fetch token from mongo
                return Literal.TRUE;
            } else {
                return Literal.FALSE;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Literal.FALSE;
        }
    }
}
