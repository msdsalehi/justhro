package io.justhro.core.util;

import io.justhro.core.exception.JustAPIException;

import java.lang.reflect.Field;

public final class ReflectionUtil {

    public static void setFieldValue(JustAPIException instance, String localizedMessage, String apiMessage)
            throws NoSuchFieldException, IllegalAccessException {
        Field apiMessageField = JustAPIException.class.getDeclaredField(apiMessage);
        apiMessageField.setAccessible(true);
        apiMessageField.set(instance, localizedMessage);
    }
}
