package org.nikolait.assigment.ewallet.exception.handler;

import jakarta.validation.ValidationException;
import org.nikolait.assigment.ewallet.exception.InsufficientFundsException;
import org.nikolait.assigment.ewallet.exception.PageSizeLimitException;
import org.nikolait.assigment.ewallet.exception.RateLimitExceededException;
import org.nikolait.assigment.ewallet.exception.WalletNotFoundException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.TransactionTimedOutException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(BindException.class)
    public ErrorResponse handleBindExceptions(BindException ex) {
        return ErrorResponse.builder(ex, ProblemDetail.forStatus(HttpStatus.BAD_REQUEST))
                .type(URI.create(ex.getClass().getSimpleName()))
                .property("errors", extractFieldErrors(ex))
                .build();
    }

    @ExceptionHandler(ValidationException.class)
    public ErrorResponse handleValidationException(ValidationException ex) {
        return ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST, ex.getMessage())
                .type(URI.create(ex.getClass().getSimpleName()))
                .build();
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ErrorResponse handlePropertyReferenceException(PropertyReferenceException ex) {
        return ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST, ex.getMessage())
                .type(URI.create(ex.getClass().getSimpleName()))
                .build();
    }

    @ExceptionHandler(PageSizeLimitException.class)
    public ErrorResponse handlePageSizeLimitException(PageSizeLimitException ex) {
        return ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST, ex.getMessage())
                .type(URI.create(ex.getClass().getSimpleName()))
                .build();
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    public ErrorResponse handleConversionException(HttpMessageConversionException ex) {
        return ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST, ex.getMessage())
                .type(URI.create(ex.getClass().getSimpleName()))
                .build();
    }

    @ExceptionHandler(HttpMediaTypeException.class)
    public ErrorResponse handleMediaTypeException(HttpMediaTypeException ex) {
        return ErrorResponse.builder(ex, ex.getStatusCode(), ex.getMessage())
                .type(URI.create(ex.getClass().getSimpleName()))
                .build();
    }

    @ExceptionHandler(CannotGetJdbcConnectionException.class)
    public ErrorResponse handleCannotGetJdbcConnectionException(CannotGetJdbcConnectionException ex) {
        return ErrorResponse.builder(ex, HttpStatus.TOO_MANY_REQUESTS, ex.getMessage())
                .type(URI.create(ex.getClass().getSimpleName()))
                .build();
    }

    @ExceptionHandler(CannotCreateTransactionException.class)
    public ErrorResponse handleCannotGetJdbcConnectionException(CannotCreateTransactionException ex) {
        return ErrorResponse.builder(ex, HttpStatus.TOO_MANY_REQUESTS, ex.getMessage())
                .type(URI.create(ex.getClass().getSimpleName()))
                .build();
    }

    @ExceptionHandler(TransactionTimedOutException.class)
    public ErrorResponse handleTransactionTimedOutException(TransactionTimedOutException ex) {
        return ErrorResponse.builder(ex, HttpStatus.TOO_MANY_REQUESTS, ex.getMessage())
                .type(URI.create(ex.getClass().getSimpleName()))
                .build();
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ErrorResponse handleRateLimitExceededException(RateLimitExceededException ex) {
        return ErrorResponse.builder(ex, HttpStatus.TOO_MANY_REQUESTS, ex.getMessage())
                .type(URI.create(ex.getClass().getSimpleName()))
                .build();
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ErrorResponse handleInsufficientFundsException(InsufficientFundsException ex) {
        return ErrorResponse.builder(ex, HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage())
                .type(URI.create(ex.getClass().getSimpleName()))
                .build();
    }

    @ExceptionHandler(WalletNotFoundException.class)
    public ErrorResponse handleWalletNotFoundException(WalletNotFoundException ex) {
        return ErrorResponse.builder(ex, HttpStatus.NOT_FOUND, ex.getMessage())
                .type(URI.create(ex.getClass().getSimpleName()))
                .build();
    }

    private Map<String, String> extractFieldErrors(BindException ex) {
        return ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toUnmodifiableMap(
                        FieldError::getField,
                        error -> error.getDefaultMessage() != null
                                ? error.getDefaultMessage()
                                : ""
                ));
    }

}
