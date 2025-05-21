package com.thenriquedb.url_shortener.shared.validation;

import com.thenriquedb.url_shortener.shared.exceptions.ApiErrorMessage;
import com.thenriquedb.url_shortener.shared.exceptions.BusinessException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.badRequest().body(new ApiErrorMessage(HttpStatus.BAD_REQUEST, "Validation error", errors));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorMessage> handleCustomExceptions(BusinessException exception) {
        ApiErrorMessage apiErrorMessage = new ApiErrorMessage(exception.getHttpStatus(), exception.getMessage());
        return ResponseEntity.status(apiErrorMessage.getStatus()).body(apiErrorMessage);
    }
}