package com.carpinaon.dto.categoria;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    private String descricao;

    private String icone;

    private String cor;

    // Se a categoria tá ativa ou não (admin pode esconder sem apagar)
    private Boolean ativo;
}