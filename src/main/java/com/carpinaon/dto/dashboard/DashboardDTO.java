package com.carpinaon.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {

    private Long totalSolicitacoes;
    private Long novasHoje;
    private Long totalCidadaos;
    private List<StatusCountDTO> solicitacoesPorStatus;
}