package com.carpinaon.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Categoria de serviços (ex: Saúde, Educação, Infraestrutura)
@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String descricao;

    // Ícone pra mostrar no app (pode ser nome do ícone ou URL)
    private String icone;

    // Cor em hexadecimal pra estilizar no app
    private String cor;

    // Se a categoria tá ativa ou não (pode esconder sem apagar)
    @Column(nullable = false)
    private Boolean ativo = true;

    // Quando foi criada e quando foi alterada pela última vez
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Construtor padrão
    public Categoria() {
    }

    // Construtor pra criar categoria nova
    public Categoria(String nome, String descricao, String icone, String cor) {
        this.nome = nome;
        this.descricao = descricao;
        this.icone = icone;
        this.cor = cor;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getIcone() {
        return icone;
    }

    public void setIcone(String icone) {
        this.icone = icone;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
