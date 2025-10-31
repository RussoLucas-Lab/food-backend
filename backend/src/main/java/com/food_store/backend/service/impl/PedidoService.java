package com.food_store.backend.service.impl;

import com.food_store.backend.entity.*;
import com.food_store.backend.entity.dto.DetallePedidoDtos.DetalleRequestDto;
import com.food_store.backend.entity.dto.PedidoDtos.PedidoCreateDto;
import com.food_store.backend.entity.dto.PedidoDtos.PedidoDto;
import com.food_store.backend.entity.dto.UsuarioDtos.UsuarioDto;
import com.food_store.backend.entity.dto.productoDtos.ProductoDto;
import com.food_store.backend.entity.enums.Estado;
import com.food_store.backend.entity.mapper.DetallePedidoMapper;
import com.food_store.backend.entity.mapper.PedidoMapper;
import com.food_store.backend.entity.mapper.UsuarioMapper;
import com.food_store.backend.repository.IPedidoRepository;
import com.food_store.backend.service.IPedidoService;
import com.food_store.backend.service.IProductoService;
import com.food_store.backend.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService implements IPedidoService {

    private final IPedidoRepository iPedidoRepository;
    private final IProductoService iProductoService;
    private final IUsuarioService iUsuarioService;

    @Autowired
    public PedidoService(IPedidoRepository iPedidoRepository, IProductoService iProductoService, IUsuarioService iUsuarioService) {
        this.iPedidoRepository = iPedidoRepository;
        this.iProductoService = iProductoService;
        this.iUsuarioService = iUsuarioService;
    }


    public PedidoDto crearPedido(PedidoCreateDto pedidoCreateDto) {

        Pedido pedidoCreate = PedidoMapper.toEntity(pedidoCreateDto);

        String estadoStr = pedidoCreateDto.getEstado();
        if (estadoStr == null || estadoStr.isBlank()) {
            throw new RuntimeException("El campo 'estado' no puede ser nulo");
        }

        pedidoCreate.setEstado(Estado.valueOf(estadoStr.toUpperCase()));

//      Agregamos los detalles

        for (DetalleRequestDto detalle : pedidoCreateDto.getDetalles()) {
            Producto producto = iProductoService.validarId(detalle.getProductoId());
            DetallePedido detalleCreate = DetallePedidoMapper.toEntity(detalle);
            detalleCreate.setProducto(producto);
            pedidoCreate.getDetalles().add(detalleCreate);
        }

        pedidoCreate.setUsuario(iUsuarioService.validarId(pedidoCreateDto.getIdUsuario()));
        iPedidoRepository.save(pedidoCreate);

        return PedidoMapper.toDto(pedidoCreate);
    }

    @Override
    public List<PedidoDto> listarPedidos() {
        return iPedidoRepository.findAll().stream()
                        .map(PedidoMapper::toDto)
                        .collect(Collectors.toList());
    }
    @Override
    public Pedido validarId(Long id) {
        if (id == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El ID no puede ser nulo"
            );
        }
        return iPedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Producto con ID " + id + " no encontrado"
                ));

    }

    @Override
    public List<PedidoDto> pedidosUsuario(Long idUsuario) {
        UsuarioDto usuariosearch = iUsuarioService.buscarPorId(idUsuario);
        List<PedidoDto> pedidosUsuario = new ArrayList<>();

        for (Long id: usuariosearch.getPedidosIds()){
            pedidosUsuario.add(buscarPorId(id));
        }
        return pedidosUsuario;
    }


    @Override
    public PedidoDto buscarPorId(Long id) {
        Pedido pedidoSearch = validarId(id);
        return PedidoMapper.toDto(pedidoSearch);
    }

    @Override
    public void eliminarPedido(Long id) {
        Pedido pedidoDelete = validarId(id);
        pedidoDelete.setEstado(Estado.CANCELADO);
        iPedidoRepository.save(pedidoDelete);
        iPedidoRepository.deleteById(pedidoDelete.getId_pedido());
    }

    @Override
    public PedidoDto actualizarEestadoPedido(Long id, Estado estado) {
        Pedido pedidoUpdate = validarId(id);

        for (Estado e: Estado.values()){
            if(e.equals(estado)){
                Estado estadoUpdate = estado;
                pedidoUpdate.setEstado(estadoUpdate);
                return PedidoMapper.toDto(iPedidoRepository.save(pedidoUpdate));
            }
        }
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Valor de estado del pedido inválido");
    }
}
