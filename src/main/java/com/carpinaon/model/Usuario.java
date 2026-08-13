package com.carpinaon.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Entidade que representa o usuário do sistema (cidadão ou servidor)
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    // CPF único - pra identificar cada cidadão
    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private String email;

    private String telefone;

    // Número do Cartão Nacional de Saúde (opcional)
    private String numeroCNS;

    // Senha hash - nunca salvar em texto plano
    @Column(nullable = false)
    private String senha;

    // Status de verificação do cidadão (verificado ou não)
    @Column(nullable = false)
    private Boolean statusVerificacao = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Construtor padrão (obrigatório pro JPA)
    public Usuario() {
    }

    // Construtor pra criar usuário novo
    public Usuario(String nome, String cpf, String email, String telefone, String senha) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.senha = senha;
        this.statusVerificacao = false;
        this.createdAt = LocalDateTime.now();
    }

    // Getters e Setters
    // Não usei Lombok pra ficar explícito o que cada coisa faz

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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getNumeroCNS() {
        return numeroCNS;
    }

    public void setNumeroCNS(String numeroCNS) {
        this.numeroCNS = numeroCNS;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Boolean getStatusVerificacao() {
        return statusVerificacao;
    }

    public void setStatusVerificacao(Boolean statusVerificacao) {
        this.statusVerificacao = statusVerificacao;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Callback pra salvar a data de criação automático
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
