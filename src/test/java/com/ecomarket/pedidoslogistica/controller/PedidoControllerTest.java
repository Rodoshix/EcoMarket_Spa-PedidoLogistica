package com.ecomarket.pedidoslogistica.controller;

import com.ecomarket.pedidoslogistica.model.EstadoPedido;
import com.ecomarket.pedidoslogistica.model.Pedido;
import com.ecomarket.pedidoslogistica.services.PedidoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PedidoController.class)
@ActiveProfiles("test")
public class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoService pedidoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCrearPedido() throws Exception {
        Pedido pedido = new Pedido(null, 101L, "cliente@correo.com", "Av. Siempre Viva 123", null, null);
        Pedido guardado = new Pedido(1L, 101L, "cliente@correo.com", "Av. Siempre Viva 123", LocalDateTime.now(), EstadoPedido.PENDIENTE);

        Mockito.when(pedidoService.crearPedido(any(Pedido.class))).thenReturn(guardado);

        mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pedido)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }

    @Test
    void testObtenerPedidoPorId() throws Exception {
        Pedido pedido = new Pedido(1L, 101L, "cliente@correo.com", "Calle Falsa 123", LocalDateTime.now(), EstadoPedido.PENDIENTE);
        Mockito.when(pedidoService.obtenerPedido(1L)).thenReturn(pedido);

        mockMvc.perform(get("/api/pedidos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.emailCliente").value("cliente@correo.com"));
    }

    @Test
    void testBuscarPorCliente() throws Exception {
        Pedido p1 = new Pedido(1L, 101L, "cliente@correo.com", "Calle A", LocalDateTime.now(), EstadoPedido.PENDIENTE);
        Pedido p2 = new Pedido(2L, 102L, "cliente@correo.com", "Calle B", LocalDateTime.now(), EstadoPedido.EN_CAMINO);

        Mockito.when(pedidoService.buscarPorCliente("cliente@correo.com")).thenReturn(List.of(p1, p2));

        mockMvc.perform(get("/api/pedidos/cliente/cliente@correo.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void testBuscarPorEstado() throws Exception {
        Pedido pedido = new Pedido(1L, 101L, "cliente@correo.com", "Direccion", LocalDateTime.now(), EstadoPedido.EN_CAMINO);

        Mockito.when(pedidoService.buscarPorEstado("EN_CAMINO")).thenReturn(List.of(pedido));

        mockMvc.perform(get("/api/pedidos/estado/EN_CAMINO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value("EN_CAMINO"));
    }

    @Test
    void testActualizarEstado() throws Exception {
        Pedido pedido = new Pedido(1L, 101L, "cliente@correo.com", "Direccion", LocalDateTime.now(), EstadoPedido.ENTREGADO);

        Mockito.when(pedidoService.actualizarEstado(1L, EstadoPedido.ENTREGADO)).thenReturn(pedido);

        mockMvc.perform(put("/api/pedidos/1/estado")
                        .param("estado", "ENTREGADO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("ENTREGADO"));
    }

    @Test
    void testCancelarPedido() throws Exception {
        Mockito.doNothing().when(pedidoService).cancelarPedido(1L);

        mockMvc.perform(put("/api/pedidos/1/cancelar"))
                .andExpect(status().isNoContent());
    }
}