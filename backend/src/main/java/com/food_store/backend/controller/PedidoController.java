package com.food_store.backend.controller;

import com.food_store.backend.entity.dto.PedidoDtos.PedidoCreateDto;
import com.food_store.backend.entity.dto.PedidoDtos.PedidoDto;
import com.food_store.backend.entity.enums.Estado;
import com.food_store.backend.service.IPedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedido")
public class PedidoController {


    private final IPedidoService iPedidoService;

    public PedidoController(IPedidoService iPedidoService) {
        this.iPedidoService = iPedidoService;
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearPedido(@RequestBody PedidoCreateDto pedidoCreateDto) {
        try {
            PedidoDto response = iPedidoService.crearPedido(pedidoCreateDto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<?> listarPedidos(){
        try {
            List<PedidoDto> response = iPedidoService.listarPedidos();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/buscarPorId/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id){
        try {
            PedidoDto response = iPedidoService.buscarPorId(id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/eliminar/{id}")
    @Transactional
    public ResponseEntity<?> eliminarPedido(@PathVariable Long id){
        try {
            iPedidoService.eliminarPedido(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/actualizarEstado/{id}")
    public ResponseEntity<?> actualizarEstadoPedido(@PathVariable Long id, @RequestBody PedidoDto pedidoDto){
        try {
            PedidoDto response = iPedidoService.actualizarEestadoPedido(id, pedidoDto.getEstado());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<?> pedidosUsuario(@PathVariable Long id){
        try {
            List<PedidoDto> response = iPedidoService.pedidosUsuario(id);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
