package io.justhro.client.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import io.justhro.core.exception.JustAPIException;
import io.justhro.core.exception.JustErrorDecoderException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Map;

public class JustErrorDecoder implements ErrorDecoder {

    private final static Logger LOGGER = LoggerFactory.getLogger(JustErrorDecoder.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        try (InputStream is = response.body().asInputStream(); StringWriter writer = new StringWriter()) {
            IOUtils.copy(is, writer, "utf-8");
            String responseString = writer.toString();
            Map errorFields = objectMapper.readValue(responseString, Map.class);
            String justExceptionClassName = errorFields.get("exceptionClassName").toString();
            Class<?> clazz = Class.forName(justExceptionClassName);
            JustAPIException justAPIException = (JustAPIException) objectMapper.readValue(responseString, clazz);
            setServiceKey(methodKey, justAPIException);
            return justAPIException;
        } catch (Exception ex) {
            LOGGER.error("Error while trying to make JustAPIException. for [" +
                    methodKey + "] service. received response: " + response, ex);
            throw new JustErrorDecoderException(ex.getMessage(), ex, response.status());
        }
    }

    private void setServiceKey(String methodKey, JustAPIException justAPIException) {
        try {
            Field apiMessageField = JustAPIException.class.getDeclaredField("serviceKey");
            apiMessageField.setAccessible(true);
            apiMessageField.set(justAPIException, methodKey);
        } catch (Exception ex) {
            LOGGER.error("Error while trying to set apiMessage field of JustAPIException with concrete type: [" +
                    justAPIException.getExceptionClassName() + "]", ex);
        }
    }
}
