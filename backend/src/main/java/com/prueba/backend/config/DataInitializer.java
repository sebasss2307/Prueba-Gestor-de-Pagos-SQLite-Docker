package com.prueba.backend.config;

import com.prueba.backend.model.Recibo;
import com.prueba.backend.model.User;
import com.prueba.backend.repository.ReciboRepository;
import com.prueba.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ReciboRepository reciboRepository;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public DataInitializer(UserRepository userRepository, ReciboRepository reciboRepository) {
        this.userRepository = userRepository;
        this.reciboRepository = reciboRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            System.out.println("================================================");
            System.out.println("Inicializando base de datos desde archivos CSV...");
            System.out.println("================================================");
            
            loadUsersFromCsv();
            loadRecibosFromCsv();
            
            System.out.println("================================================");
            System.out.println("Base de datos inicializada correctamente");
            System.out.println("================================================");
        } else {
            System.out.println("Base de datos ya contiene datos, omitiendo inicialización");
        }
    }

    private void loadUsersFromCsv() {
        try {
            ClassPathResource resource = new ClassPathResource("Usuarios.csv");
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)
            );

            reader.readLine(); // Skip header
            
            String line;
            int count = 0;
            
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length < 6) continue;

                User user = new User();
                user.setUsername(parts[0].trim());
                user.setPasswordHash(parts[1].trim());
                user.setNombres(parts[2].trim());
                user.setPrimerApellido(parts[3].trim());
                user.setSegundoApellido(parts[4].trim());
                user.setFechaNacimiento(LocalDate.parse(parts[5].trim(), DATE_FORMATTER));

                userRepository.save(user);
                count++;
                System.out.println("✓ Usuario cargado: " + user.getUsername() + " (" + user.getNombreCompleto() + ")");
            }

            reader.close();
            System.out.println("✓ Total de usuarios cargados: " + count);
            
        } catch (Exception e) {
            System.err.println("Error al cargar usuarios desde CSV: " + e.getMessage());
            throw new RuntimeException("No se pudo cargar el archivo Usuarios.csv", e);
        }
    }

    private void loadRecibosFromCsv() {
        try {
            ClassPathResource resource = new ClassPathResource("Recibos de Pago.csv");
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)
            );

            reader.readLine(); // Skip header
            
            String line;
            int count = 0;
            
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length < 11) continue;

                Recibo recibo = new Recibo();
                recibo.setUsername(parts[0].trim());
                recibo.setNroRecibo(Integer.parseInt(parts[1].trim()));
                recibo.setFechaPago(LocalDate.parse(parts[2].trim(), DATE_FORMATTER));
                recibo.setPeriodo(parts[3].trim());
                recibo.setSueldoBase(Double.parseDouble(parts[4].trim()));
                recibo.setBonoProduccion(Double.parseDouble(parts[5].trim()));
                recibo.setDescuentoSalud(Double.parseDouble(parts[6].trim()));
                recibo.setDescuentoAfp(Double.parseDouble(parts[7].trim()));
                recibo.setOtrosDescuentos(Double.parseDouble(parts[8].trim()));
                recibo.setSueldoLiquido(Double.parseDouble(parts[9].trim()));
                recibo.setDetalle(parts[10].trim());

                reciboRepository.save(recibo);
                count++;
            }

            reader.close();
            System.out.println("✓ Total de recibos cargados: " + count);
            
        } catch (Exception e) {
            System.err.println("Error al cargar recibos desde CSV: " + e.getMessage());
            throw new RuntimeException("No se pudo cargar el archivo Recibos de Pago.csv", e);
        }
    }
}
