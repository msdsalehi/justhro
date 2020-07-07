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
package io.justhro.service.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.justhro.core.exception.JustAPIException;
import io.justhro.core.exception.JustAccessDeniedAPIException;
import io.justhro.core.exception.JustBadRequestAPIException;
import io.justhro.core.exception.JustUnknownAPIException;
import io.justhro.core.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;

@ControllerAdvice
public class JustAdvice {

    private final static Logger LOGGER = LoggerFactory.getLogger(JustAdvice.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();
    public static final UrlPathHelper URL_PATH_HELPER = new UrlPathHelper();

    @Autowired
    private MessageSource errorMessages;

    private void updateAPILocalizedMessage(JustAPIException instance) {
        try {
            String apiMessage = errorMessages.getMessage(instance.getClass().getName(),
                    instance.getApiMessageArgs(), instance.getApiMessage(), LocaleContextHolder.getLocale());
            ReflectionUtil.setFieldValue(instance, apiMessage, "apiMessage");
        } catch (Exception ex) {
            LOGGER.warn("Exception occurred while trying to set apiMessage to : ["
                    + ex.getClass().getSimpleName() + "].", ex);
        }
    }

    private ResponseEntity<JustAPIException> prepareInternalServerException(
            Throwable ex, JustAPIException instance, HttpStatus status) {
        updateAPILocalizedMessage(instance);
        try {
            instance.addCause(MAPPER.writeValueAsString(ex));
        } catch (JsonProcessingException e) {
            LOGGER.warn("Exception occurred while trying to serialize " + ex.getClass().getSimpleName()
                    + " for setting in JustAPIException cause property.", e);
        }
        return new ResponseEntity<>(instance, status);
    }

    private void setPath(HttpServletRequest req, JustAPIException justAPIException) {
        try {
            ReflectionUtil.setFieldValue(justAPIException, URL_PATH_HELPER.getPathWithinApplication(req), "path");
        } catch (Exception ex) {
            LOGGER.warn("Exception occurred while trying to set apiMessage to : ["
                    + ex.getClass().getSimpleName() + "].", ex);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<JustAPIException> handleMethodArgumentNotValidException(
            HttpServletRequest req, MethodArgumentNotValidException ex) {
        JustBadRequestAPIException justBadRequestAPIException = new JustBadRequestAPIException(
                "MethodArgumentNotValidException : " + ex.getMessage());
        setPath(req, justBadRequestAPIException);
        return prepareInternalServerException(ex, justBadRequestAPIException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseEntity<JustAPIException> handleHttpMessageNotReadableException(
            HttpServletRequest req, HttpMessageNotReadableException ex) {
        JustBadRequestAPIException justBadRequestAPIException = new JustBadRequestAPIException(
                "HttpMessageNotReadableException : " + ex.getMessage());
        setPath(req, justBadRequestAPIException);
        return prepareInternalServerException(ex, justBadRequestAPIException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServletException.class)
    @ResponseBody
    public ResponseEntity<JustAPIException> handleServletRequestBindingException(
            HttpServletRequest req, HttpMessageNotReadableException ex) {
        JustBadRequestAPIException justBadRequestAPIException = new JustBadRequestAPIException(
                "HttpMessageNotReadableException : " + ex.getMessage());
        setPath(req, justBadRequestAPIException);
        return prepareInternalServerException(ex, justBadRequestAPIException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public ResponseEntity<JustAPIException> handleAccessDeniedException(
            HttpServletRequest req, AccessDeniedException ex) {
        JustAccessDeniedAPIException justAccessDeniedAPIException = new JustAccessDeniedAPIException(
                "AccessDeniedException : " + ex.getMessage());
        setPath(req, justAccessDeniedAPIException);
        return prepareInternalServerException(ex, justAccessDeniedAPIException, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JustAPIException.class)
    @ResponseBody
    public ResponseEntity<JustAPIException> handleJustAPIException(
            HttpServletRequest req, JustAPIException ex) {
        updateAPILocalizedMessage(ex);
        setPath(req, ex);
        return new ResponseEntity<>(ex, HttpStatus.valueOf(ex.getHttpStatus()));
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<JustAPIException> handleThrowable(HttpServletRequest req, Throwable ex) {
        JustUnknownAPIException justUnknownAPIException = new JustUnknownAPIException(
                ex.getClass().getSimpleName() + " : " + ex.getMessage());
        setPath(req, justUnknownAPIException);
        ResponseEntity<JustAPIException> internalServerException = prepareInternalServerException(ex,
                justUnknownAPIException, HttpStatus.INTERNAL_SERVER_ERROR);
        return internalServerException;
    }
}
