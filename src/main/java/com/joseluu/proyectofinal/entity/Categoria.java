package com.joseluu.proyectofinal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categorias")
@Data
@NoArgsConstructor
/*
 * Entidad JPA Categoria.
 * Representa una tabla y sus relaciones dentro del dominio de inventario.
 */
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la categoria es obligatorio")
    @Column(nullable = false, unique = true)
    private String nombre;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductoCategoria> productosCategorias = new ArrayList<>();

    /*
     * Constructor utilitario de entidad.
     * Inicializa campos minimos para crear un estado valido del objeto.
     */
    public Categoria(String nombre) {
        this.nombre = nombre;
    }
}
