package com.carpinaon.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Eventos de turismo da cidade (festas, shows, feiras, etc)
@Entity
@Table(name = "evento_turismo")
public class EventoTurismo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    // Categoria do evento (música, gastronomia, religioso, etc)
    private String categoria;

    // Data de início e fim do evento
    @Column(nullable = false)
    private LocalDateTime dataInicio;

    @Column(nullable = false)
    private LocalDateTime dataFim;

    // Local do evento (texto livre ou referência)
    private String local;

    // URL da imagem de divulgação (opcional)
    private String imagemUrl;

    // Nota do ponto turístico (opcional, 0 a 5)
    private Double rating;

    // Construtor padrão
    public EventoTurismo() {
    }

    // Construtor pra criar evento novo
    public EventoTurismo(String titulo, String descricao, String categoria,
                         LocalDateTime dataInicio, LocalDateTime dataFim, String local) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.categoria = categoria;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.local = local;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDateTime dataFim) {
        this.dataFim = dataFim;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
