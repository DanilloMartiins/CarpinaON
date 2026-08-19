package com.carpinaon.dto.solicitacao;

import com.carpinaon.dto.servico.ServicoResumidoDTO;
import com.carpinaon.model.enums.StatusSolicitacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitacaoResumidoDTO {

    private Long id;
    private String protocolo;
    private StatusSolicitacao status;
    private String statusDescricao;
    private ServicoResumidoDTO servico;
    private String descricao;
    private String endereco;
    private LocalDateTime createdAt;
}
