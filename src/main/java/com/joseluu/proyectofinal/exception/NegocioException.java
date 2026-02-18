package com.joseluu.proyectofinal.exception;

/*
 * Manejo centralizado de errores en NegocioException.
 * Estandariza respuestas de error para web y endpoints REST.
 */
public class NegocioException extends RuntimeException {
    /*
     * Implementa la logica del metodo NegocioException.
     * Mantiene la responsabilidad de su capa dentro de la arquitectura.
     */
    public NegocioException(String mensaje) {
        super(mensaje);
    }
}
