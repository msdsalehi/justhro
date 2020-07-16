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
package io.justhro.service.advice;

import io.justhro.core.exception.*;
import io.justhro.core.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Order(1)
public class JustAdvice {

    private static final UrlPathHelper URL_PATH_HELPER = new UrlPathHelper();
    private static final Logger LOGGER = LoggerFactory.getLogger(JustAdvice.class);
    private static final MediaType DEFAULT_MEDIA_TYPE = MediaType.APPLICATION_PROBLEM_JSON;
    private static final String DEFAULT_TYPE = "about:blank";
    private static final String DEFAULT_TITLE = "Error";
    private static final String DETAIL_PROP_NAME = "detail";
    private static final String TYPE_PROP_NAME = "type";
    private static final String TITLE_PROP_NAME = "title";
    private static final String INSTANCE_PROP_NAME = "instance";

    @Autowired
    private MessageSource justhroDetails;

    @Autowired
    private MessageSource justhroTitles;

    @Autowired
    private MessageSource justhroTypes;

    private void setDetail(JustAPIExceptionDetailsProvider instance, String defaultDetail,
                           Object[] detailArgs) {
        try {
            String detail = justhroDetails.getMessage(instance.getClass().getName(),
                    detailArgs, defaultDetail, LocaleContextHolder.getLocale());
            ReflectionUtil.setFieldValue(instance, detail, DETAIL_PROP_NAME);
        } catch (Exception ex) {
            LOGGER.warn("Exception occurred while trying to set detail to : '{}'.",
                    ex.getClass().getSimpleName());
            LOGGER.warn(ex.getMessage(), ex);
        }
    }

    private void setType(JustAPIExceptionDetailsProvider instance) {
        try {
            String type = justhroTypes.getMessage(instance.getClass().getName(),
                    null, DEFAULT_TYPE, LocaleContextHolder.getLocale());
            ReflectionUtil.setFieldValue(instance, type, TYPE_PROP_NAME);
        } catch (Exception ex) {
            LOGGER.warn("Exception occurred while trying to set type to : '{}'.",
                    ex.getClass().getSimpleName());
            LOGGER.warn(ex.getMessage(), ex);
        }
    }

    private void setTitle(JustAPIExceptionDetailsProvider instance) {
        try {
            String defaultTitle = DEFAULT_TITLE;
            try {
                HttpStatus httpStatus = HttpStatus.valueOf(instance.getStatus());
                defaultTitle = httpStatus.getReasonPhrase();
            } catch (IllegalArgumentException iae) {
                LOGGER.warn("Default title not found for status : '{}'. Setting to : '{}'", instance.getStatus(),
                        DEFAULT_TITLE);
            }
            String detail = justhroTitles.getMessage(instance.getClass().getName(),
                    null, defaultTitle, LocaleContextHolder.getLocale());
            ReflectionUtil.setFieldValue(instance, detail, TITLE_PROP_NAME);
        } catch (Exception ex) {
            LOGGER.warn("Exception occurred while trying to set title to : '{}'.",
                    ex.getClass().getSimpleName());
            LOGGER.warn(ex.getMessage(), ex);
        }
    }

    private void setInstance(HttpServletRequest req, JustAPIExceptionDetailsProvider justAPIException) {
        try {
            ReflectionUtil.setFieldValue(justAPIException, URL_PATH_HELPER.getPathWithinApplication(req),
                    INSTANCE_PROP_NAME);
        } catch (Exception ex) {
            LOGGER.warn("Exception occurred while trying to set instance to : '{}'.",
                    ex.getClass().getSimpleName());
            LOGGER.warn(ex.getMessage(), ex);
        }
    }

    private void setGeneralExceptionAsCause(Throwable ex, JustAPIException instance) {
        try {
            instance.addCause(ex.getMessage());
        } catch (Exception e) {
            LOGGER.warn("Exception occurred while trying to serialize '{}' for setting in JustAPIException " +
                    "cause property.", ex.getClass().getSimpleName());
            LOGGER.warn(e.getMessage(), e);
        }
    }

