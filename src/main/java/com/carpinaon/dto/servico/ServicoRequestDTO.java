package com.carpinaon.dto.servico;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicoRequestDTO {

    @NotNull(message = "Categoria é obrigatória")
    private Long categoriaId;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    private String descricao;

    private String quandoUsar;

    private String naoUsarPara;

    private Boolean requerEndereco;

    // Tipo do formulário no app (ex: form_light_repair)
    private String formType;

    // Prazo estimado em dias úteis
    private Integer estimatedDays;

    // Documentos que o cidadão precisa anexar
    private List<String> requiredDocuments;

    // Se o serviço tá ativo ou não (admin pode esconder sem apagar)
    private Boolean ativo;
}
