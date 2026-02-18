package com.joseluu.proyectofinal.repository;

import com.joseluu.proyectofinal.entity.Categoria;
import com.joseluu.proyectofinal.entity.Producto;
import com.joseluu.proyectofinal.entity.ProductoCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
/*
 * Repositorio JPA de ProductoCategoriaRepository.
 * Define consultas para lectura y escritura sobre el modelo de datos.
 */
public interface ProductoCategoriaRepository extends JpaRepository<ProductoCategoria, Long> {

    /*
     * Declara una operacion de acceso a datos.
     * Spring Data resuelve la consulta a partir de la firma o anotaciones.
     */
    List<ProductoCategoria> findByProductoOrderByCategoriaAsc(Producto producto);

    /*
     * Declara una operacion de acceso a datos.
     * Spring Data resuelve la consulta a partir de la firma o anotaciones.
     */
    boolean existsByProductoAndCategoria(Producto producto, Categoria categoria);

    /*
     * Declara una operacion de acceso a datos.
     * Spring Data resuelve la consulta a partir de la firma o anotaciones.
     */
    Optional<ProductoCategoria> findByProductoAndCategoria(Producto producto, Categoria categoria);

    /*
     * Declara una operacion de acceso a datos.
     * Spring Data resuelve la consulta a partir de la firma o anotaciones.
     */
    void deleteByProducto(Producto producto);
}
