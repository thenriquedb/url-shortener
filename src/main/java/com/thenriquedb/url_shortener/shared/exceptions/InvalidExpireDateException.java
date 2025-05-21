package com.thenriquedb.url_shortener.shared.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidExpireDateException extends BusinessException {
    public InvalidExpireDateException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }


}
