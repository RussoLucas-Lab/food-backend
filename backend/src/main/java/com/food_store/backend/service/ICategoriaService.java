package com.food_store.backend.service;

import com.food_store.backend.entity.Categoria;
import com.food_store.backend.entity.dto.categoriaDtos.CategoriaCreateDto;

import java.util.List;
import java.util.Optional;

public interface ICategoriaService {

    List<Categoria> listarCategoria();
    Categoria crearCategoria(CategoriaCreateDto categoriaCreateDto);
    Optional<Categoria> buscarCategoria(String categoriaNombre);
    String eliminarCategoria(String categoria);
}
