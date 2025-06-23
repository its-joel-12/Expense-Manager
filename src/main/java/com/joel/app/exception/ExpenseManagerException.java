package com.joel.app.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class ExpenseManagerException {
    private final Integer httpCode;
    private final HttpStatus httpStatus;
    private final String message;
    private final String description;
}
