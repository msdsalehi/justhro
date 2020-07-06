package io.justhro.core.exception;

public class JustErrorDecoderException extends JustAPIException {

    private int httpStatus;

    public JustErrorDecoderException(String message, Throwable cause, int httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public JustErrorDecoderException(String message, int httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public JustErrorDecoderException(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public JustErrorDecoderException() {
    }

    @Override
    public int getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getCode() {
        return INTERNAL_CODE_PREFIX + httpStatus;
    }
}
