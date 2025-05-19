package com.ecomarket.pedidoslogistica.dto;

import lombok.Data;

@Data
public class NotificacionDTO {
    private String destinatario;
    private String asunto;
    private String cuerpo;
}