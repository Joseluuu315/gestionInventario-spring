package com.joseluu.proyectofinal.repository;

import com.joseluu.proyectofinal.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
/*
 * Repositorio JPA de ProductoRepository.
 * Define consultas para lectura y escritura sobre el modelo de datos.
 */
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /*
     * Declara una operacion de acceso a datos.
     * Spring Data resuelve la consulta a partir de la firma o anotaciones.
     */
    List<Producto> findByNombreContainingIgnoreCaseOrderByNombreAsc(String nombre);

    /*
     * Declara una operacion de acceso a datos.
     * Spring Data resuelve la consulta a partir de la firma o anotaciones.
     */
    List<Producto> findByStockLessThanOrderByStockAsc(int umbral);

    @Query("SELECT DISTINCT p FROM Producto p " +
           "JOIN p.productosCategorias pc " +
           "JOIN pc.categoria c " +
           "WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombreCategoria, '%')) " +
           "ORDER BY p.nombre ASC")
    /*
     * Declara una operacion de acceso a datos.
     * Spring Data resuelve la consulta a partir de la firma o anotaciones.
     */
    List<Producto> findByCategoriaContainingIgnoreCase(@Param("nombreCategoria") String nombreCategoria);

    @Query("SELECT SUM(p.precio * p.stock) FROM Producto p")
    /*
     * Calcula el valor economico del inventario.
     * Devuelve cero cuando la consulta agregada retorna null.
     */
    BigDecimal calcularValorTotalInventario();
}
