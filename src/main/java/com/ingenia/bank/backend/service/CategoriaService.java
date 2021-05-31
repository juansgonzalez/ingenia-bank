package com.ingenia.bank.backend.service;

import com.ingenia.bank.backend.model.Categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaService {

    /**
     * Obtiene todas las categorías de la BD
     * @return lista de categorías
     */
    public List<Categoria> obtenerTodasCategorias();

    /**
     * Obtiene una categoría según su id
     * @param id
     * @return categoría
     */
    public Optional<Categoria> obtenerCategoriaById(Long id);

    /**
     * Crea una nueva categoría en la BD. Si ya existe un categoría con el mismo 'nombre' no se crea.
     * @param categoria
     * @return categoría creada
     */
    public Optional<Categoria> crearCategoria(Categoria categoria);
}
