package com.ecomarket.pedidoslogistica.services;

import com.ecomarket.pedidoslogistica.dto.NotificacionDTO;
import com.ecomarket.pedidoslogistica.model.EstadoPedido;
import com.ecomarket.pedidoslogistica.model.Pedido;
import com.ecomarket.pedidoslogistica.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private RestTemplate restTemplate;

    // Crear un nuevo pedido
    public Pedido crearPedido(Pedido pedido) {
        pedido.setFechaCreacion(LocalDateTime.now());
        pedido.setEstado(EstadoPedido.PENDIENTE);

        Pedido guardado = pedidoRepository.save(pedido);

        // Enviar notificación
        NotificacionDTO dto = new NotificacionDTO();
        dto.setDestinatario(pedido.getEmailCliente());
        dto.setAsunto("EcoMarket - Pedido creado");
        dto.setCuerpo("Tu pedido ha sido recibido y está en preparación. ID Pedido: " + guardado.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<NotificacionDTO> request = new HttpEntity<>(dto, headers);

        try {
            restTemplate.postForEntity("http://localhost:8087/api/notificaciones/enviar", request, String.class);
            System.out.println("Correo enviado a: " + pedido.getEmailCliente());
        } catch (Exception e) {
            System.err.println("Error al enviar notificación: " + e.getMessage());
        }

        return guardado;
    }

    // Cambiar estado de un pedido
    public Pedido actualizarEstado(Long idPedido, EstadoPedido nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        pedido.setEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }

    // Obtener por ID
    public Pedido obtenerPedido(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }

    // Buscar pedidos por cliente
    public List<Pedido> buscarPorCliente(String email) {
        return pedidoRepository.findByEmailCliente(email);
    }

    // Buscar por estado
    public List<Pedido> buscarPorEstado(String estado) {
        return pedidoRepository.findByEstado(estado);
    }

    // Cancelar pedido
    public void cancelarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        if (pedido.getEstado() == EstadoPedido.ENTREGADO) {
            throw new RuntimeException("No se puede cancelar un pedido ya entregado");
        }

        pedido.setEstado(EstadoPedido.CANCELADO);
        pedidoRepository.save(pedido);
    }
}
