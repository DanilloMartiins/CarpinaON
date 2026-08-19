package com.carpinaon.dto.dashboard;

import com.carpinaon.model.enums.StatusSolicitacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusCountDTO {

    private StatusSolicitacao status;
    private String statusDescricao;
    private Long total;
}