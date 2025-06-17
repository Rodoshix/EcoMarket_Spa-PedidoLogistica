package com.ecomarket.pedidoslogistica.integration;

import com.ecomarket.pedidoslogistica.model.EstadoPedido;
import com.ecomarket.pedidoslogistica.model.Pedido;
import com.ecomarket.pedidoslogistica.repository.PedidoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PedidoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        pedidoRepository.deleteAll(); // limpieza antes de cada test
    }

    @Test
    void testCrearYObtenerPedido() throws Exception {
        Pedido pedido = new Pedido();
        pedido.setIdVenta(200L);
        pedido.setEmailCliente("cliente@correo.com");
        pedido.setDireccionDespacho("Calle Real 456");

        String json = objectMapper.writeValueAsString(pedido);

        // Crear
        String response = mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.estado").value("PENDIENTE"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Pedido creado = objectMapper.readValue(response, Pedido.class);

        // Obtener por ID
        mockMvc.perform(get("/api/pedidos/" + creado.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.emailCliente").value("cliente@correo.com"));
    }

    @Test
    void testActualizarEstadoYCancelar() throws Exception {
        Pedido pedido = new Pedido(null, 201L, "cliente@correo.com", "Av. Cero 789", LocalDateTime.now(), EstadoPedido.PENDIENTE);
        pedido = pedidoRepository.save(pedido);

        // Actualizar estado
        mockMvc.perform(put("/api/pedidos/" + pedido.getId() + "/estado")
                        .param("estado", "EN_CAMINO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("EN_CAMINO"));

        // Cancelar
        mockMvc.perform(put("/api/pedidos/" + pedido.getId() + "/cancelar"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testBuscarPorEstado() throws Exception {
        Pedido pedido = new Pedido(null, 300L, "cliente@correo.com", "Av. Test", LocalDateTime.now(), EstadoPedido.EN_CAMINO);
        pedidoRepository.save(pedido);

        mockMvc.perform(get("/api/pedidos/estado/EN_CAMINO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testBuscarPorCliente() throws Exception {
        Pedido pedido = new Pedido(null, 301L, "cliente@correo.com", "Av. Cliente", LocalDateTime.now(), EstadoPedido.PENDIENTE);
        pedidoRepository.save(pedido);

        mockMvc.perform(get("/api/pedidos/cliente/cliente@correo.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}