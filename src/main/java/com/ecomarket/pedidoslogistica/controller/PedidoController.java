package com.ecomarket.pedidoslogistica.controller;

import com.ecomarket.pedidoslogistica.model.EstadoPedido;
import com.ecomarket.pedidoslogistica.model.Pedido;
import com.ecomarket.pedidoslogistica.services.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos", description = "Operaciones relacionadas con pedidos logísticos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Operation(summary = "Crear un nuevo pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<Pedido> crearPedido(
            @RequestBody Pedido pedido) {
        return ResponseEntity.ok(pedidoService.crearPedido(pedido));
    }

    @Operation(summary = "Obtener un pedido por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPedido(
            @Parameter(description = "ID del pedido") @PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtenerPedido(id));
    }

    @Operation(summary = "Buscar pedidos por correo electrónico del cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedidos encontrados")
    })
    @GetMapping("/cliente/{email}")
    public ResponseEntity<List<Pedido>> buscarPorCliente(
            @Parameter(description = "Correo electrónico del cliente") @PathVariable String email) {
        return ResponseEntity.ok(pedidoService.buscarPorCliente(email));
    }

    @Operation(summary = "Buscar pedidos por estado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedidos encontrados")
    })
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Pedido>> buscarPorEstado(
            @Parameter(description = "Estado del pedido (PENDIENTE, EN_CAMINO, ENTREGADO, CANCELADO)") 
            @PathVariable String estado) {
        return ResponseEntity.ok(pedidoService.buscarPorEstado(estado.toUpperCase()));
    }

    @Operation(summary = "Actualizar el estado de un pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    @PutMapping("/{id}/estado")
    public ResponseEntity<Pedido> actualizarEstado(
            @Parameter(description = "ID del pedido") @PathVariable Long id,
            @Parameter(description = "Nuevo estado del pedido") @RequestParam EstadoPedido estado) {
        return ResponseEntity.ok(pedidoService.actualizarEstado(id, estado));
    }

    @Operation(summary = "Cancelar un pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pedido cancelado"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarPedido(
            @Parameter(description = "ID del pedido") @PathVariable Long id) {
        pedidoService.cancelarPedido(id);
        return ResponseEntity.noContent().build();
    }
}