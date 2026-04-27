package com.sprigboot.test.exception;

public class ResourceFoundException extends RuntimeException{

    public ResourceFoundException(String message) {
        super(message);
    }

    public ResourceFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
