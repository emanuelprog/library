package com.challenge.library.util;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class ResponseUtil {

    public static ResponseEntity<Object> generateResponse(Object obj, HttpStatus status, String... message) {
        Map<String, Object> body = new HashMap<>();

        body.put("obj", obj);
        body.put("status", status.value());
        body.put("msg", (message != null && message.length > 0) ? message[0] : null);

        return new ResponseEntity<>(body, status);
    }

    public static ResponseEntity<Object> generateResponse(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();

        body.put("status", status.value());
        body.put("msg", message);

        return new ResponseEntity<>(body, status);
    }
}