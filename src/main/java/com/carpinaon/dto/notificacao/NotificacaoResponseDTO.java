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
    // Protocolo da solicitação quando a notificação nasce de uma mudança de status
    private String protocoloSolicitacao;
    private LocalDateTime createdAt;
}
