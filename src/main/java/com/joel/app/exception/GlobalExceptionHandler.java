package com.joel.app.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // RESOURCE NOT FOUND EXCEPTION
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExpenseManagerException> handleException(ResourceNotFoundException ex) {
        ExpenseManagerException error = new ExpenseManagerException(
                404,
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                "Required Resource Not Found, hence the operation was unsuccessful!!"
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // HttpMessageNotReadableException
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex){
        ExpenseManagerException error = new ExpenseManagerException(
                400,
                HttpStatus.BAD_REQUEST,
                "Please make sure you send data in proper format, also don't send an empty request body!!",
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // NoResourceFoundException
    @ExceptionHandler(value = NoResourceFoundException.class)
    public ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex){
        ExpenseManagerException error = new ExpenseManagerException(
                400,
                HttpStatus.BAD_REQUEST,
                "Check proper format of the api and its Http method!!",
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        ExpenseManagerException error = new ExpenseManagerException(
                405,
                HttpStatus.METHOD_NOT_ALLOWED,
                "HTTP method not supported for this endpoint!",
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.METHOD_NOT_ALLOWED);
    }

    // INVALID FIELD DATA
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        Map<String, String> res = new HashMap<>();

        ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors().forEach((er) -> {
            String fieldName = ((FieldError) er).getField();
            String message = er.getDefaultMessage();
            res.put(fieldName, message);
        });
        ExpenseManagerException error = new ExpenseManagerException(
                400,
                HttpStatus.BAD_REQUEST,
                "Invalid Inputs !!",
                res.toString()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // DataIntegrityViolationException
    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex){
        ExpenseManagerException error = new ExpenseManagerException(
                400,
                HttpStatus.BAD_REQUEST,
                "User already exists with the entered email !!",
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // MethodArgumentTypeMismatchException
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex){
        ExpenseManagerException error = new ExpenseManagerException(
                400,
                HttpStatus.BAD_REQUEST,
                "Please provide proper Integer ID input in the api url argument!!",
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    // UN IDENTIFIED EXCEPTION
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExpenseManagerException> handleUnknownException(Exception ex) {
        ExpenseManagerException error = new ExpenseManagerException(
                500,
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Un identified error | Please contact the Backend Developer!!... :D",
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
