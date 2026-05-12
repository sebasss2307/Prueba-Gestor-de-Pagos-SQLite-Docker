package com.prueba.backend.repository;

import com.prueba.backend.model.Recibo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReciboRepository extends JpaRepository<Recibo, Long> {
    List<Recibo> findTop20ByUsernameOrderByFechaPagoDesc(String username);
    List<Recibo> findByUsernameOrderByFechaPagoDesc(String username);
    Optional<Recibo> findByUsernameAndNroRecibo(String username, Integer nroRecibo);
    boolean existsByNroRecibo(Integer nroRecibo);
}
