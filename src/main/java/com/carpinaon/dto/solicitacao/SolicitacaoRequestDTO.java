package com.carpinaon.dto.solicitacao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitacaoRequestDTO {

    @NotEmpty(message = "Obrigatório selecionar o serviço")
    private List<Long> servicoIds;

    @NotBlank(message = "CEP é obrigatório")
    private String cep;

    private String complemento;
    private String pontoReferencia;

    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;
}
