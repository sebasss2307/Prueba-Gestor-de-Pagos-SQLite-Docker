package com.prueba.backend.service;

import com.prueba.backend.dto.LoginRequest;
import com.prueba.backend.dto.LoginResponse;
import com.prueba.backend.model.User;
import com.prueba.backend.repository.UserRepository;
import com.prueba.backend.util.ValidationUtil;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final Map<String, String> activeTokens = new ConcurrentHashMap<>();

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        // Validar formato del username con regex
        if (!ValidationUtil.isValidUsername(username)) {
            String errorMessage = ValidationUtil.getValidationErrorMessage(username);
            throw new RuntimeException("Formato de usuario inválido: " + errorMessage);
        }

        // Buscar usuario en la base de datos
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Credenciales incorrectas"));

        // Generar hash SHA-256 y comparar
        String generatedHash = ValidationUtil.generateLoginHash(username, password);
        if (!generatedHash.equals(user.getPasswordHash())) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        // Generar token de sesión
        String token = UUID.randomUUID().toString();
        activeTokens.put(token, username);

        return new LoginResponse(
                token,
                username,
                user.getNombres(),
                user.getPrimerApellido(),
                user.getSegundoApellido(),
                user.getNombreCompleto(),
                user.getFechaNacimiento().toString()
        );
    }

    public boolean validateUsername(String username) {
        return ValidationUtil.isValidUsername(username);
    }

    public String getValidationError(String username) {
        return ValidationUtil.getValidationErrorMessage(username);
    }

    public String validateToken(String token) {
        return activeTokens.get(token);
    }

    public void logout(String token) {
        activeTokens.remove(token);
    }
}
