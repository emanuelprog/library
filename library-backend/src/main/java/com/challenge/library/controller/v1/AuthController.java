package com.challenge.library.controller.v1;

import com.challenge.library.dto.request.UserRequestDTO;
import com.challenge.library.service.AuthService;
import com.challenge.library.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register a new user and return JWT token")
    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody UserRequestDTO request) {
        return ResponseUtil.generateResponse(authService.register(request), HttpStatus.CREATED, "User registered successfully");
    }

    @Operation(summary = "Login with username and password, returns JWT token")
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserRequestDTO request) {
        return ResponseUtil.generateResponse(authService.login(request), HttpStatus.OK, "Login successful");
    }
}