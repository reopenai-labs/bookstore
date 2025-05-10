package com.reopenai.bookstore.component.webflux;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.reopenai.bookstore.bean.ApiResponse;
import com.reopenai.bookstore.bean.ErrorCode;
import com.reopenai.bookstore.component.exception.BusinessException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MimeType;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.reactive.resource.NoResourceFoundException;
import org.springframework.web.server.*;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.StringJoiner;

/**
 * Created by Allen Huang
 */
@Slf4j
@RestControllerAdvice
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ApiResponse<Void> methodArgumentTypeMismatchException(ServerWebExchange exchange, MethodArgumentTypeMismatchException e) {
        Locale locale = exchange.getLocaleContext().getLocale();
        return ApiResponse.failure(locale, ErrorCode.INVALID_PARAMETER, String.format("%s=%s", e.getName(), e.getValue()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ApiResponse<Void> validationException(ServerWebExchange exchange, ValidationException e) {
        return ApiResponse.failureWithMessage(ErrorCode.FAILED_PARAMETER_CHECK, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResponse<Void> constraintViolationException(ServerWebExchange exchange, ConstraintViolationException e) {
        StringJoiner joiner = new StringJoiner(";");
        for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
            joiner.add(constraintViolation.getMessage());
        }
        return ApiResponse.failureWithMessage(ErrorCode.FAILED_PARAMETER_CHECK, joiner.toString());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Void> methodArgumentNotValidException(ServerWebExchange exchange, MethodArgumentNotValidException error) {
        BindingResult result = error.getBindingResult();
        if (result.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            result.getAllErrors().forEach(err -> sb.append(err.getDefaultMessage()).append(";"));
            return ApiResponse.failureWithMessage(ErrorCode.FAILED_PARAMETER_CHECK, sb.toString());
        }
        return ApiResponse.failureWithMessage(ErrorCode.FAILED_PARAMETER_CHECK, error.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoResourceFoundException.class)
    public ApiResponse<Void> onNoResourceFoundException(ServerWebExchange exchange, ServerHttpRequest request) {
        return ApiResponse.failureWithMessage(ErrorCode.NOT_FOUND, String.format("%s not found", request.getPath()));
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(MethodNotAllowedException.class)
    public ApiResponse<Void> methodNotAllowedException(ServerWebExchange exchange, MethodNotAllowedException e) {
        Locale locale = exchange.getLocaleContext().getLocale();
        return ApiResponse.failure(locale, ErrorCode.METHOD_NOT_ALLOWED, e.getHttpMethod());
    }


    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(UnsupportedMediaTypeStatusException.class)
    public ApiResponse<Void> unsupportedMediaTypeStatusException(ServerWebExchange exchange, UnsupportedMediaTypeStatusException e) {
        Locale locale = exchange.getLocaleContext().getLocale();
        String contentType = Optional.ofNullable(e.getContentType())
                .map(MimeType::toString)
                .orElse("");
        return ApiResponse.failure(locale, ErrorCode.MEDIA_TYPE_NOT_ALLOWED, contentType);
    }

    // 无法接收
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(NotAcceptableStatusException.class)
    public ApiResponse<Void> httpMediaTypeNotAcceptableException(ServerWebExchange exchange, NotAcceptableStatusException e) {
        Locale locale = exchange.getLocaleContext().getLocale();
        return ApiResponse.failure(locale, ErrorCode.NOT_ACCEPTABLE, " ");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingRequestValueException.class)
    public ApiResponse<Void> missingRequestValueException(ServerWebExchange exchange, MissingRequestValueException e) {
        Locale locale = exchange.getLocaleContext().getLocale();
        Object[] args = new Object[]{e.getName(), e.getType().getSimpleName()};
        return ApiResponse.failure(locale, ErrorCode.MISSING_REQUEST_PARAMETER, args);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServerWebInputException.class)
    public ApiResponse<Void> badRequest(ServerWebExchange exchange, ServerWebInputException e) {
        Locale locale = exchange.getLocaleContext().getLocale();
        Throwable cause = e.getCause();
        if (cause instanceof TypeMismatchException err) {
            String propertyName = err.getPropertyName();
            String values = Optional.ofNullable(err.getValue()).map(Object::toString).orElse("");
            String typeName = Optional.ofNullable(err.getRequiredType()).map(Class::getSimpleName).orElse("");
            return ApiResponse.failure(locale, ErrorCode.PARAM_TYPE_MISMATCH, propertyName, values, typeName);
        }
        return ApiResponse.failure(locale, ErrorCode.MISSING_REQUEST_PARAMETER);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ApiResponse<Void> handlerMethodValidationException(ServerWebExchange exchange, HandlerMethodValidationException e) {
        Locale locale = exchange.getLocaleContext().getLocale();
        StringJoiner builder = new StringJoiner(";");
        for (ParameterValidationResult result : e.getParameterValidationResults()) {
            for (MessageSourceResolvable resolvableError : result.getResolvableErrors()) {
                builder.add(resolvableError.getDefaultMessage());
            }
        }
        return ApiResponse.failure(locale, ErrorCode.FAILED_PARAMETER_CHECK, builder.toString());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WebExchangeBindException.class)
    public ApiResponse<Void> webExchangeBindException(ServerWebExchange exchange, WebExchangeBindException e) {
        Locale locale = exchange.getLocaleContext().getLocale();
        StringJoiner builder = new StringJoiner(";");
        for (ObjectError allError : e.getAllErrors()) {
            builder.add(allError.getDefaultMessage());
        }
        return ApiResponse.failure(locale, ErrorCode.FAILED_PARAMETER_CHECK, builder.toString());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResponse<Void> httpMessageNotReadableException(ServerWebExchange exchange, HttpMessageNotReadableException e) {
        Locale locale = exchange.getLocaleContext().getLocale();
        Throwable cause = e.getCause();
        if (cause instanceof InvalidFormatException err) {
            for (int i = 0; i < err.getPath().size(); i++) {
                JsonMappingException.Reference reference = err.getPath().get(i);
                String fieldName = reference.getFieldName();
                if (StringUtils.hasText(fieldName)) {
                    String value = Optional.ofNullable(err.getValue())
                            .map(Object::toString)
                            .orElse("");
                    return ApiResponse.failure(locale, ErrorCode.INVALID_PARAMETER, String.format("%s:%s", fieldName, value));
                }
            }
        } else if (cause instanceof JsonMappingException err) {
            StringJoiner builder = new StringJoiner(".");
            for (JsonMappingException.Reference reference : err.getPath()) {
                builder.add(reference.getFieldName());
            }
            String fieldName = builder.toString();
            if (StringUtils.hasText(fieldName)) {
                return ApiResponse.failure(locale, ErrorCode.INVALID_PARAMETER, fieldName);
            }
        } else if (cause instanceof ValidationException err) {
            return validationException(exchange, err);
        }
        return ApiResponse.failure(locale, ErrorCode.INVALID_PARAMETER, "UNKNOWN");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BusinessException.class)
    public ApiResponse<Object> businessHandler(ServerWebExchange exchange, BusinessException error) {
        ErrorCode errorCode = error.getErrorCode();
        return ApiResponse.failure(exchange.getLocaleContext().getLocale(), errorCode, error.getArgs());
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> ioException(ServerWebExchange exchange, IOException e) {
        Locale locale = exchange.getLocaleContext().getLocale();
        log.error("[Exception]I/O Exception", e);
        return ApiResponse.failure(locale, ErrorCode.SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> exceptionHandler(ServerWebExchange exchange, Exception e) {
        Locale locale = exchange.getLocaleContext().getLocale();
        log.error("caught unhandled exception.", e);
        return ApiResponse.failure(locale, ErrorCode.SERVER_ERROR);
    }


}
