package com.ecomarket.pedidoslogistica.performance;

import com.ecomarket.pedidoslogistica.model.EstadoPedido;
import com.ecomarket.pedidoslogistica.model.Pedido;
import com.ecomarket.pedidoslogistica.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class PedidoPerformanceTest {

    @Autowired
    private PedidoRepository pedidoRepository;

    @BeforeEach
    void limpiar() {
        pedidoRepository.deleteAll();
    }

    @Test
    void testCrearPedidoPerformance() {
        long start = System.nanoTime();

        Pedido pedido = new Pedido(null, 999L, "perf@correo.com", "Calle Test 999", LocalDateTime.now(), EstadoPedido.PENDIENTE);
        pedidoRepository.save(pedido);

        long end = System.nanoTime();
        long durationMs = (end - start) / 1_000_000;

        System.out.println("Tiempo de creación: " + durationMs + " ms");

        assertTrue(durationMs < 100, "Crear pedido debería tomar menos de 100ms");
    }

    @RepeatedTest(5)
    void testBuscarPorEstadoPerformance() {
        for (int i = 0; i < 100; i++) {
            pedidoRepository.save(new Pedido(null, 500L + i, "cliente@correo.com", "Calle X", LocalDateTime.now(), EstadoPedido.EN_CAMINO));
        }

        long start = System.nanoTime();
        var resultados = pedidoRepository.findByEstado(EstadoPedido.EN_CAMINO);
        long end = System.nanoTime();

        long durationMs = (end - start) / 1_000_000;
        System.out.println("Buscar por estado (100 registros): " + durationMs + " ms");

        assertTrue(resultados.size() > 0, "La búsqueda debería devolver al menos un pedido");
        assertTrue(durationMs < 100, "Buscar por estado debería tomar menos de 100ms");
    }
}