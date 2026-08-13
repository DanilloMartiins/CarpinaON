package com.carpinaon.dto.notificacao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacaoResponseDTO {

    private Long id;
    private String titulo;
    private String mensagem;
    private Boolean lida;
    private LocalDateTime createdAt;
}
