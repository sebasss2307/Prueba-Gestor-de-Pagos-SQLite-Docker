package com.prueba.backend.controller;

import com.prueba.backend.dto.LoginRequest;
import com.prueba.backend.dto.LoginResponse;
import com.prueba.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/validate-username/{username}")
    public ResponseEntity<?> validateUsername(@PathVariable String username) {
        boolean isValid = authService.validateUsername(username);
        
        if (isValid) {
            return ResponseEntity.ok(new ValidationResponse(true, "Username válido"));
        } else {
            String errorMessage = authService.getValidationError(username);
            return ResponseEntity.ok(new ValidationResponse(false, errorMessage));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                authService.logout(token);
            }
            return ResponseEntity.ok(new MessageResponse("Sesión cerrada correctamente"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al cerrar sesión"));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(new MessageResponse("OK"));
    }

    static class ErrorResponse {
        private String error;
        public ErrorResponse(String error) { this.error = error; }
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
    }

    static class ValidationResponse {
        private boolean valid;
        private String message;
        public ValidationResponse(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }
        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    static class MessageResponse {
        private String message;
        public MessageResponse(String message) { this.message = message; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}
