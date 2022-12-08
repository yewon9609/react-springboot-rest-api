package com.prgrms.exception;

public class FailedUpdateException extends RuntimeException{

    public FailedUpdateException(String message) {
        super(message);
    }

    public FailedUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
