package io.justhro.core.exception;

import org.springframework.http.HttpStatus;

public class JustBadRequestAPIException extends JustAPIException {

    public JustBadRequestAPIException() {
    }

    public JustBadRequestAPIException(String message) {
        super(message);
    }

    public JustBadRequestAPIException(String message, String localizedMessage) {
        super(message, localizedMessage);
    }

    public JustBadRequestAPIException(String message, Throwable cause) {
        super(message, cause);
    }

    public JustBadRequestAPIException(Throwable cause) {
        super(cause);
    }

    public JustBadRequestAPIException(String message, Throwable cause,
                                      boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public int getHttpStatus() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getCode() {
        return INTERNAL_CODE_PREFIX + HttpStatus.BAD_REQUEST.value();
    }
}
