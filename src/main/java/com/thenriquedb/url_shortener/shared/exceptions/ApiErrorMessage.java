package com.thenriquedb.url_shortener.shared.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ApiErrorMessage  {
    private HttpStatus status;
    private String message;
    private Map<String, String> errors;

    public ApiErrorMessage(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        this.errors = new HashMap<>();
    }
}