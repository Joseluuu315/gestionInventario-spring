package com.joseluu.proyectofinal.exception;

/*
 * Manejo centralizado de errores en RecursoNoEncontradoException.
 * Estandariza respuestas de error para web y endpoints REST.
 */
public class RecursoNoEncontradoException extends RuntimeException {
    /*
     * Implementa la logica del metodo RecursoNoEncontradoException.
     * Mantiene la responsabilidad de su capa dentro de la arquitectura.
     */
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
