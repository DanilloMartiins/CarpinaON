package com.carpinaon.dto.servico;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
