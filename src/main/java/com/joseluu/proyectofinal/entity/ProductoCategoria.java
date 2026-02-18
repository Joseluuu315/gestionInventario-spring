package com.joseluu.proyectofinal.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "producto_categoria",
       uniqueConstraints = @UniqueConstraint(columnNames = {"producto_id", "categoria_id"}))
@Data
@NoArgsConstructor
/*
 * Entidad JPA ProductoCategoria.
 * Representa una tabla y sus relaciones dentro del dominio de inventario.
 */
public class ProductoCategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Column(nullable = false)
    private LocalDateTime fechaAsociacion = LocalDateTime.now();

    /*
     * Constructor utilitario de entidad.
     * Inicializa campos minimos para crear un estado valido del objeto.
     */
    public ProductoCategoria(Producto producto, Categoria categoria) {
        this.producto = producto;
        this.categoria = categoria;
        this.fechaAsociacion = LocalDateTime.now();
    }
}
