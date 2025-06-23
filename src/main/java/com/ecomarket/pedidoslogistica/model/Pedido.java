package com.ecomarket.pedidoslogistica.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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

    @NotNull(message = "El ID de la venta asociada es obligatorio")
    @Schema(description = "ID de la venta asociada", example = "1001")
    private Long idVenta;

    @NotBlank(message = "El correo electrónico del cliente es obligatorio")
    @Email(message = "El correo electrónico debe tener un formato válido")
    @Size(max = 100, message = "El correo no debe superar los 100 caracteres")
    @Schema(description = "Correo electrónico del cliente", example = "cliente@correo.com", maxLength = 100)
    private String emailCliente;

    @NotBlank(message = "La dirección de despacho es obligatoria")
    @Size(max = 200, message = "La dirección de despacho no debe superar los 200 caracteres")
    @Schema(description = "Dirección donde se despachará el pedido", example = "Av. Los Leones 1234, Santiago", maxLength = 200)
    private String direccionDespacho;

    @NotNull(message = "La fecha de creación del pedido es obligatoria")
    @Schema(description = "Fecha de creación del pedido", example = "2025-06-17T10:30:00")
    private LocalDateTime fechaCreacion;

    @NotNull(message = "El estado del pedido es obligatorio")
    @Enumerated(EnumType.STRING)
    @Schema(description = "Estado actual del pedido", example = "PENDIENTE", allowableValues = {"PENDIENTE", "EN_DESPACHO", "ENTREGADO"})
    private EstadoPedido estado;
}