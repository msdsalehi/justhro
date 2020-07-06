package io.justhro.core.exception;

import org.springframework.http.HttpStatus;

public class JustAccessDeniedAPIException extends JustAPIException {

    public JustAccessDeniedAPIException() {
    }

    public JustAccessDeniedAPIException(String message) {
        super(message);
    }

    public JustAccessDeniedAPIException(String message, String localizedMessage) {
        super(message, localizedMessage);
    }

    public JustAccessDeniedAPIException(String message, Throwable cause) {
        super(message, cause);
    }

    public JustAccessDeniedAPIException(Throwable cause) {
        super(cause);
    }

    public JustAccessDeniedAPIException(String message, Throwable cause,
                                        boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public int getHttpStatus() {
        return HttpStatus.UNAUTHORIZED.value();
    }

    @Override
    public String getCode() {
        return INTERNAL_CODE_PREFIX + HttpStatus.UNAUTHORIZED.value();
    }
}
