package com.github.wakingrufus.elo.api.error;


public class ValidationException extends RuntimeException {
    public ValidationException(String s) {
        super(s);
    }
}
