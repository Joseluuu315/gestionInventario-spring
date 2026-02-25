package com.joseluu.proyectofinal.controller;

import com.joseluu.proyectofinal.dto.ProductoDTO;
import com.joseluu.proyectofinal.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
/*
 * Controlador de entrada para ProductoRestController.
 * Procesa parametros HTTP, delega al servicio y prepara la vista o respuesta API.
 */
public class ProductoRestController {

    private final ProductoService productoService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    /*
     * Obtiene datos para listados.
     * Aplica filtros opcionales y prepara el resultado para vista o API.
     */
    public ResponseEntity<List<ProductoDTO.Response>> listar(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String categoria) {
        if (nombre != null && !nombre.isBlank()) {
            return ResponseEntity.ok(productoService.buscarPorNombre(nombre));
        }
        if (categoria != null && !categoria.isBlank()) {
            return ResponseEntity.ok(productoService.buscarPorCategoria(categoria));
        }
        return ResponseEntity.ok(productoService.listarProductos());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    /*
     * Recupera un recurso por id.
     * Devuelve el registro solicitado o deja que la capa de errores lo gestione.
     */
    public ResponseEntity<ProductoDTO.Response> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerProductoPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    /*
     * Crea un recurso desde API.
     * Valida entrada y responde con estado HTTP 201 cuando se crea correctamente.
     */
    public ResponseEntity<ProductoDTO.Response> crear(@Valid @RequestBody ProductoDTO.Request dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crearProducto(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    /*
     * Actualiza un recurso existente.
     * Aplica cambios validados y devuelve la representacion actualizada.
     */
    public ResponseEntity<ProductoDTO.Response> actualizar(@PathVariable Long id,
                                                            @Valid @RequestBody ProductoDTO.Request dto) {
        return ResponseEntity.ok(productoService.actualizarProducto(id, dto));
    }

    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasRole('ADMIN')")
    /*
     * Actualiza el stock de un producto.
     * Valida que no sea negativo antes de persistir.
     */
    public ResponseEntity<ProductoDTO.Response> actualizarStock(@PathVariable Long id,
                                                                  @Valid @RequestBody ProductoDTO.StockRequest dto) {
        return ResponseEntity.ok(productoService.actualizarStock(id, dto.getStock()));
    }

    @PatchMapping("/{id}/precio")
    @PreAuthorize("hasRole('ADMIN')")
    /*
     * Actualiza el precio de un producto.
     * Valida reglas de negocio del precio y guarda el cambio.
     */
    public ResponseEntity<ProductoDTO.Response> actualizarPrecio(@PathVariable Long id,
                                                                   @Valid @RequestBody ProductoDTO.PrecioRequest dto) {
        return ResponseEntity.ok(productoService.actualizarPrecio(id, dto.getPrecio()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    /*
     * Elimina el recurso indicado.
     * Delegacion directa a servicio y respuesta acorde al tipo de endpoint.
     */
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stock-bajo")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    /*
     * Lista productos con stock bajo.
     * Permite definir umbral para monitoreo y reposicion operativa.
     */
    public ResponseEntity<List<ProductoDTO.Response>> stockBajo(
            @RequestParam(defaultValue = "5") int umbral) {
        return ResponseEntity.ok(productoService.listarConStockBajoPersonalizado(umbral));
    }

    @GetMapping("/valor-inventario")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    /*
     * Calcula valor total del inventario.
     * Retorna la suma agregada de precio por stock disponible.
     */
    public ResponseEntity<Map<String, BigDecimal>> valorInventario() {
        return ResponseEntity.ok(Map.of("valorTotal", productoService.calcularValorTotalInventario()));
    }

    @GetMapping("/{id}/categorias")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    /*
     * Obtiene categorias de un producto.
     * Devuelve nombres asociados para consumo de interfaz o API.
     */
    public ResponseEntity<List<String>> categorias(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerCategoriasDeProducto(id));
    }

    @PostMapping("/{id}/categorias/{categoriaId}")
    @PreAuthorize("hasRole('ADMIN')")
    /*
     * Crea relacion producto-categoria.
     * Verifica existencia y evita asociaciones duplicadas.
     */
    public ResponseEntity<Void> asociarCategoria(@PathVariable Long id,
                                                  @PathVariable Long categoriaId) {
        productoService.asociarCategoria(id, categoriaId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}/categorias/{categoriaId}")
    @PreAuthorize("hasRole('ADMIN')")
    /*
     * Elimina relacion producto-categoria.
     * Borra solo la vinculacion solicitada.
     */
    public ResponseEntity<Void> desasociarCategoria(@PathVariable Long id,
                                                     @PathVariable Long categoriaId) {
        productoService.desasociarCategoria(id, categoriaId);
        return ResponseEntity.noContent().build();
    }
}
