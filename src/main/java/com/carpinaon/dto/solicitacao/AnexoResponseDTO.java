package com.carpinaon.dto.solicitacao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnexoResponseDTO {

    private Long id;
    private String urlArquivo;
    private String tipoMime;
    private LocalDateTime createdAt;
}
