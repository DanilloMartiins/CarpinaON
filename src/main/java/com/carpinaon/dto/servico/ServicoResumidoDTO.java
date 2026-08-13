package com.carpinaon.dto.servico;

import com.carpinaon.dto.categoria.CategoriaResumidoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicoResumidoDTO {

    private Long id;
    private String nome;
    private CategoriaResumidoDTO categoria;
}
