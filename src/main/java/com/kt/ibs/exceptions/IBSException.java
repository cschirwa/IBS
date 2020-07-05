package com.kt.ibs.exceptions;

public class IBSException extends RuntimeException {
    public IBSException(String message) {
        super(message);
    }

    public IBSException(String message, Throwable cause) {
        super(message, cause);
    }
}
