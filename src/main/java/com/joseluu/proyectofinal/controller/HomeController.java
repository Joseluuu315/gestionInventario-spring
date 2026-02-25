package com.joseluu.proyectofinal.controller;

import com.joseluu.proyectofinal.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
/*
 * Controlador de entrada para HomeController.
 * Procesa parametros HTTP, delega al servicio y prepara la vista o respuesta API.
 */
public class HomeController {

    private final ProductoService productoService;

    @GetMapping("/")
    /*
     * Prepara el panel principal de la aplicacion.
     * Carga metricas de inventario para mostrarlas en la vista de inicio.
     */
    public String home(Model model) {
        model.addAttribute("totalProductos", productoService.listarProductos().size());
        model.addAttribute("valorInventario", productoService.calcularValorTotalInventario());
        model.addAttribute("productosStockBajo", productoService.listarConStockBajo().size());
        return "index";
    }

    @GetMapping("/login")
    /*
     * Devuelve la vista de login.
     * Centraliza la navegacion al formulario de autenticacion.
     */
    public String login() {
        return "login";
    }

    @GetMapping("/acceso-denegado")
    /*
     * Devuelve la vista de acceso denegado.
     * Se usa cuando un usuario autenticado no tiene permisos suficientes.
     */
    public String accesoDenegado() {
        return "403";
    }
}
