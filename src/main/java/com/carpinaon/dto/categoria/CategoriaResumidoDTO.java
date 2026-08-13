package com.carpinaon.dto.categoria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaResumidoDTO {

    private Long id;
    private String nome;
    private String icone;
    private String cor;
}
