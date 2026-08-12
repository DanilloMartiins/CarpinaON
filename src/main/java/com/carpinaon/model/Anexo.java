package com.carpinaon.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Anexos/fotos que o cidadão pode enviar junto com a solicitação
@Entity
@Table(name = "anexo")
public class Anexo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Cada anexo pertence a uma solicitação
    @ManyToOne
    @JoinColumn(name = "solicitacao_id", nullable = false)
    private Solicitacao solicitacao;

    // URL do arquivo no storage (S3, MinIO, etc)
    @Column(nullable = false)
    private String urlArquivo;

    // Tipo do arquivo (image/jpeg, application/pdf, etc)
    @Column(nullable = false)
    private String tipoMime;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Construtor padrão
    public Anexo() {
    }

    // Construtor pra criar anexo novo
    public Anexo(Solicitacao solicitacao, String urlArquivo, String tipoMime) {
        this.solicitacao = solicitacao;
        this.urlArquivo = urlArquivo;
        this.tipoMime = tipoMime;
        this.createdAt = LocalDateTime.now();
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

    public String getUrlArquivo() {
        return urlArquivo;
    }

    public void setUrlArquivo(String urlArquivo) {
        this.urlArquivo = urlArquivo;
    }

    public String getTipoMime() {
        return tipoMime;
    }

    public void setTipoMime(String tipoMime) {
        this.tipoMime = tipoMime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
