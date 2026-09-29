package com.mycompany.sistema.reservas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Punto de entrada de la API REST. Los paquetes del proyecto (controller, service,
 * repository...) viven fuera de com.mycompany, por eso se declaran explícitamente.
 */
@SpringBootApplication(scanBasePackages = {"controller", "service", "repository", "mapper", "exception"})
@EntityScan(basePackages = "com.mycompany.sistema.reservas.dominio.modelo")
@EnableJpaRepositories(basePackages = "repository")
public class SistemaReservasApplication {

    public static void main(String[] args) {
        SpringApplication.run(SistemaReservasApplication.class, args);
    }
}