    private void updateProperties(HttpServletRequest req, JustAPIExceptionDetailsProvider ex) {
        setDetail(ex, ex.getDetail(), ex.getDetailArgs());
        setInstance(req, ex);
        setType(ex);
        setTitle(ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<JustAPIException> handleMethodArgumentNotValidException(
            HttpServletRequest req, MethodArgumentNotValidException ex) {
        LOGGER.error(ex.getMessage(), ex);
        JustBadRequestAPIException justBadRequestAPIException = new JustBadRequestAPIException(
                "MethodArgumentNotValidException : " + ex.getMessage(), null);
        updateProperties(req, justBadRequestAPIException);
        setGeneralExceptionAsCause(ex, justBadRequestAPIException);
        return ResponseEntity.status(justBadRequestAPIException.getStatus())
                .contentType(DEFAULT_MEDIA_TYPE)
                .body(justBadRequestAPIException);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseEntity<JustAPIException> handleHttpMessageNotReadableException(
            HttpServletRequest req, HttpMessageNotReadableException ex) {
        LOGGER.error(ex.getMessage(), ex);
        JustBadRequestAPIException justBadRequestAPIException = new JustBadRequestAPIException(
                "HttpMessageNotReadableException : " + ex.getMessage(), null);
        updateProperties(req, justBadRequestAPIException);
        setGeneralExceptionAsCause(ex, justBadRequestAPIException);
        return ResponseEntity.status(justBadRequestAPIException.getStatus())
                .contentType(DEFAULT_MEDIA_TYPE)
                .body(justBadRequestAPIException);
    }

    @ExceptionHandler(ServletException.class)
    @ResponseBody
    public ResponseEntity<JustAPIException> handleServletRequestBindingException(
            HttpServletRequest req, ServletException ex) {
        LOGGER.error(ex.getMessage(), ex);
        JustBadRequestAPIException justBadRequestAPIException = new JustBadRequestAPIException(
                "ServletException : " + ex.getMessage(), null);
        updateProperties(req, justBadRequestAPIException);
        setGeneralExceptionAsCause(ex, justBadRequestAPIException);
        return ResponseEntity.status(justBadRequestAPIException.getStatus())
                .contentType(DEFAULT_MEDIA_TYPE)
                .body(justBadRequestAPIException);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public ResponseEntity<JustAPIException> handleAccessDeniedException(
            HttpServletRequest req, AccessDeniedException ex) {
        LOGGER.error(ex.getMessage(), ex);
        JustAccessDeniedAPIException justAccessDeniedAPIException = new JustAccessDeniedAPIException(
                "AccessDeniedException : " + ex.getMessage(), null);
        updateProperties(req, justAccessDeniedAPIException);
        setGeneralExceptionAsCause(ex, justAccessDeniedAPIException);
        return ResponseEntity.status(justAccessDeniedAPIException.getStatus())
                .contentType(DEFAULT_MEDIA_TYPE)
                .body(justAccessDeniedAPIException);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public ResponseEntity<JustAPIException> handleHttpMethodNotSupportedException(
            HttpServletRequest req, HttpRequestMethodNotSupportedException ex) {
        LOGGER.error(ex.getMessage(), ex);
        JustHttpMethodNotSupportedAPIException methodNotSupportedException = new JustHttpMethodNotSupportedAPIException(
                "AccessDeniedException : " + ex.getMessage(), null);
        updateProperties(req, methodNotSupportedException);
        setGeneralExceptionAsCause(ex, methodNotSupportedException);
        return ResponseEntity.status(methodNotSupportedException.getStatus())
                .contentType(DEFAULT_MEDIA_TYPE)
                .body(methodNotSupportedException);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    public ResponseEntity<JustAPIException> handleBadCredentialsException(
            HttpServletRequest req, BadCredentialsException ex) {
        LOGGER.error(ex.getMessage(), ex);
        JustBadCredentialsAPIException justBadCredentialsAPIException = new JustBadCredentialsAPIException(
                "AccessDeniedException : " + ex.getMessage(), null);
        updateProperties(req, justBadCredentialsAPIException);
        setGeneralExceptionAsCause(ex, justBadCredentialsAPIException);
        return ResponseEntity.status(justBadCredentialsAPIException.getStatus())
                .contentType(DEFAULT_MEDIA_TYPE)
                .body(justBadCredentialsAPIException);
    }

    @ExceptionHandler({NoHandlerFoundException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<JustAPIException> notFound(HttpServletRequest req, NoHandlerFoundException ex) {
        LOGGER.error(ex.getMessage(), ex);
        JustNotFoundAPIException justNotFoundAPIException = new JustNotFoundAPIException(
                "NoHandlerFoundException : " + ex.getMessage(), null);
        updateProperties(req, justNotFoundAPIException);
        setGeneralExceptionAsCause(ex, justNotFoundAPIException);
        return ResponseEntity.status(justNotFoundAPIException.getStatus())
                .contentType(DEFAULT_MEDIA_TYPE)
                .body(justNotFoundAPIException);
    }

    @ExceptionHandler({JustAPIException.class})
    @ResponseBody
    public ResponseEntity<JustAPIException> handleJustAPIException(HttpServletRequest req, JustAPIException ex) {
        LOGGER.error(ex.getMessage(), ex);
        updateProperties(req, ex);
        return ResponseEntity.status(ex.getStatus())
                .contentType(DEFAULT_MEDIA_TYPE)
                .body(ex);
    }

    @ExceptionHandler({JustAPICheckedException.class})
    @ResponseBody
    public ResponseEntity<JustAPICheckedException> handleJustAPICheckedException(
            HttpServletRequest req, JustAPICheckedException ex) {
        LOGGER.error(ex.getMessage(), ex);
        updateProperties(req, ex);
        return ResponseEntity.status(ex.getStatus())
                .contentType(DEFAULT_MEDIA_TYPE)
                .body(ex);
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<JustAPIException> handleThrowable(HttpServletRequest req, Throwable ex) {
        LOGGER.error(ex.getMessage(), ex);
        JustUnknownAPIException justUnknownAPIException = new JustUnknownAPIException(
                ex.getClass().getSimpleName() + " : " + ex.getMessage(), null);
        updateProperties(req, justUnknownAPIException);
        setGeneralExceptionAsCause(ex, justUnknownAPIException);
        return ResponseEntity.status(justUnknownAPIException.getStatus())
                .contentType(DEFAULT_MEDIA_TYPE)
                .body(justUnknownAPIException);
    }
}
