package com.joseluu.proyectofinal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
/*
 * Manejo centralizado de errores en GlobalExceptionHandler.
 * Estandariza respuestas de error para web y endpoints REST.
 */
public class GlobalExceptionHandler {


    @ExceptionHandler(RecursoNoEncontradoException.class)
    /*
     * Gestiona errores de recurso no encontrado.
     * Responde JSON o vista HTML segun el tipo de solicitud.
     */
    public Object handleRecursoNoEncontrado(RecursoNoEncontradoException ex,
                                            HttpServletRequest request) {
        if (esRest(request)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(errorBody(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI()));
        }
        return vistaError("error", ex.getMessage(), HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(NegocioException.class)
    /*
     * Gestiona excepciones de negocio.
     * Devuelve error 400 con mensaje funcional para cliente o usuario.
     */
    public Object handleNegocio(NegocioException ex, HttpServletRequest request) {
        if (esRest(request)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(errorBody(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI()));
        }
        return vistaError("error", ex.getMessage(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    /*
     * Gestiona errores de validacion en DTO.
     * Construye detalle por campo para API o vista de error para web.
     */
    public Object handleValidacion(MethodArgumentNotValidException ex, HttpServletRequest request) {
        if (esRest(request)) {
            Map<String, String> errores = new HashMap<>();
            ex.getBindingResult().getAllErrors().forEach(error -> {
                String campo = ((FieldError) error).getField();
                errores.put(campo, error.getDefaultMessage());
            });
            Map<String, Object> body = new HashMap<>();
            body.put("timestamp", LocalDateTime.now().toString());
            body.put("status", 400);
            body.put("error", "ValidaciÃƒÆ’Ã‚Â³n fallida");
            body.put("errores", errores);
            return ResponseEntity.badRequest().body(body);
        }
        return vistaError("error", "Datos de entrada invÃƒÆ’Ã‚Â¡lidos", HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(Exception.class)
    /*
     * Captura errores inesperados.
     * Evita fuga de detalles internos y devuelve respuesta uniforme.
     */
    public Object handleGeneral(Exception ex, HttpServletRequest request) {
        if (esRest(request)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorBody(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor",
                            request.getRequestURI()));
        }
        return vistaError("error", "Ha ocurrido un error inesperado: " + ex.getMessage(), 500);
    }


    /*
     * Detecta si la solicitud espera JSON.
     * Evalua ruta y cabecera Accept para seleccionar formato de error.
     */
    private boolean esRest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String accept = request.getHeader("Accept");
        return uri.startsWith("/api/") ||
               (accept != null && accept.contains("application/json"));
    }

    /*
     * Construye cuerpo estandar de error.
     * Incluye timestamp, estado HTTP, mensaje y path solicitado.
     */
    private Map<String, Object> errorBody(HttpStatus status, String mensaje, String path) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("mensaje", mensaje);
        body.put("path", path);
        return body;
    }

    /*
     * Construye ModelAndView de error.
     * Adjunta codigo y mensaje para renderizar una vista consistente.
     */
    private ModelAndView vistaError(String vista, String mensaje, int codigo) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("mensaje", mensaje);
        mav.addObject("codigo", codigo);
        return mav;
    }
}
