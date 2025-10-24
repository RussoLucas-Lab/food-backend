package com.food_store.backend.service.impl;

import com.food_store.backend.entity.Categoria;
import com.food_store.backend.entity.dto.categoriaDtos.CategoriaCreateDto;
import com.food_store.backend.entity.mapper.CategoriaMapper;
import com.food_store.backend.repository.ICategoriaRepository;
import com.food_store.backend.service.ICategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService implements ICategoriaService {

    private final ICategoriaRepository iCategoriaRepository;

    @Autowired
    public CategoriaService(ICategoriaRepository iCategoriaRepository) {
        this.iCategoriaRepository = iCategoriaRepository;
    }

    @Override
    public List<Categoria> listarCategoria() {

        return iCategoriaRepository.findAll();
    }

    @Override
    public Categoria crearCategoria(CategoriaCreateDto categoriaCreateDto) {

        if(categoriaCreateDto.getNombre().isEmpty() || categoriaCreateDto.getNombre() == null ){
            throw new IllegalArgumentException("El nombre de la categoria no debe ser nulo");
        }
        String stringSearch = categoriaCreateDto.getNombre().trim().toLowerCase();
        Optional<Categoria> checkCategoria = iCategoriaRepository.findByNombre(stringSearch);

        if(checkCategoria.isPresent()){
            throw new RuntimeException("Ya existe una categoría con el nombre: " + categoriaCreateDto.getNombre());
        }

        categoriaCreateDto.setNombre(stringSearch);
        Categoria categoriaCreate = CategoriaMapper.toEntity(categoriaCreateDto);
        iCategoriaRepository.save(categoriaCreate);

        return categoriaCreate;
    }

    @Override
    public Optional<Categoria> buscarCategoria(String categoriaNombre) {
        if (categoriaNombre == null || categoriaNombre.isEmpty()) {
            throw new IllegalArgumentException("La categoria no puede ser nula");
        }
        String categoriaSearch = categoriaNombre.trim().toLowerCase();
        return iCategoriaRepository.findByNombre(categoriaSearch)
                .or(() -> {
                    throw new RuntimeException("Categoria " + categoriaNombre + " no encontrada");
                });

    }

    @Override
    public String eliminarCategoria(String categoria) {

        Optional<Categoria> categoriaDelete = buscarCategoria(categoria);
        iCategoriaRepository.deleteByNombre(categoria);
        return"La categoria \" " + categoria +" \" fue eliminada correctamente" ;
    }




}
