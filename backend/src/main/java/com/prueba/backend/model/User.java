package com.prueba.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 64)
    private String passwordHash;

    @Column(nullable = false, length = 100)
    private String nombres;

    @Column(nullable = false, length = 100)
    private String primerApellido;

    @Column(nullable = false, length = 100)
    private String segundoApellido;

    @Column(nullable = false)
    private LocalDate fechaNacimiento;

    public String getNombreCompleto() {
        return String.format("%s %s %s", nombres, primerApellido, segundoApellido);
    }
}
