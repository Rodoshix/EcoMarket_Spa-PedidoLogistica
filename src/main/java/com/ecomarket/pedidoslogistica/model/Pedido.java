package com.ecomarket.pedidoslogistica.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa un pedido logístico")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del pedido", example = "1")
    private Long id;

    @Schema(description = "ID de la venta asociada", example = "1001")
    private Long idVenta;

    @Schema(description = "Correo electrónico del cliente", example = "cliente@correo.com")
    private String emailCliente;

    @Schema(description = "Dirección donde se despachará el pedido", example = "Av. Los Leones 1234, Santiago")
    private String direccionDespacho;

    @Schema(description = "Fecha de creación del pedido", example = "2025-06-17T10:30:00")
    private LocalDateTime fechaCreacion;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Estado actual del pedido", example = "PENDIENTE")
    private EstadoPedido estado;
}