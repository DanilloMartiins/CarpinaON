package com.carpinaon.model;

import com.carpinaon.model.enums.StatusSolicitacao;
import jakarta.persistence.*;
import java.time.LocalDateTime;

// Histórico de mudanças de status da solicitação
// Cada vez que muda o status, salva um registro aqui pra auditoria
@Entity
@Table(name = "historico_status")
public class HistoricoStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Qual solicitação teve o status mudado
    @ManyToOne
    @JoinColumn(name = "solicitacao_id", nullable = false)
    private Solicitacao solicitacao;

    // Status que estava antes (null no primeiro registro, quando a solicitação é criada)
    @Enumerated(EnumType.STRING)
    private StatusSolicitacao statusAnterior;

    // Status novo
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusSolicitacao statusNovo;

    // Observação sobre a mudança (obrigatório quando indeferir)
    @Column(columnDefinition = "TEXT")
    private String observacao;

    // Quem mudou o status (ID do usuário ou admin)
    @Column(nullable = false)
    private Long changedBy;

    @Column(nullable = false, updatable = false)
    private LocalDateTime changedAt;

    // Construtor padrão
    public HistoricoStatus() {
    }

    // Construtor pra registrar mudança de status
    public HistoricoStatus(Solicitacao solicitacao, StatusSolicitacao statusAnterior,
                           StatusSolicitacao statusNovo, String observacao, Long changedBy) {
        this.solicitacao = solicitacao;
        this.statusAnterior = statusAnterior;
        this.statusNovo = statusNovo;
        this.observacao = observacao;
        this.changedBy = changedBy;
        this.changedAt = LocalDateTime.now();
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Solicitacao getSolicitacao() {
        return solicitacao;
    }

    public void setSolicitacao(Solicitacao solicitacao) {
        this.solicitacao = solicitacao;
    }

    public StatusSolicitacao getStatusAnterior() {
        return statusAnterior;
    }

    public void setStatusAnterior(StatusSolicitacao statusAnterior) {
        this.statusAnterior = statusAnterior;
    }

    public StatusSolicitacao getStatusNovo() {
        return statusNovo;
    }

    public void setStatusNovo(StatusSolicitacao statusNovo) {
        this.statusNovo = statusNovo;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Long getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(Long changedBy) {
        this.changedBy = changedBy;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(LocalDateTime changedAt) {
        this.changedAt = changedAt;
    }

    @PrePersist
    protected void onCreate() {
        this.changedAt = LocalDateTime.now();
    }
}
