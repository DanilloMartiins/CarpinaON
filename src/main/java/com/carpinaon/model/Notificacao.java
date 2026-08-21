package com.carpinaon.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Notificações enviadas pro cidadão (in-app e push)
@Entity
@Table(name = "notificacao")
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Cada notificação é pra um usuário
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Quando a notificação nasce de uma solicitação (ex: mudança de status), guarda o vínculo
    @ManyToOne
    @JoinColumn(name = "solicitacao_id")
    private Solicitacao solicitacao;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String mensagem;

    // Se o cidadão já leu a notificação
    @Column(nullable = false)
    private Boolean lida = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Construtor padrão
    public Notificacao() {
    }

    // Construtor pra criar notificação nova
    public Notificacao(Usuario usuario, String titulo, String mensagem) {
        this.usuario = usuario;
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.lida = false;
        this.createdAt = LocalDateTime.now();
    }

    // Construtor com vínculo na solicitação (usado quando o status muda)
    public Notificacao(Usuario usuario, Solicitacao solicitacao, String titulo, String mensagem) {
        this(usuario, titulo, mensagem);
        this.solicitacao = solicitacao;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Solicitacao getSolicitacao() {
        return solicitacao;
    }

    public void setSolicitacao(Solicitacao solicitacao) {
        this.solicitacao = solicitacao;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Boolean getLida() {
        return lida;
    }

    public void setLida(Boolean lida) {
        this.lida = lida;
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
