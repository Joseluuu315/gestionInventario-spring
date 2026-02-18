package com.joseluu.proyectofinal.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * DTO container CategoriaDTO.
 * Agrupa modelos de entrada y salida para desacoplar API y entidades.
 */
public class CategoriaDTO {

    @Data
    @NoArgsConstructor
    /*
     * DTO container Request.
     * Agrupa modelos de entrada y salida para desacoplar API y entidades.
     */
    public static class Request {
        @NotBlank(message = "El nombre de la categoria es obligatorio")
        private String nombre;
    }

    @Data
    @NoArgsConstructor
    /*
     * DTO container Response.
     * Agrupa modelos de entrada y salida para desacoplar API y entidades.
     */
    public static class Response {
        private Long id;
        private String nombre;
        private int totalProductos;
    }
}
