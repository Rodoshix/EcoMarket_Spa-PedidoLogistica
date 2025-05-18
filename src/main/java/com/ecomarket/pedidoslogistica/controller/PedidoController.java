package com.ecomarket.pedidoslogistica.controller;

import com.ecomarket.pedidoslogistica.model.EstadoPedido;
import com.ecomarket.pedidoslogistica.model.Pedido;
import com.ecomarket.pedidoslogistica.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    // Crear un pedido
    @PostMapping
    public ResponseEntity<Pedido> crearPedido(@RequestBody Pedido pedido) {
        return ResponseEntity.ok(pedidoService.crearPedido(pedido));
    }

    // Obtener pedido por ID
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPedido(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtenerPedido(id));
    }

    // Obtener pedidos por email
    @GetMapping("/cliente/{email}")
    public ResponseEntity<List<Pedido>> buscarPorCliente(@PathVariable String email) {
        return ResponseEntity.ok(pedidoService.buscarPorCliente(email));
    }

    // Obtener pedidos por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Pedido>> buscarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(pedidoService.buscarPorEstado(estado.toUpperCase()));
    }

    // Actualizar estado de un pedido
    @PutMapping("/{id}/estado")
    public ResponseEntity<Pedido> actualizarEstado(
            @PathVariable Long id,
            @RequestParam EstadoPedido estado
    ) {
        return ResponseEntity.ok(pedidoService.actualizarEstado(id, estado));
    }

    // Cancelar pedido
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarPedido(@PathVariable Long id) {
        pedidoService.cancelarPedido(id);
        return ResponseEntity.noContent().build();
    }
}
