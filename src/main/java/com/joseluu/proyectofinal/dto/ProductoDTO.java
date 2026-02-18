package com.joseluu.proyectofinal.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/*
 * DTO container ProductoDTO.
 * Agrupa modelos de entrada y salida para desacoplar API y entidades.
 */
public class ProductoDTO {

    @Data
    @NoArgsConstructor
    /*
     * DTO container Request.
     * Agrupa modelos de entrada y salida para desacoplar API y entidades.
     */
    public static class Request {

        @NotBlank(message = "El nombre es obligatorio")
        private String nombre;

        private String descripcion;

        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.01", message = "El precio debe ser mayor que 0")
        private BigDecimal precio;

        @NotNull(message = "El stock es obligatorio")
        @Min(value = 0, message = "El stock no puede ser negativo")
        private Integer stock;

        private List<Long> categoriasIds;
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
        private String descripcion;
        private BigDecimal precio;
        private Integer stock;
        private List<String> categorias;
    }

    @Data
    @NoArgsConstructor
    /*
     * DTO container StockRequest.
     * Agrupa modelos de entrada y salida para desacoplar API y entidades.
     */
    public static class StockRequest {
        @NotNull(message = "El nuevo stock es obligatorio")
        @Min(value = 0, message = "El stock no puede ser negativo")
        private Integer stock;
    }

    @Data
    @NoArgsConstructor
    /*
     * DTO container PrecioRequest.
     * Agrupa modelos de entrada y salida para desacoplar API y entidades.
     */
    public static class PrecioRequest {
        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.01", message = "El precio debe ser mayor que 0")
        private BigDecimal precio;
    }
}
