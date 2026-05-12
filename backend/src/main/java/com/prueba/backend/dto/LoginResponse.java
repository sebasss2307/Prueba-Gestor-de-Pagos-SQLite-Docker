package com.prueba.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String username;
    private String nombres;
    private String primerApellido;
    private String segundoApellido;
    private String nombreCompleto;
    private String fechaNacimiento;
}
