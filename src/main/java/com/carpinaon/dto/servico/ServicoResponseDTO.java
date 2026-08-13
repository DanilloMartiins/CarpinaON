package com.carpinaon.dto.servico;

import com.carpinaon.dto.categoria.CategoriaResumidoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicoResponseDTO {

    private Long id;
    private String nome;
    private String descricao;
    private String quandoUsar;
    private String naoUsarPara;
    private Boolean requerEndereco;
    private Boolean ativo;
    private CategoriaResumidoDTO categoria;
}
