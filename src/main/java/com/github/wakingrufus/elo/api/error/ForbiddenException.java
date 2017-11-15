package com.github.wakingrufus.elo.api.error;


public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String s) {
        super(s);
    }
}
