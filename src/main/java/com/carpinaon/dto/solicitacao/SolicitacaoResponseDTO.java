package com.carpinaon.dto.solicitacao;

import com.carpinaon.dto.servico.ServicoResponseDTO;
import com.carpinaon.dto.usuario.UsuarioResponseDTO;
import com.carpinaon.model.enums.StatusSolicitacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitacaoResponseDTO {

    private Long id;
    private String protocolo;
    private StatusSolicitacao status;
    private String cep;
    private String endereco;
    private String complemento;
    private String pontoReferencia;
    private String descricao;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UsuarioResponseDTO usuario;
    private ServicoResponseDTO servico;
    private List<AnexoResponseDTO> anexos;
    private List<HistoricoStatusResponseDTO> historico;
}
