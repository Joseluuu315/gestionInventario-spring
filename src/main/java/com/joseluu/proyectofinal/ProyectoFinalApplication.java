package com.joseluu.proyectofinal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
/*
 * Componente ProyectoFinalApplication de la aplicacion.
 * Define comportamiento segun su responsabilidad dentro de la arquitectura.
 */
public class ProyectoFinalApplication {
    /*
     * Punto de entrada de la aplicacion.
     * Inicializa Spring Boot y registra todos los beans del contexto.
     */
    public static void main(String[] args) {
        SpringApplication.run(ProyectoFinalApplication.class, args);
    }
}
