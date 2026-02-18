package com.joseluu.proyectofinal.controller;

import com.joseluu.proyectofinal.dto.ProductoDTO;
import com.joseluu.proyectofinal.service.CategoriaService;
import com.joseluu.proyectofinal.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/productos")
@RequiredArgsConstructor
/*
 * Controlador de entrada para ProductoController.
 * Procesa parametros HTTP, delega al servicio y prepara la vista o respuesta API.
 */
public class ProductoController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    @GetMapping
    /*
     * Obtiene datos para listados.
     * Aplica filtros opcionales y prepara el resultado para vista o API.
     */
    public String listar(@RequestParam(required = false) String buscar,
                         @RequestParam(required = false) String categoria,
                         Model model) {
        if (buscar != null && !buscar.isBlank()) {
            model.addAttribute("productos", productoService.buscarPorNombre(buscar));
            model.addAttribute("buscar", buscar);
        } else if (categoria != null && !categoria.isBlank()) {
            model.addAttribute("productos", productoService.buscarPorCategoria(categoria));
            model.addAttribute("categoria", categoria);
        } else {
            model.addAttribute("productos", productoService.listarProductos());
        }
        model.addAttribute("categorias", categoriaService.listarCategorias());
        model.addAttribute("valorTotal", productoService.calcularValorTotalInventario());
        return "productos/lista";
    }

    @GetMapping("/nuevo")
    /*
     * Prepara formulario de alta de producto.
     * Carga DTO vacio, catalogo de categorias y metadatos para la vista.
     */
    public String formularioNuevo(Model model) {
        model.addAttribute("productoForm", new ProductoDTO.Request());
        model.addAttribute("categorias", categoriaService.listarCategorias());
        model.addAttribute("titulo", "Nuevo Producto");
        return "productos/formulario";
    }

    @PostMapping("/nuevo")
    /*
     * Crea un producto nuevo.
     * Valida datos de entrada, persiste entidad y devuelve confirmacion.
     */
    public String crearProducto(@Valid @ModelAttribute("productoForm") ProductoDTO.Request dto,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            model.addAttribute("categorias", categoriaService.listarCategorias());
            model.addAttribute("titulo", "Nuevo Producto");
            return "productos/formulario";
        }
        productoService.crearProducto(dto);
        redirectAttrs.addFlashAttribute("exito", "Producto creado correctamente");
        return "redirect:/productos";
    }

    @GetMapping("/{id}/editar")
    /*
     * Prepara formulario de edicion.
     * Carga el estado actual del registro y lo mapea al DTO de entrada.
     */
    public String formularioEditar(@PathVariable Long id, Model model) {
        ProductoDTO.Response p = productoService.obtenerProductoPorId(id);
        ProductoDTO.Request form = new ProductoDTO.Request();
        form.setNombre(p.getNombre());
        form.setDescripcion(p.getDescripcion());
        form.setPrecio(p.getPrecio());
        form.setStock(p.getStock());
        model.addAttribute("productoForm", form);
        model.addAttribute("productoId", id);
        model.addAttribute("categorias", categoriaService.listarCategorias());
        model.addAttribute("titulo", "Editar Producto");
        return "productos/formulario";
    }

    @PostMapping("/{id}/editar")
    /*
     * Actualiza un producto existente.
     * Sincroniza datos base y sus asociaciones con categorias.
     */
    public String actualizarProducto(@PathVariable Long id,
                                     @Valid @ModelAttribute("productoForm") ProductoDTO.Request dto,
                                     BindingResult result,
                                     Model model,
                                     RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            model.addAttribute("productoId", id);
            model.addAttribute("categorias", categoriaService.listarCategorias());
            model.addAttribute("titulo", "Editar Producto");
            return "productos/formulario";
        }
        productoService.actualizarProducto(id, dto);
        redirectAttrs.addFlashAttribute("exito", "Producto actualizado correctamente");
        return "redirect:/productos";
    }

    @GetMapping("/{id}/stock")
    /*
     * Prepara la pantalla de ajuste de stock.
     * Expone el producto actual y un DTO para capturar el nuevo valor.
     */
    public String formularioStock(@PathVariable Long id, Model model) {
        model.addAttribute("producto", productoService.obtenerProductoPorId(id));
        model.addAttribute("stockForm", new ProductoDTO.StockRequest());
        return "productos/stock";
    }

    @PostMapping("/{id}/stock")
    /*
     * Actualiza el stock de un producto.
     * Valida que no sea negativo antes de persistir.
     */
    public String actualizarStock(@PathVariable Long id,
                                  @Valid @ModelAttribute("stockForm") ProductoDTO.StockRequest dto,
                                  BindingResult result,
                                  Model model,
                                  RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            model.addAttribute("producto", productoService.obtenerProductoPorId(id));
            return "productos/stock";
        }
        productoService.actualizarStock(id, dto.getStock());
        redirectAttrs.addFlashAttribute("exito", "Stock actualizado correctamente");
        return "redirect:/productos";
    }

    @GetMapping("/{id}/precio")
    /*
     * Prepara la pantalla de ajuste de precio.
     * Expone el producto y un DTO para capturar el nuevo importe.
     */
    public String formularioPrecio(@PathVariable Long id, Model model) {
        model.addAttribute("producto", productoService.obtenerProductoPorId(id));
        model.addAttribute("precioForm", new ProductoDTO.PrecioRequest());
        return "productos/precio";
    }

    @PostMapping("/{id}/precio")
    /*
     * Actualiza el precio de un producto.
     * Valida reglas de negocio del precio y guarda el cambio.
     */
    public String actualizarPrecio(@PathVariable Long id,
                                   @Valid @ModelAttribute("precioForm") ProductoDTO.PrecioRequest dto,
                                   BindingResult result,
                                   Model model,
                                   RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            model.addAttribute("producto", productoService.obtenerProductoPorId(id));
            return "productos/precio";
        }
        productoService.actualizarPrecio(id, dto.getPrecio());
        redirectAttrs.addFlashAttribute("exito", "Precio actualizado correctamente");
        return "redirect:/productos";
    }

    @GetMapping("/{id}/detalle")
    /*
     * Muestra detalle de producto.
     * Carga el registro por id y lo publica en la vista de detalle.
     */
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("producto", productoService.obtenerProductoPorId(id));
        return "productos/detalle";
    }

    @PostMapping("/{id}/eliminar")
    /*
     * Elimina el recurso indicado.
     * Delegacion directa a servicio y respuesta acorde al tipo de endpoint.
     */
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        productoService.eliminarProducto(id);
        redirectAttrs.addFlashAttribute("exito", "Producto eliminado correctamente");
        return "redirect:/productos";
    }

    @GetMapping("/stock-bajo")
    /*
     * Lista productos con stock bajo.
     * Permite definir umbral para monitoreo y reposicion operativa.
     */
    public String stockBajo(@RequestParam(defaultValue = "5") int umbral, Model model) {
        model.addAttribute("productos", productoService.listarConStockBajoPersonalizado(umbral));
        model.addAttribute("umbral", umbral);
        return "productos/stock-bajo";
    }
}
