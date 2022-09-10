package com.clairvoyant.iPlanner.Shared;

public class RequestValidationException extends Exception {
    public RequestValidationException(String errorMessage) {
        super(errorMessage);
    }
}
