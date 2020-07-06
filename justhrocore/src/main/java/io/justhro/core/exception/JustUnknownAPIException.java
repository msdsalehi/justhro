package io.justhro.core.exception;

import java.util.List;

public class JustUnknownAPIException extends JustAPIException {

    private String apiMessage;
    protected String serviceKey;
    private List<String> causes;
    private int httpStatus;
    private String code;

    public JustUnknownAPIException(String apiMessage) {
        this.apiMessage = apiMessage;
    }

    public JustUnknownAPIException(String message, String apiMessage) {
        super(message);
        this.apiMessage = apiMessage;
    }

    public String getApiMessage() {
        return apiMessage;
    }

    public void setApiMessage(String apiMessage) {
        this.apiMessage = apiMessage;
    }

    public String getServiceKey() {
        return serviceKey;
    }

    public void setServiceKey(String serviceKey) {
        this.serviceKey = serviceKey;
    }

    public List<String> getCauses() {
        return causes;
    }

    public void setCauses(List<String> causes) {
        this.causes = causes;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
