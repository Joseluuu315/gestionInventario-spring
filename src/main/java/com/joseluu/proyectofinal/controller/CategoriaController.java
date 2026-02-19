package com.joseluu.proyectofinal.controller;

import com.joseluu.proyectofinal.dto.CategoriaDTO;
import com.joseluu.proyectofinal.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categorias")
@RequiredArgsConstructor
/*
 * Controlador de entrada para CategoriaController.
 * Procesa parametros HTTP, delega al servicio y prepara la vista o respuesta API.
 */
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    /*
     * Obtiene datos para listados.
     * Aplica filtros opcionales y prepara el resultado para vista o API.
     */
    public String listar(Model model) {
        model.addAttribute("categorias", categoriaService.listarCategorias());
        return "categorias/lista";
    }

    @GetMapping("/nueva")
    /*
     * Prepara formulario de alta de categoria.
     * Inicializa DTO y atributos necesarios para renderizar la pantalla.
     */
    public String formularioNueva(Model model) {
        model.addAttribute("categoriaForm", new CategoriaDTO.Request());
        model.addAttribute("titulo", "Nueva Categoria");
        return "categorias/formulario";
    }

    @PostMapping("/nueva")
    /*
     * Crea una categoria con datos validados.
     * Si hay errores de binding, vuelve al formulario; si no, guarda y redirige.
     */
    public String crearCategoria(@Valid @ModelAttribute("categoriaForm") CategoriaDTO.Request dto,
                                 BindingResult result,
                                 Model model,
                                 RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Nueva Categoria");
            return "categorias/formulario";
        }
        categoriaService.crearCategoria(dto);
        redirectAttrs.addFlashAttribute("exito", "Categoria creada correctamente");
        return "redirect:/categorias";
    }

    @GetMapping("/{id}/editar")
    /*
     * Prepara formulario de edicion.
     * Carga el estado actual del registro y lo mapea al DTO de entrada.
     */
    public String formularioEditar(@PathVariable Long id, Model model) {
        CategoriaDTO.Response cat = categoriaService.obtenerCategoriaPorId(id);
        CategoriaDTO.Request form = new CategoriaDTO.Request();
        form.setNombre(cat.getNombre());
        model.addAttribute("categoriaForm", form);
        model.addAttribute("categoriaId", id);
        model.addAttribute("titulo", "Editar Categoria");
        return "categorias/formulario";
    }

    @PostMapping("/{id}/editar")
    /*
     * Actualiza una categoria existente.
     * Valida datos y persiste cambios manteniendo reglas de negocio.
     */
    public String actualizarCategoria(@PathVariable Long id,
                                      @Valid @ModelAttribute("categoriaForm") CategoriaDTO.Request dto,
                                      BindingResult result,
                                      Model model,
                                      RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            model.addAttribute("categoriaId", id);
            model.addAttribute("titulo", "Editar Categoria");
            return "categorias/formulario";
        }
        categoriaService.actualizarCategoria(id, dto);
        redirectAttrs.addFlashAttribute("exito", "Categoria actualizada correctamente");
        return "redirect:/categorias";
    }

    @PostMapping("/{id}/eliminar")
    /*
     * Elimina el recurso indicado.
     * Delegacion directa a servicio y respuesta acorde al tipo de endpoint.
     */
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        categoriaService.eliminarCategoria(id);
        redirectAttrs.addFlashAttribute("exito", "Categoria eliminada correctamente");
        return "redirect:/categorias";
    }
}
