package com.food_store.backend.service.impl;

import com.food_store.backend.entity.Categoria;
import com.food_store.backend.entity.Producto;
import com.food_store.backend.entity.dto.productoDtos.ProductoCreateDto;
import com.food_store.backend.entity.dto.productoDtos.ProductoDto;
import com.food_store.backend.entity.mapper.ProductoMapper;
import com.food_store.backend.repository.ICategoriaRepository;
import com.food_store.backend.repository.IProductoRepository;
import com.food_store.backend.service.IProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ProductoService implements IProductoService {

    private final IProductoRepository iProductoRepository;
    private final ICategoriaRepository iCategoriaRepository;

    public ProductoService(IProductoRepository iProductoRepository, ICategoriaRepository iCategoriaRepository) {
        this.iProductoRepository = iProductoRepository;
        this.iCategoriaRepository = iCategoriaRepository;
    }

    @Override
    public Producto validarId(Long id) {
        if (id == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El ID no puede ser nulo"
            );
        }
        Producto productoSearch = iProductoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Producto con ID " + id + " no encontrado"
                ));
        return productoSearch;
    }


    @Override
    public List<ProductoDto> listarProductos() {
        return iProductoRepository.findAll().stream()
                .map(ProductoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductoDto buscarPorId(Long id) {
        return ProductoMapper.toDto(validarId(id));
    }


    @Override
    public ProductoDto crearProducto(ProductoCreateDto productoCreateDto) {
        Categoria categoria = iCategoriaRepository.findById(productoCreateDto.getCategoriaId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Categoria con id" + productoCreateDto.getCategoriaId() + " no encontrada"
                ));
        Producto productoCreate = ProductoMapper.toEntity(productoCreateDto);
        productoCreate.setCategoria(categoria);
        iProductoRepository.save(productoCreate);
        return ProductoMapper.toDto(productoCreate);
    }

    @Override
    public void eliminarProducto(Long id) {
        ProductoDto productoDelete = buscarPorId(id);
        iProductoRepository.deleteById(productoDelete.getId());
    }

    @Override
    public ProductoDto actualizarProducto(Long id, ProductoCreateDto productoCreateDto) {
        Producto productoUpdate = validarId(id);

        Categoria categoriaUpdate = iCategoriaRepository.findById(productoCreateDto.getCategoriaId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Categoria con id" + productoCreateDto.getCategoriaId() + " no encontrada"
                ));


        productoUpdate.setNombre(productoCreateDto.getNombre());
        productoUpdate.setDescripcion(productoCreateDto.getDescripcion());
        productoUpdate.setPrecio(productoCreateDto.getPrecio());
        productoUpdate.setImgURL(productoCreateDto.getImgURL());
        productoUpdate.setStock(productoCreateDto.getStock());
        productoUpdate.setCategoria(categoriaUpdate);

        return ProductoMapper.toDto(iProductoRepository.save(productoUpdate));
    }

    @Override
    public ProductoDto actualizarStock(Long id, Integer stock) {
        Producto productoUpdate = validarId(id);
        if (stock == null || stock < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El stock no puede ser negativo ni nulo"
            );
        }
        productoUpdate.setStock(stock);
        iProductoRepository.save(productoUpdate);

        return ProductoMapper.toDto(productoUpdate);
    }

    @Override
    public Integer obtenerStock(Long id) {
        Producto producto = validarId(id);
        return producto.getStock();
    }

    @Override
    public void aumentarStock(Long id, Integer cantidad) {
        Producto producto = validarId(id);
        Integer stockActual = obtenerStock(producto.getId());
        if (cantidad == null || cantidad < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El stock no puede ser negativo ni nulo"
            );
        }
        producto.setStock(stockActual + cantidad);
        iProductoRepository.save(producto);
    }

    @Override
    public void disminuirStock(Long id, Integer cantidad) {
        Producto producto = validarId(id);
        Integer stockActual = obtenerStock(producto.getId());
        if (cantidad == null || cantidad < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El stock no puede ser negativo ni nulo"
            );
        }
        producto.setStock(stockActual - cantidad);
        iProductoRepository.save(producto);
    }

}
