package com.carpinaon.model.enums;

// Enum com os status possíveis de uma solicitação
// Seguindo o fluxo do drawio: RECEBIDA -> EM_ANALISE -> EM_ANDAMENTO -> RESOLVIDA/INDEFERIDA/CANCELADA
public enum StatusSolicitacao {

    RECEBIDA("Recebida"),
    EM_ANALISE("Em Análise"),
    EM_ANDAMENTO("Em Andamento"),
    RESOLVIDA("Resolvida"),
    INDEFERIDA("Indeferida"),
    CANCELADA("Cancelada");

    private final String descricao;

    StatusSolicitacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
