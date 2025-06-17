package com.ecomarket.pedidoslogistica.services;

import com.ecomarket.pedidoslogistica.model.EstadoPedido;
import com.ecomarket.pedidoslogistica.model.Pedido;
import com.ecomarket.pedidoslogistica.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
public class PedidoServiceTest {

    @InjectMocks
    private PedidoService pedidoService;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearPedido_enviaNotificacion() {
        Pedido pedido = new Pedido(null, 123L, "cliente@correo.com", "Av. Test", null, null);
        Pedido guardado = new Pedido(1L, 123L, "cliente@correo.com", "Av. Test", LocalDateTime.now(), EstadoPedido.PENDIENTE);

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(guardado);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
            .thenReturn(null);

            ReflectionTestUtils.setField(pedidoService, "urlNotificacion", "http://localhost:9999/api/fake");

        Pedido resultado = pedidoService.crearPedido(pedido);

        assertNotNull(resultado);
        assertEquals(EstadoPedido.PENDIENTE, resultado.getEstado());

        verify(pedidoRepository, times(1)).save(any(Pedido.class));
        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void testActualizarEstado() {
        Pedido pedido = new Pedido(1L, 123L, "correo@correo.com", "direccion", LocalDateTime.now(), EstadoPedido.PENDIENTE);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        Pedido actualizado = pedidoService.actualizarEstado(1L, EstadoPedido.ENTREGADO);

        assertEquals(EstadoPedido.ENTREGADO, actualizado.getEstado());
    }

    @Test
    void testCancelarPedido_exitoso() {
        Pedido pedido = new Pedido(1L, 100L, "correo", "direccion", LocalDateTime.now(), EstadoPedido.EN_CAMINO);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        pedidoService.cancelarPedido(1L);

        assertEquals(EstadoPedido.CANCELADO, pedido.getEstado());
        verify(pedidoRepository).save(pedido);
    }

    @Test
    void testCancelarPedido_fallaSiYaEntregado() {
        Pedido pedido = new Pedido(1L, 100L, "correo", "direccion", LocalDateTime.now(), EstadoPedido.ENTREGADO);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> pedidoService.cancelarPedido(1L));
        assertTrue(ex.getMessage().contains("No se puede cancelar"));
    }

    @Test
    void testBuscarPorCliente() {
        Pedido p = new Pedido(1L, 200L, "correo", "dir", LocalDateTime.now(), EstadoPedido.PENDIENTE);
        when(pedidoRepository.findByEmailCliente("correo")).thenReturn(List.of(p));

        List<Pedido> resultado = pedidoService.buscarPorCliente("correo");

        assertEquals(1, resultado.size());
    }

    @Test
    void testBuscarPorEstado() {
        Pedido p = new Pedido(1L, 200L, "correo", "dir", LocalDateTime.now(), EstadoPedido.EN_CAMINO);
        when(pedidoRepository.findByEstado(EstadoPedido.EN_CAMINO)).thenReturn(List.of(p));

        List<Pedido> resultado = pedidoService.buscarPorEstado("EN_CAMINO");

        assertEquals(EstadoPedido.EN_CAMINO, resultado.get(0).getEstado());
    }
    
    @Test
    void testObtenerPedidoPorId() {
        Pedido pedido = new Pedido(1L, 123L, "cliente@correo.com", "dir", LocalDateTime.now(), EstadoPedido.PENDIENTE);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        Pedido resultado = pedidoService.obtenerPedido(1L);

        assertEquals(1L, resultado.getId());
        assertEquals("cliente@correo.com", resultado.getEmailCliente());
    }
}