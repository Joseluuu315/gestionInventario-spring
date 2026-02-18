package com.joseluu.proyectofinal.service;

import com.joseluu.proyectofinal.dto.ProductoDTO;
import com.joseluu.proyectofinal.entity.Categoria;
import com.joseluu.proyectofinal.entity.Producto;
import com.joseluu.proyectofinal.entity.ProductoCategoria;
import com.joseluu.proyectofinal.exception.NegocioException;
import com.joseluu.proyectofinal.exception.RecursoNoEncontradoException;
import com.joseluu.proyectofinal.repository.ProductoCategoriaRepository;
import com.joseluu.proyectofinal.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
/*
 * Servicio de aplicacion para ProductoService.
 * Concentra reglas de negocio, validaciones y coordinacion de persistencia.
 */
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final ProductoCategoriaRepository productoCategoriaRepository;
    private final CategoriaService categoriaService;

    @Value("${app.stock.umbral:5}")
    private int umbralStockBajo;


    /*
     * Crea un producto nuevo.
     * Valida datos de entrada, persiste entidad y devuelve confirmacion.
     */
    public ProductoDTO.Response crearProducto(ProductoDTO.Request dto) {
        Producto producto = new Producto();
        mapearRequest(dto, producto);
        Producto guardado = productoRepository.save(producto);
        asociarCategorias(guardado, dto.getCategoriasIds());
        return toResponse(guardado);
    }


    @Transactional(readOnly = true)
    /*
     * Lista todos los productos.
     * Transforma entidades a DTO para consumo de lectura.
     */
    public List<ProductoDTO.Response> listarProductos() {
        return productoRepository.findAll().stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    /*
     * Obtiene un producto por id.
     * Recupera la entidad y la convierte a DTO de salida.
     */
    public ProductoDTO.Response obtenerProductoPorId(Long id) {
        return toResponse(findById(id));
    }

    @Transactional(readOnly = true)
    /*
     * Busca entidad por identificador.
     * Lanza excepcion de dominio cuando el registro no existe.
     */
    public Producto findById(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con id: " + id));
    }

    @Transactional(readOnly = true)
    /*
     * Busca productos por nombre.
     * Usa coincidencia parcial ignorando mayusculas/minusculas.
     */
    public List<ProductoDTO.Response> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCaseOrderByNombreAsc(nombre)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    /*
     * Busca productos por categoria.
     * Consulta relaciones y devuelve coincidencias unicas ordenadas.
     */
    public List<ProductoDTO.Response> buscarPorCategoria(String categoria) {
        return productoRepository.findByCategoriaContainingIgnoreCase(categoria)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    /*
     * Lista productos bajo el umbral configurado.
     * Se usa para alertas de reposicion automatizadas o manuales.
     */
    public List<ProductoDTO.Response> listarConStockBajo() {
        return productoRepository.findByStockLessThanOrderByStockAsc(umbralStockBajo)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    /*
     * Lista productos bajo un umbral personalizado.
     * Permite analisis puntual sin tocar configuracion global.
     */
    public List<ProductoDTO.Response> listarConStockBajoPersonalizado(int umbral) {
        return productoRepository.findByStockLessThanOrderByStockAsc(umbral)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    /*
     * Calcula el valor economico del inventario.
     * Devuelve cero cuando la consulta agregada retorna null.
     */
    public BigDecimal calcularValorTotalInventario() {
        BigDecimal total = productoRepository.calcularValorTotalInventario();
        return total != null ? total : BigDecimal.ZERO;
    }

    @Transactional(readOnly = true)
    /*
     * Recupera categorias asociadas a un producto.
     * Proyecta nombres desde la tabla intermedia para salida ligera.
     */
    public List<String> obtenerCategoriasDeProducto(Long productoId) {
        Producto producto = findById(productoId);
        return productoCategoriaRepository.findByProductoOrderByCategoriaAsc(producto)
                .stream()
                .map(pc -> pc.getCategoria().getNombre())
                .collect(Collectors.toList());
    }


    /*
     * Actualiza un producto existente.
     * Sincroniza datos base y sus asociaciones con categorias.
     */
    public ProductoDTO.Response actualizarProducto(Long id, ProductoDTO.Request dto) {
        Producto producto = findById(id);
        mapearRequest(dto, producto);
        Producto guardado = productoRepository.save(producto);
        // Reasignar categorias
        productoCategoriaRepository.deleteByProducto(guardado);
        asociarCategorias(guardado, dto.getCategoriasIds());
        return toResponse(guardado);
    }

    /*
     * Actualiza el stock de un producto.
     * Valida que no sea negativo antes de persistir.
     */
    public ProductoDTO.Response actualizarStock(Long id, int nuevoStock) {
        if (nuevoStock < 0) {
            throw new NegocioException("El stock no puede ser negativo");
        }
        Producto producto = findById(id);
        producto.setStock(nuevoStock);
        return toResponse(productoRepository.save(producto));
    }

    /*
     * Actualiza el precio de un producto.
     * Valida reglas de negocio del precio y guarda el cambio.
     */
    public ProductoDTO.Response actualizarPrecio(Long id, BigDecimal nuevoPrecio) {
        if (nuevoPrecio == null || nuevoPrecio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new NegocioException("El precio debe ser mayor que 0");
        }
        Producto producto = findById(id);
        producto.setPrecio(nuevoPrecio);
        return toResponse(productoRepository.save(producto));
    }


    /*
     * Crea relacion producto-categoria.
     * Verifica existencia y evita asociaciones duplicadas.
     */
    public void asociarCategoria(Long productoId, Long categoriaId) {
        Producto producto = findById(productoId);
        Categoria categoria = categoriaService.findById(categoriaId);
        if (productoCategoriaRepository.existsByProductoAndCategoria(producto, categoria)) {
            throw new NegocioException("El producto ya pertenece a esa categorÃƒÆ’Ã‚Â­a");
        }
        productoCategoriaRepository.save(new ProductoCategoria(producto, categoria));
    }

    /*
     * Elimina relacion producto-categoria.
     * Borra solo la vinculacion solicitada.
     */
    public void desasociarCategoria(Long productoId, Long categoriaId) {
        Producto producto = findById(productoId);
        Categoria categoria = categoriaService.findById(categoriaId);
        ProductoCategoria pc = productoCategoriaRepository.findByProductoAndCategoria(producto, categoria)
                .orElseThrow(() -> new NegocioException("El producto no pertenece a esa categorÃƒÆ’Ã‚Â­a"));
        productoCategoriaRepository.delete(pc);
    }


    /*
     * Elimina un producto del inventario.
     * Busca por id y ejecuta borrado en repositorio.
     */
    public void eliminarProducto(Long id) {
        Producto producto = findById(id);
        productoRepository.delete(producto);
    }


    /*
     * Mapea DTO de entrada hacia entidad.
     * Centraliza asignaciones para reutilizar en crear y actualizar.
     */
    private void mapearRequest(ProductoDTO.Request dto, Producto producto) {
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
    }

    /*
     * Asocia una lista de categorias al producto.
     * Itera ids y persiste solo relaciones que no existen.
     */
    private void asociarCategorias(Producto producto, List<Long> categoriasIds) {
        if (categoriasIds == null || categoriasIds.isEmpty()) return;
        for (Long catId : categoriasIds) {
            Categoria categoria = categoriaService.findById(catId);
            if (!productoCategoriaRepository.existsByProductoAndCategoria(producto, categoria)) {
                productoCategoriaRepository.save(new ProductoCategoria(producto, categoria));
            }
        }
    }

    /*
     * Convierte entidad a DTO de salida.
     * Incluye campos principales y categorias para lectura completa.
     */
    public ProductoDTO.Response toResponse(Producto p) {
        ProductoDTO.Response r = new ProductoDTO.Response();
        r.setId(p.getId());
        r.setNombre(p.getNombre());
        r.setDescripcion(p.getDescripcion());
        r.setPrecio(p.getPrecio());
        r.setStock(p.getStock());
        List<ProductoCategoria> pcs = productoCategoriaRepository.findByProductoOrderByCategoriaAsc(p);
        r.setCategorias(pcs.stream().map(pc -> pc.getCategoria().getNombre()).collect(Collectors.toList()));
        return r;
    }

    /*
     * Expone umbral de stock bajo vigente.
     * Permite consultar el valor aplicado por configuracion.
     */
    public int getUmbralStockBajo() {
        return umbralStockBajo;
    }
}
