package com.ecomarket.pedidoslogistica.controller;

import com.ecomarket.pedidoslogistica.assembler.PedidoModelAssembler;
import com.ecomarket.pedidoslogistica.model.EstadoPedido;
import com.ecomarket.pedidoslogistica.model.Pedido;
import com.ecomarket.pedidoslogistica.services.PedidoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/pedidos")
@Tag(name = "Pedidos V2", description = "Versión con HATEOAS")
public class PedidoControllerV2 {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private PedidoModelAssembler assembler;

    @PostMapping
    public ResponseEntity<EntityModel<Pedido>> crearPedido(@RequestBody Pedido pedido) {
        Pedido creado = pedidoService.crearPedido(pedido);
        return ResponseEntity.ok(assembler.toModel(creado));
    }

    @GetMapping("/{id}")
    public EntityModel<Pedido> obtenerPedido(@PathVariable Long id) {
        Pedido pedido = pedidoService.obtenerPedido(id);
        return assembler.toModel(pedido);
    }

    @GetMapping
    public CollectionModel<EntityModel<Pedido>> listarPedidos() {
        List<EntityModel<Pedido>> pedidos = pedidoService.listarTodos()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(pedidos,
                linkTo(methodOn(PedidoControllerV2.class).listarPedidos()).withSelfRel());
    }

    @GetMapping("/cliente/{email}")
    public CollectionModel<EntityModel<Pedido>> buscarPorCliente(@PathVariable String email) {
        List<EntityModel<Pedido>> pedidos = pedidoService.buscarPorCliente(email)
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(pedidos,
                linkTo(methodOn(PedidoControllerV2.class).buscarPorCliente(email)).withSelfRel());
    }

    @GetMapping("/estado/{estado}")
    public CollectionModel<EntityModel<Pedido>> buscarPorEstado(@PathVariable String estado) {
        List<EntityModel<Pedido>> pedidos = pedidoService.buscarPorEstado(estado.toUpperCase())
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(pedidos,
                linkTo(methodOn(PedidoControllerV2.class).buscarPorEstado(estado)).withSelfRel());
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<EntityModel<Pedido>> actualizarEstado(@PathVariable Long id,
                                                                 @RequestParam EstadoPedido estado) {
        Pedido actualizado = pedidoService.actualizarEstado(id, estado);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarPedido(@PathVariable Long id) {
        pedidoService.cancelarPedido(id);
        return ResponseEntity.noContent().build();
    }
}