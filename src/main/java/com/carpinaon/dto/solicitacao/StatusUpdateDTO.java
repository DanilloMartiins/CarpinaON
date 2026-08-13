package com.carpinaon.dto.solicitacao;

import com.carpinaon.model.enums.StatusSolicitacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusUpdateDTO {

    private StatusSolicitacao status;
    private String observacao;
}
