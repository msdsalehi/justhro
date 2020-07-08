/**
 * Copyright (C) 2020  Masoud Salehi Alamdari
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package io.justhro.client.decoder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import io.justhro.core.exception.JustErrorDecoderException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.Map;

public class JustErrorDecoder implements ErrorDecoder {

    private final static Logger LOGGER = LoggerFactory.getLogger(JustErrorDecoder.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        String responseString = null;
        try {
            responseString = getResponseString(methodKey, response);
            String exceptionClassName = getExceptionClassName(methodKey, response, responseString);
            Class<?> clazz = Class.forName(exceptionClassName);
            Exception casted = clazz.asSubclass(Exception.class).cast(objectMapper.readValue(responseString, clazz));
            LOGGER.error("JustDecoder error received: {}", responseString);
            return casted;
        } catch (JustErrorDecoderException ex) {
            throw ex;
        } catch (Exception ex) {
            LOGGER.error("Error while trying to make JustAPIException. for '{}' service." +
                    "\n--received response: {}" +
                    "\n--raw response body: {}", methodKey, response, responseString);
            LOGGER.error(ex.getMessage(), ex);
            throw new JustErrorDecoderException(ex.getMessage(), ex, response.status());
        }
    }

    private String getExceptionClassName(String methodKey, Response response, String responseString)
            throws JsonProcessingException {
        Map errorFields = objectMapper.readValue(responseString, Map.class);
        Object exceptionClassName = errorFields.get("exceptionClassName");
        if (exceptionClassName == null || exceptionClassName.toString().isEmpty()) {
            LOGGER.error("Error 'exceptionClassName' property not found on received response for '{}' service." +
                    "\n--received response: {}" +
                    "\n--raw response body: {}", methodKey, response, responseString);
            throw new JustErrorDecoderException(response.status());
        }
        return exceptionClassName.toString();
    }

    private String getResponseString(String methodKey, Response response) {
        try (InputStream is = response.body().asInputStream(); StringWriter writer = new StringWriter()) {
            IOUtils.copy(is, writer, "utf-8");
            return writer.toString();
        } catch (Exception ex) {
            LOGGER.error("Error while trying read response body. for '{}' service." +
                    "\n--received response: {}", methodKey, response);
            LOGGER.error(ex.getMessage(), ex);
            throw new JustErrorDecoderException(ex.getMessage(), ex, response.status());
        }
    }
}
