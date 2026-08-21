package com.carpinaon.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Serviço específico dentro de uma categoria
// Ex: dentro de "Saúde" pode ter "Agendamento de Consulta", "Vacinação", etc
@Entity
@Table(name = "servico")
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Cada serviço pertence a uma categoria
    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Column(nullable = false)
    private String nome;

    private String descricao;

    // Texto explicando quando usar esse serviço
    @Column(columnDefinition = "TEXT")
    private String quandoUsar;

    // Texto explicando quando NÃO usar (evitar demandas indevidas)
    @Column(columnDefinition = "TEXT")
    private String naoUsarPara;

    // Se true, o formulário vai exigir CEP/endereço
    @Column(nullable = false)
    private Boolean requerEndereco = false;

    // Se o serviço tá ativo ou não (pode desativar sem deletar)
    @Column(nullable = false)
    private Boolean ativo = true;

    // Tipo do formulário no app (o Flutter usa isso pra abrir o formulário certo)
    private String formType;

    // Prazo estimado em dias úteis pra resposta do pedido
    private Integer estimatedDays = 0;

    // Documentos que o cidadão precisa anexar (ex: "Foto do Poste")
    @ElementCollection
    @CollectionTable(name = "servico_documentos", joinColumns = @JoinColumn(name = "servico_id"))
    @Column(name = "documento")
    private List<String> requiredDocuments = new ArrayList<>();

    // Quando foi criado e quando foi alterado pela última vez
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Construtor padrão
    public Servico() {
    }

    // Construtor pra criar serviço novo
    public Servico(Categoria categoria, String nome, String descricao,
                   String quandoUsar, String naoUsarPara, Boolean requerEndereco) {
        this.categoria = categoria;
        this.nome = nome;
        this.descricao = descricao;
        this.quandoUsar = quandoUsar;
        this.naoUsarPara = naoUsarPara;
        this.requerEndereco = requerEndereco;
        this.ativo = true;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
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

    public String getQuandoUsar() {
        return quandoUsar;
    }

    public void setQuandoUsar(String quandoUsar) {
        this.quandoUsar = quandoUsar;
    }

    public String getNaoUsarPara() {
        return naoUsarPara;
    }

    public void setNaoUsarPara(String naoUsarPara) {
        this.naoUsarPara = naoUsarPara;
    }

    public Boolean getRequerEndereco() {
        return requerEndereco;
    }

    public void setRequerEndereco(Boolean requerEndereco) {
        this.requerEndereco = requerEndereco;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public Integer getEstimatedDays() {
        return estimatedDays;
    }

    public void setEstimatedDays(Integer estimatedDays) {
        this.estimatedDays = estimatedDays;
    }

    public List<String> getRequiredDocuments() {
        return requiredDocuments;
    }

    public void setRequiredDocuments(List<String> requiredDocuments) {
        this.requiredDocuments = requiredDocuments;
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
