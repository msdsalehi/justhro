/**
 * Copyright (C) 2020  Masoud Salehi Alamdari
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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
            return (JustAPIException) objectMapper.readValue(responseString, clazz);
        } catch (Exception ex) {
            LOGGER.error("Error while trying to make JustAPIException. for [" +
                    methodKey + "] service. received response: " + response, ex);
            throw new JustErrorDecoderException(ex.getMessage(), ex, response.status());
        }
    }
}
