package com.joseluu.proyectofinal.controller;

import com.joseluu.proyectofinal.dto.CategoriaDTO;
import com.joseluu.proyectofinal.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
/*
 * Controlador de entrada para CategoriaRestController.
 * Procesa parametros HTTP, delega al servicio y prepara la vista o respuesta API.
 */
public class CategoriaRestController {

    private final CategoriaService categoriaService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    /*
     * Obtiene datos para listados.
     * Aplica filtros opcionales y prepara el resultado para vista o API.
     */
    public ResponseEntity<List<CategoriaDTO.Response>> listar() {
        return ResponseEntity.ok(categoriaService.listarCategorias());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    /*
     * Recupera un recurso por id.
     * Devuelve el registro solicitado o deja que la capa de errores lo gestione.
     */
    public ResponseEntity<CategoriaDTO.Response> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.obtenerCategoriaPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    /*
     * Crea un recurso desde API.
     * Valida entrada y responde con estado HTTP 201 cuando se crea correctamente.
     */
    public ResponseEntity<CategoriaDTO.Response> crear(@Valid @RequestBody CategoriaDTO.Request dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.crearCategoria(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    /*
     * Actualiza un recurso existente.
     * Aplica cambios validados y devuelve la representacion actualizada.
     */
    public ResponseEntity<CategoriaDTO.Response> actualizar(@PathVariable Long id,
                                                             @Valid @RequestBody CategoriaDTO.Request dto) {
        return ResponseEntity.ok(categoriaService.actualizarCategoria(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    /*
     * Elimina el recurso indicado.
     * Delegacion directa a servicio y respuesta acorde al tipo de endpoint.
     */
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        categoriaService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
