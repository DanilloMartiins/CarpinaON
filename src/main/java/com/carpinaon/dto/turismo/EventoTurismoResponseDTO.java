package com.carpinaon.dto.turismo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoTurismoResponseDTO {

    private Long id;
    private String titulo;
    private String descricao;
    private String categoria;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private String local;
    private String imagemUrl;
    private Double rating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
