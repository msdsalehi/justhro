package io.justhro.core.exception;

import org.springframework.http.HttpStatus;

public class JustForbidedenAPIException extends JustAPIException {

    public JustForbidedenAPIException() {
    }

    public JustForbidedenAPIException(String message) {
        super(message);
    }

    public JustForbidedenAPIException(String message, String localizedMessage) {
        super(message, localizedMessage);
    }

    public JustForbidedenAPIException(String message, Throwable cause) {
        super(message, cause);
    }

    public JustForbidedenAPIException(Throwable cause) {
        super(cause);
    }

    public JustForbidedenAPIException(String message, Throwable cause,
                                      boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public int getHttpStatus() {
        return HttpStatus.FORBIDDEN.value();
    }

    @Override
    public String getCode() {
        return INTERNAL_CODE_PREFIX + HttpStatus.FORBIDDEN.value();
    }
}
