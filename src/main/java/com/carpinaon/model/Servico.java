package com.carpinaon.model;

import jakarta.persistence.*;

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
}
