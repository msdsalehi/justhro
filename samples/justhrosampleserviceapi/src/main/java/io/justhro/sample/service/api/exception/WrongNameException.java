package io.justhro.sample.service.api.exception;

import io.justhro.core.exception.JustAPIException;

public class WrongNameException extends JustAPIException {

    public WrongNameException() {
    }

    public WrongNameException(String message) {
        super(message);
    }

    public WrongNameException(String message, String localizedMessage) {
        super(message, localizedMessage);
    }

    public WrongNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongNameException(Throwable cause) {
        super(cause);
    }

    public WrongNameException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public int getHttpStatus() {
        return 400;
    }

    @Override
    public String getCode() {
        return "1003412";
    }
}
