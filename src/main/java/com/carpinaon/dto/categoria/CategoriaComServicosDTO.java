package com.carpinaon.dto.categoria;

import com.carpinaon.dto.servico.ServicoResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// Categoria com os serviços dentro - formato que o app espera na home (1 chamada)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaComServicosDTO {

    private Long id;
    private String nome;
    private String descricao;
    private String icone;
    private String cor;
    private List<ServicoResponseDTO> servicos;
}