package com.food_store.backend.service;

import com.food_store.backend.entity.Pedido;
import com.food_store.backend.entity.dto.PedidoDtos.PedidoCreateDto;
import com.food_store.backend.entity.dto.PedidoDtos.PedidoDto;
import com.food_store.backend.entity.enums.Estado;

import java.util.List;


public interface IPedidoService {
    PedidoDto crearPedido(PedidoCreateDto pedidoCreateDto);
    List<PedidoDto> listarPedidos();
    PedidoDto buscarPorId(Long id);
    void eliminarPedido(Long id);
    PedidoDto actualizarEestadoPedido(Long id, Estado estado);
    Pedido validarId(Long id);
    List<PedidoDto> pedidosUsuario(Long idUsuario);
    boolean checkearStock(Long idProducto, Integer stockPedido);

}
