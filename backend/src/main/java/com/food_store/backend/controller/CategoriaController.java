package com.food_store.backend.controller;

import com.food_store.backend.entity.Categoria;
import com.food_store.backend.entity.dto.categoriaDtos.CategoriaCreateDto;
import com.food_store.backend.entity.dto.categoriaDtos.CategoriaDto;
import com.food_store.backend.entity.mapper.CategoriaMapper;
import com.food_store.backend.service.ICategoriaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categoria")
public class CategoriaController {

    private final ICategoriaService iCategoriaService;

    public CategoriaController(ICategoriaService iCategoriaService) {
        this.iCategoriaService = iCategoriaService;
    }

    //Listar categorias
    @GetMapping("/listarCategorias")
    public List<CategoriaDto> listarCategorias(){

        return iCategoriaService.listarCategoria()
                .stream()
                .map(CategoriaMapper::toDto)
                .collect(Collectors.toList());
    }

    //Crear categoria
    @PostMapping("/crearCategorias")
    public ResponseEntity<CategoriaDto> crearCategoria(@RequestBody CategoriaCreateDto categoriaCreateDto){
        Categoria response = iCategoriaService.crearCategoria(categoriaCreateDto);
        CategoriaDto responseDto = CategoriaMapper.toDto(response);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/buscarCategoria/{categoria}")
    public ResponseEntity<CategoriaDto> buscarCategoria(@PathVariable String categoria){
        Optional<Categoria> response = iCategoriaService.buscarCategoria(categoria);
        CategoriaDto responseDto = CategoriaMapper.toDto(response.get());
        return new ResponseEntity<>(responseDto, HttpStatus.FOUND);
    }

    @DeleteMapping("/eliminarCategoria/{categoria}")
    public ResponseEntity<CategoriaDto> eliminarCategoria (@PathVariable String categoria){

return null;
    }

}
