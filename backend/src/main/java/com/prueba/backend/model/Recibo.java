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
@Table(name = "recibos")
public class Recibo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, unique = true)
    private Integer nroRecibo;

    @Column(nullable = false)
    private LocalDate fechaPago;

    @Column(nullable = false, length = 20)
    private String periodo;

    @Column(nullable = false)
    private Double sueldoBase;

    @Column(nullable = false)
    private Double bonoProduccion;

    @Column(nullable = false)
    private Double descuentoSalud;

    @Column(nullable = false)
    private Double descuentoAfp;

    @Column(nullable = false)
    private Double otrosDescuentos;

    @Column(nullable = false)
    private Double sueldoLiquido;

    @Column(length = 500)
    private String detalle;

    public double getTotalDescuentos() {
        return descuentoSalud + descuentoAfp + otrosDescuentos;
    }

    public double getTotalBruto() {
        return sueldoBase + bonoProduccion;
    }
}
