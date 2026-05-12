package com.prueba.backend.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

public class ValidationUtil {
    
    // Patrón regex: UpperCamelCase, mínimo 15 caracteres, termina con 3 dígitos
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[A-Z][a-zA-Z]{11,}\\d{3}$");

    public static boolean isValidUsername(String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        return USERNAME_PATTERN.matcher(username).matches();
    }

    // Genera hash SHA-256 de "username:password"
    public static String generateLoginHash(String username, String password) {
        try {
            String combined = username + ":" + password;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(combined.getBytes(StandardCharsets.UTF_8));
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = String.format("%02x", b);
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al generar hash SHA-256", e);
        }
    }

    public static boolean verifyPassword(String username, String password, String storedHash) {
        String generatedHash = generateLoginHash(username, password);
        return generatedHash.equals(storedHash);
    }

    public static String getValidationErrorMessage(String username) {
        if (username == null || username.isEmpty()) {
            return "El nombre de usuario no puede estar vacío";
        }
        if (username.length() < 15) {
            return "El nombre de usuario debe tener al menos 15 caracteres";
        }
        if (!Character.isLetter(username.charAt(0))) {
            return "El nombre de usuario debe comenzar con una letra";
        }
        if (!Character.isUpperCase(username.charAt(0))) {
            return "El nombre de usuario debe comenzar con una letra mayúscula";
        }
        
        String lastThree = username.substring(username.length() - 3);
        if (!lastThree.matches("\\d{3}")) {
            return "El nombre de usuario debe terminar con exactamente 3 dígitos";
        }
        
        String withoutLastThree = username.substring(0, username.length() - 3);
        if (!withoutLastThree.matches("[a-zA-Z]+")) {
            return "El nombre de usuario solo puede contener letras";
        }
        
        return null;
    }
}
