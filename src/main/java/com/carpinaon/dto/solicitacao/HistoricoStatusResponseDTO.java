package com.carpinaon.dto.solicitacao;

import com.carpinaon.model.enums.StatusSolicitacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoStatusResponseDTO {

    private Long id;
    private StatusSolicitacao statusAnterior;
    private StatusSolicitacao statusNovo;
    private String observacao;
    private Long changedBy;
    private LocalDateTime changedAt;
}
