package com.carpinaon.dto.servico;

import com.carpinaon.dto.categoria.CategoriaResumidoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private String formType;
    private Integer estimatedDays;
    private List<String> requiredDocuments;
    private CategoriaResumidoDTO categoria;
}
