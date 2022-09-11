package com.clairvoyant.iPlanner.Exceptions;

public class RequestValidationException extends Exception {
    public RequestValidationException(String errorMessage) {
        super(errorMessage);
    }
}
