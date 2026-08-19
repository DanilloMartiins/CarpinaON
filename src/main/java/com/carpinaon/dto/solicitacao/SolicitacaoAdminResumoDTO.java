package com.carpinaon.dto.solicitacao;

import com.carpinaon.model.enums.StatusSolicitacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitacaoAdminResumoDTO {

    private Long id;
    private String protocolo;
    private StatusSolicitacao status;
    private String statusDescricao;
    private String nomeCidadao;
    private String nomeServico;
    private String endereco;
    private LocalDateTime createdAt;
}