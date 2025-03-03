package com.dm.learning.exceptions;

public class ApiRequestException extends RuntimeException {
    public ApiRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
