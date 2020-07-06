package io.justhro.service.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.justhro.core.exception.JustAPIException;
import io.justhro.core.exception.JustAccessDeniedAPIException;
import io.justhro.core.exception.JustBadRequestAPIException;
import io.justhro.core.exception.JustUnknownAPIException;
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;

@ControllerAdvice
public class JustAdvice {

    private final static Logger LOGGER = LoggerFactory.getLogger(JustAdvice.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private MessageSource errorMessages;

    private void updateAPILocalizedMessage(JustAPIException instance) {
        try {
            String localizedMessage = errorMessages.getMessage(instance.getClass().getName(),
                    instance.getLocalizedMessageArgs(), instance.getLocalizedMessage(), LocaleContextHolder.getLocale());
            Field detailMessageField = JustAPIException.class.getDeclaredField("detailMessage");
            detailMessageField.setAccessible(true);
            detailMessageField.set(instance, localizedMessage);
        } catch (Exception ex) {
            LOGGER.warn("Exception occurred while trying to set localizedMessage to : ["
                    + ex.getClass().getSimpleName() + "].", ex);
        }
    }

    private ResponseEntity<JustAPIException> prepareInternalAPIException(
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<JustAPIException> handleMethodArgumentNotValidException(
            HttpServletRequest req, MethodArgumentNotValidException ex) {
        JustBadRequestAPIException justBadRequestAPIException = new JustBadRequestAPIException(
                "MethodArgumentNotValidException : " + ex.getMessage());
        return prepareInternalAPIException(ex, justBadRequestAPIException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseEntity<JustAPIException> handleHttpMessageNotReadableException(
            HttpServletRequest req, HttpMessageNotReadableException ex) {
        JustBadRequestAPIException justBadRequestAPIException = new JustBadRequestAPIException(
                "HttpMessageNotReadableException : " + ex.getMessage());
        return prepareInternalAPIException(ex, justBadRequestAPIException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServletException.class)
    @ResponseBody
    public ResponseEntity<JustAPIException> handleServletRequestBindingException(
            HttpServletRequest req, HttpMessageNotReadableException ex) {
        JustBadRequestAPIException justBadRequestAPIException = new JustBadRequestAPIException(
                "HttpMessageNotReadableException : " + ex.getMessage());
        return prepareInternalAPIException(ex, justBadRequestAPIException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public ResponseEntity<JustAPIException> handleAccessDeniedException(
            HttpServletRequest req, AccessDeniedException ex) {
        JustAccessDeniedAPIException justAccessDeniedAPIException = new JustAccessDeniedAPIException(
                "AccessDeniedException : " + ex.getMessage());
        return prepareInternalAPIException(ex, justAccessDeniedAPIException, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JustAPIException.class)
    @ResponseBody
    public ResponseEntity<JustAPIException> handleJustAPIException(
            HttpServletRequest req, JustAPIException ex) {
        updateAPILocalizedMessage(ex);
        return new ResponseEntity<>(ex, HttpStatus.valueOf(ex.getHttpStatus()));
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<JustAPIException> handleThrowable(Throwable ex) {
        JustUnknownAPIException justUnknownAPIException = new JustUnknownAPIException(
                ex.getClass().getSimpleName() + " : " + ex.getMessage());
        return prepareInternalAPIException(ex, justUnknownAPIException, HttpStatus.UNAUTHORIZED);
    }
}
