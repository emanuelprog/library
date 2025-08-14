package com.challenge.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex) {
        HashMap<String, Object> map = new HashMap<>();

        map.put("code", HttpStatus.NOT_FOUND.value());
        map.put("message", ex.getMessage());

        return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException ex) {
        HashMap<String, Object> map = new HashMap<>();

        map.put("code", HttpStatus.BAD_REQUEST.value());
        map.put("message", ex.getMessage());

        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<Object> handleAlreadyExistException(AlreadyExistException ex) {
        HashMap<String, Object> map = new HashMap<>();

        map.put("code", HttpStatus.CONFLICT.value());
        map.put("message", ex.getMessage());

        return new ResponseEntity<>(map, HttpStatus.CONFLICT);
    }
}