package com.prueba.backend.controller;

import com.prueba.backend.model.Recibo;
import com.prueba.backend.repository.ReciboRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recibos")
@CrossOrigin(origins = "*")
public class ReciboController {
    private final ReciboRepository reciboRepository;

    public ReciboController(ReciboRepository reciboRepository) {
        this.reciboRepository = reciboRepository;
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getRecibos(@PathVariable String username) {
        try {
            List<Recibo> recibos = reciboRepository.findTop20ByUsernameOrderByFechaPagoDesc(username);
            return ResponseEntity.ok(recibos);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al obtener recibos: " + e.getMessage()));
        }
    }

    @GetMapping("/{username}/{nroRecibo}")
    public ResponseEntity<?> getReciboDetalle(
            @PathVariable String username,
            @PathVariable Integer nroRecibo) {
        try {
            Recibo recibo = reciboRepository.findByUsernameAndNroRecibo(username, nroRecibo)
                    .orElse(null);
            
            if (recibo == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Recibo no encontrado"));
            }
            
            return ResponseEntity.ok(recibo);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al obtener recibo: " + e.getMessage()));
        }
    }

    static class ErrorResponse {
        private String error;
        public ErrorResponse(String error) { this.error = error; }
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
    }
}
