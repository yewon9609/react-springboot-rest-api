package com.prgrms.exception;

public class FailedInsertException extends RuntimeException{

    public FailedInsertException(String message) {
        super(message);
    }

    public FailedInsertException(String message, Throwable cause) {
        super(message, cause);
    }
}
