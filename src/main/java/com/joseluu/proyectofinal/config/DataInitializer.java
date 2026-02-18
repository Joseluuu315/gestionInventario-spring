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
            log.info("Datos de prueba ya existentes, saltando inicializaciÃƒÆ’Ã‚Â³n.");
            return;
        }

        log.info("Cargando datos de prueba...");

        Categoria electronica = categoriaRepository.save(new Categoria("ElectrÃƒÆ’Ã‚Â³nica"));
        Categoria informatica = categoriaRepository.save(new Categoria("InformÃƒÆ’Ã‚Â¡tica"));
        Categoria hogar = categoriaRepository.save(new Categoria("Hogar"));
        Categoria ropa = categoriaRepository.save(new Categoria("Ropa"));
        Categoria alimentacion = categoriaRepository.save(new Categoria("AlimentaciÃƒÆ’Ã‚Â³n"));

        // Productos
        Producto p1 = new Producto();
        p1.setNombre("PortÃƒÆ’Ã‚Â¡til Lenovo ThinkPad");
        p1.setDescripcion("PortÃƒÆ’Ã‚Â¡til profesional con Intel i7, 16GB RAM, 512GB SSD");
        p1.setPrecio(new BigDecimal("1299.99"));
        p1.setStock(8);
        productoRepository.save(p1);
        productoCategoriaRepository.save(new ProductoCategoria(p1, electronica));
        productoCategoriaRepository.save(new ProductoCategoria(p1, informatica));

        Producto p2 = new Producto();
        p2.setNombre("RatÃƒÆ’Ã‚Â³n InalÃƒÆ’Ã‚Â¡mbrico Logitech MX");
        p2.setDescripcion("RatÃƒÆ’Ã‚Â³n ergonÃƒÆ’Ã‚Â³mico, baterÃƒÆ’Ã‚Â­a 70 dÃƒÆ’Ã‚Â­as, multi-dispositivo");
        p2.setPrecio(new BigDecimal("79.90"));
        p2.setStock(3);
        productoRepository.save(p2);
        productoCategoriaRepository.save(new ProductoCategoria(p2, informatica));

        Producto p3 = new Producto();
        p3.setNombre("Auriculares Sony WH-1000XM5");
        p3.setDescripcion("CancelaciÃƒÆ’Ã‚Â³n de ruido activa, 30h baterÃƒÆ’Ã‚Â­a, sonido premium");
        p3.setPrecio(new BigDecimal("349.00"));
        p3.setStock(15);
        productoRepository.save(p3);
        productoCategoriaRepository.save(new ProductoCategoria(p3, electronica));

        Producto p4 = new Producto();
        p4.setNombre("Cafetera Nespresso Vertuo");
        p4.setDescripcion("Cafetera automÃƒÆ’Ã‚Â¡tica compatible con cÃƒÆ’Ã‚Â¡psulas Vertuo");
        p4.setPrecio(new BigDecimal("129.00"));
        p4.setStock(2);
        productoRepository.save(p4);
        productoCategoriaRepository.save(new ProductoCategoria(p4, hogar));
        productoCategoriaRepository.save(new ProductoCategoria(p4, electronica));

        Producto p5 = new Producto();
        p5.setNombre("Camiseta AlgodÃƒÆ’Ã‚Â³n Premium");
        p5.setDescripcion("100% algodÃƒÆ’Ã‚Â³n orgÃƒÆ’Ã‚Â¡nico, varios colores disponibles");
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
        p7.setNombre("Pack CafÃƒÆ’Ã‚Â© Molido Especial");
        p7.setDescripcion("Mezcla de tueste medio, 500g, origen EtiopÃƒÆ’Ã‚Â­a");
        p7.setPrecio(new BigDecimal("12.50"));
        p7.setStock(1);
        productoRepository.save(p7);
        productoCategoriaRepository.save(new ProductoCategoria(p7, alimentacion));

        log.info("Datos de prueba cargados correctamente.");
    }
}
