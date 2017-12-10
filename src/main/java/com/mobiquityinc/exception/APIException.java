package com.mobiquityinc.exception;

public class APIException extends Exception {

    public APIException(String _message) {
        super(_message);
    }

    public APIException(String _message, Throwable _cause) {
        super(_message, _cause);
    }
}
