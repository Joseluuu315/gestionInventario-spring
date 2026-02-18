package com.joseluu.proyectofinal.repository;

import com.joseluu.proyectofinal.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
/*
 * Repositorio JPA de CategoriaRepository.
 * Define consultas para lectura y escritura sobre el modelo de datos.
 */
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    /*
     * Declara una operacion de acceso a datos.
     * Spring Data resuelve la consulta a partir de la firma o anotaciones.
     */
    Optional<Categoria> findByNombreIgnoreCase(String nombre);

    /*
     * Declara una operacion de acceso a datos.
     * Spring Data resuelve la consulta a partir de la firma o anotaciones.
     */
    List<Categoria> findAllByOrderByNombreAsc();

    /*
     * Declara una operacion de acceso a datos.
     * Spring Data resuelve la consulta a partir de la firma o anotaciones.
     */
    boolean existsByNombreIgnoreCase(String nombre);
}
