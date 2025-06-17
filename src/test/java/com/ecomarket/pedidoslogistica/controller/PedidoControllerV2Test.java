package com.ecomarket.pedidoslogistica.controller;

import com.ecomarket.pedidoslogistica.assembler.PedidoModelAssembler;
import com.ecomarket.pedidoslogistica.model.EstadoPedido;
import com.ecomarket.pedidoslogistica.model.Pedido;
import com.ecomarket.pedidoslogistica.services.PedidoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PedidoControllerV2.class)
@ActiveProfiles("test")
public class PedidoControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoService pedidoService;

    @MockBean
    private PedidoModelAssembler assembler;

    // --- Crear Pedido ---
    @Test
    void testCrearPedido() throws Exception {
        Pedido pedido = new Pedido(1L, 100L, "cliente@correo.com", "Av. Siempre Viva 123", LocalDateTime.now(), EstadoPedido.PENDIENTE);
        when(pedidoService.crearPedido(any(Pedido.class))).thenReturn(pedido);
        when(assembler.toModel(any(Pedido.class))).thenReturn(EntityModel.of(pedido));

        String json = """
                {
                    "idVenta": 100,
                    "emailCliente": "cliente@correo.com",
                    "direccionDespacho": "Av. Siempre Viva 123"
                }
                """;

        mockMvc.perform(post("/api/v2/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    // --- Obtener por ID ---
    @Test
    void testObtenerPedidoPorId() throws Exception {
        Pedido pedido = new Pedido(1L, 100L, "cliente@correo.com", "Av. Siempre Viva 123", LocalDateTime.now(), EstadoPedido.PENDIENTE);
        when(pedidoService.obtenerPedido(1L)).thenReturn(pedido);
        when(assembler.toModel(pedido)).thenReturn(EntityModel.of(pedido));

        mockMvc.perform(get("/api/v2/pedidos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    // --- Listar todos ---
    @Test
    void testListarPedidos() throws Exception {
        Pedido pedido = new Pedido(1L, 100L, "cliente@correo.com", "Calle A", LocalDateTime.now(), EstadoPedido.EN_CAMINO);
        List<Pedido> pedidos = List.of(pedido);

        when(pedidoService.listarTodos()).thenReturn(pedidos);
        when(assembler.toModel(pedido)).thenReturn(EntityModel.of(pedido));

        mockMvc.perform(get("/api/v2/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.pedidoList[0].id").value(1));
    }

    // --- Buscar por cliente ---
    @Test
    void testBuscarPorCliente() throws Exception {
        Pedido pedido = new Pedido(1L, 100L, "cliente@correo.com", "Calle B", LocalDateTime.now(), EstadoPedido.EN_CAMINO);
        when(pedidoService.buscarPorCliente("cliente@correo.com")).thenReturn(List.of(pedido));
        when(assembler.toModel(pedido)).thenReturn(EntityModel.of(pedido));

        mockMvc.perform(get("/api/v2/pedidos/cliente/cliente@correo.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.pedidoList[0].emailCliente").value("cliente@correo.com"));
    }

    // --- Buscar por estado ---
    @Test
    void testBuscarPorEstado() throws Exception {
        Pedido pedido = new Pedido(1L, 100L, "cliente@correo.com", "Calle C", LocalDateTime.now(), EstadoPedido.EN_CAMINO);
        when(pedidoService.buscarPorEstado("EN_CAMINO")).thenReturn(List.of(pedido));
        when(assembler.toModel(pedido)).thenReturn(EntityModel.of(pedido));

        mockMvc.perform(get("/api/v2/pedidos/estado/EN_CAMINO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.pedidoList[0].estado").value("EN_CAMINO"));
    }

    // --- Actualizar estado ---
    @Test
    void testActualizarEstado() throws Exception {
        Pedido pedido = new Pedido(1L, 100L, "cliente@correo.com", "Calle C", LocalDateTime.now(), EstadoPedido.ENTREGADO);
        when(pedidoService.actualizarEstado(1L, EstadoPedido.ENTREGADO)).thenReturn(pedido);
        when(assembler.toModel(pedido)).thenReturn(EntityModel.of(pedido));

        mockMvc.perform(put("/api/v2/pedidos/1/estado")
                        .param("estado", "ENTREGADO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("ENTREGADO"));
    }

    // --- Cancelar pedido ---
    @Test
    void testCancelarPedido() throws Exception {
        Mockito.doNothing().when(pedidoService).cancelarPedido(1L);

        mockMvc.perform(put("/api/v2/pedidos/1/cancelar"))
                .andExpect(status().isNoContent());
    }
}