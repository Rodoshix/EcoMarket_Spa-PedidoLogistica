package com.ecomarket.pedidoslogistica.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para enviar notificaciones por correo")
public class NotificacionDTO {

    @Schema(description = "Correo del destinatario", example = "cliente@correo.com")
    private String destinatario;

    @Schema(description = "Asunto del mensaje", example = "Pedido Despachado")
    private String asunto;

    @Schema(description = "Cuerpo del mensaje", example = "Tu pedido ha sido enviado y llegará pronto.")
    private String cuerpo;
}