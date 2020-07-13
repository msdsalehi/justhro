package io.justhro.core.exception;

public interface JustAPIExceptionDetailsProvider {

    int getStatus();

    String getCode();

    String getDetail();

    Object[] getDetailArgs();
}
