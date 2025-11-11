package com.food_store.backend.service;

import com.food_store.backend.entity.Producto;
import com.food_store.backend.entity.dto.productoDtos.ProductoCreateDto;
import com.food_store.backend.entity.dto.productoDtos.ProductoDto;

import java.util.List;
import java.util.Optional;

public interface IProductoService {

    List<ProductoDto> listarProductos();
    ProductoDto buscarPorId(Long id);
    ProductoDto crearProducto(ProductoCreateDto productoCreateDto);
    void eliminarProducto (Long id);
    ProductoDto actualizarProducto(Long id, ProductoCreateDto productoCreateDto);
    ProductoDto actualizarStock(Long id, Integer stock);
    Producto validarId(Long id);
    Integer obtenerStock(Long id);
    void aumentarStock(Long id, Integer cantidad);
    void disminuirStock(Long id, Integer cantidad);
}
