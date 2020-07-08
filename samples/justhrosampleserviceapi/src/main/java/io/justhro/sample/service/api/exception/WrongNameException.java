package io.justhro.sample.service.api.exception;

import io.justhro.core.exception.JustAPIException;

public class WrongNameException extends JustAPIException {

    public WrongNameException(String message, Throwable cause, JustAPIException rootCause) {
        super(message, cause, rootCause);
    }

    public WrongNameException(Throwable cause, JustAPIException rootCause) {
        super(cause, rootCause);
    }

    public WrongNameException(String message, JustAPIException rootCause) {
        super(message, rootCause);
    }

    public WrongNameException(JustAPIException rootCause) {
        super(rootCause);
    }

    public WrongNameException() {
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
