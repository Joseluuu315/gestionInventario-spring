package com.joseluu.proyectofinal.config;

import com.joseluu.proyectofinal.entity.Categoria;
import com.joseluu.proyectofinal.entity.Producto;
import com.joseluu.proyectofinal.entity.ProductoCategoria;
import com.joseluu.proyectofinal.repository.CategoriaRepository;
import com.joseluu.proyectofinal.repository.ProductoCategoriaRepository;
import com.joseluu.proyectofinal.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
/*
 * Configuracion tecnica en DataInitializer.
 * Declara beans y comportamiento transversal de la aplicacion.
 */
public class DataInitializer implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;
    private final ProductoCategoriaRepository productoCategoriaRepository;

    @Override
    /*
     * Carga datos iniciales al arrancar.
     * Inserta categorias y productos de ejemplo solo si la base esta vacia.
     */
    public void run(String... args) {
        if (categoriaRepository.count() > 0) {
            log.info("Datos de prueba ya existentes, saltando inicializacion.");
            return;
        }

        log.info("Cargando datos de prueba...");

        Categoria electronica = categoriaRepository.save(new Categoria("Electronica"));
        Categoria informatica = categoriaRepository.save(new Categoria("Informatica"));
        Categoria hogar = categoriaRepository.save(new Categoria("Hogar"));
        Categoria ropa = categoriaRepository.save(new Categoria("Ropa"));
        Categoria alimentacion = categoriaRepository.save(new Categoria("Alimentacion"));

        // Productos
        Producto p1 = new Producto();
        p1.setNombre("Portatil Lenovo ThinkPad");
        p1.setDescripcion("Portatil profesional con Intel i7, 16GB RAM, 512GB SSD");
        p1.setPrecio(new BigDecimal("1299.99"));
        p1.setStock(8);
        productoRepository.save(p1);
        productoCategoriaRepository.save(new ProductoCategoria(p1, electronica));
        productoCategoriaRepository.save(new ProductoCategoria(p1, informatica));

        Producto p2 = new Producto();
        p2.setNombre("Raton inalambrico Logitech MX");
        p2.setDescripcion("RAton multidispositivo");
        p2.setPrecio(new BigDecimal("79.90"));
        p2.setStock(3);
        productoRepository.save(p2);
        productoCategoriaRepository.save(new ProductoCategoria(p2, informatica));

        Producto p3 = new Producto();
        p3.setNombre("Auriculares Sony WH-1000XM5");
        p3.setDescripcion("Cancelacion de ruido y bateria de 20h");
        p3.setPrecio(new BigDecimal("349.00"));
        p3.setStock(15);
        productoRepository.save(p3);
        productoCategoriaRepository.save(new ProductoCategoria(p3, electronica));

        Producto p4 = new Producto();
        p4.setNombre("Cafetera Nespresso Vertuo");
        p4.setDescripcion("Cafetera automatica compatible con capsulas Vertuo");
        p4.setPrecio(new BigDecimal("129.00"));
        p4.setStock(2);
        productoRepository.save(p4);
        productoCategoriaRepository.save(new ProductoCategoria(p4, hogar));
        productoCategoriaRepository.save(new ProductoCategoria(p4, electronica));

        Producto p5 = new Producto();
        p5.setNombre("Camiseta Algodon Premium");
        p5.setDescripcion("100% algodon organico, varios colores disponibles");
        p5.setPrecio(new BigDecimal("24.99"));
        p5.setStock(50);
        productoRepository.save(p5);
        productoCategoriaRepository.save(new ProductoCategoria(p5, ropa));

        Producto p6 = new Producto();
        p6.setNombre("Monitor LG 27\" 4K");
        p6.setDescripcion("Panel IPS, 144Hz, HDR, USB-C");
        p6.setPrecio(new BigDecimal("499.00"));
        p6.setStock(4);
        productoRepository.save(p6);
        productoCategoriaRepository.save(new ProductoCategoria(p6, informatica));
        productoCategoriaRepository.save(new ProductoCategoria(p6, electronica));

        Producto p7 = new Producto();
        p7.setNombre("Pack Cafe Molido Especial");
        p7.setDescripcion("Mezcla de tueste medio, 500g, origen Etiopia");
        p7.setPrecio(new BigDecimal("12.50"));
        p7.setStock(1);
        productoRepository.save(p7);
        productoCategoriaRepository.save(new ProductoCategoria(p7, alimentacion));

        log.info("Datos de prueba cargados correctamente.");
    }
}
