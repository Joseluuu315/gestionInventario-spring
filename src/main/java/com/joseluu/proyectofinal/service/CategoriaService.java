package com.joseluu.proyectofinal.service;

import com.joseluu.proyectofinal.dto.CategoriaDTO;
import com.joseluu.proyectofinal.entity.Categoria;
import com.joseluu.proyectofinal.exception.NegocioException;
import com.joseluu.proyectofinal.exception.RecursoNoEncontradoException;
import com.joseluu.proyectofinal.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
/*
 * Servicio de aplicacion para CategoriaService.
 * Concentra reglas de negocio, validaciones y coordinacion de persistencia.
 */
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;


    /*
     * Crea una categoria con datos validados.
     * Si hay errores de binding, vuelve al formulario; si no, guarda y redirige.
     */
    public CategoriaDTO.Response crearCategoria(CategoriaDTO.Request dto) {
        if (categoriaRepository.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new NegocioException("Ya existe una categoria con el nombre: " + dto.getNombre());
        }
        Categoria categoria = new Categoria(dto.getNombre());
        return toResponse(categoriaRepository.save(categoria));
    }


    @Transactional(readOnly = true)
    /*
     * Lista categorias ordenadas.
     * Mapea entidades a DTO para salida limpia de capa de servicio.
     */
    public List<CategoriaDTO.Response> listarCategorias() {
        return categoriaRepository.findAllByOrderByNombreAsc()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    /*
     * Obtiene una categoria por id.
     * Reutiliza busqueda interna y transforma a DTO de respuesta.
     */
    public CategoriaDTO.Response obtenerCategoriaPorId(Long id) {
        return toResponse(findById(id));
    }

    @Transactional(readOnly = true)
    /*
     * Busca entidad por identificador.
     * Lanza excepcion de dominio cuando el registro no existe.
     */
    public Categoria findById(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoria no encontrada con id: " + id));
    }


    /*
     * Actualiza una categoria existente.
     * Valida datos y persiste cambios manteniendo reglas de negocio.
     */
    public CategoriaDTO.Response actualizarCategoria(Long id, CategoriaDTO.Request dto) {
        Categoria categoria = findById(id);
        if (!categoria.getNombre().equalsIgnoreCase(dto.getNombre())
                && categoriaRepository.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new NegocioException("Ya existe una categoria con el nombre: " + dto.getNombre());
        }
        categoria.setNombre(dto.getNombre());
        return toResponse(categoriaRepository.save(categoria));
    }


    /*
     * Elimina una categoria por id.
     * Garantiza existencia previa antes de ejecutar borrado.
     */
    public void eliminarCategoria(Long id) {
        Categoria categoria = findById(id);
        categoriaRepository.delete(categoria);
    }


    /*
     * Convierte entidad a DTO de salida.
     * Incluye campos principales y categorias para lectura completa.
     */
    private CategoriaDTO.Response toResponse(Categoria c) {
        CategoriaDTO.Response r = new CategoriaDTO.Response();
        r.setId(c.getId());
        r.setNombre(c.getNombre());
        r.setTotalProductos(c.getProductosCategorias() != null ? c.getProductosCategorias().size() : 0);
        return r;
    }
}
