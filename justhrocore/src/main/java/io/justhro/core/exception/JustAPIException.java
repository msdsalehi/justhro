package io.justhro.core.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(value = {"cause", "stackTrace", "suppressed", "apiMessageArgs"},
        ignoreUnknown = true)
public abstract class JustAPIException extends RuntimeException {

    static final String INTERNAL_CODE_PREFIX = "*-";
    private Object[] localizedMessageArgs;
    protected String serviceKey;
    private List<String> causes;

    public JustAPIException() {
    }

    public JustAPIException(String message) {
        super(message);
    }

    public JustAPIException(String message, String localizedMessage) {
        super(message);
    }

    public JustAPIException(String message, Throwable cause) {
        super(message, cause);
    }

    public JustAPIException(Throwable cause) {
        super(cause);
    }

    public JustAPIException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public abstract int getHttpStatus();

    public abstract String getCode();

    public Object[] getLocalizedMessageArgs() {
        return localizedMessageArgs;
    }

    public void setLocalizedMessageArgs(Object... localizedMessageArgs) {
        this.localizedMessageArgs = localizedMessageArgs;
    }

    public String getServiceKey() {
        return serviceKey;
    }

    public List<String> getCauses() {
        return causes;
    }

    public void setCauses(List<String> causes) {
        this.causes = causes;
    }

    public void addCause(String cause) {
        if (this.causes == null) {
            this.causes = new ArrayList<>();
        }
        this.causes.add(cause);
    }

    public final String getExceptionClassName() {
        return this.getClass().getName();
    }
}
