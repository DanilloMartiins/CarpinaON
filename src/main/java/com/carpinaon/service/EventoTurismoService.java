package com.carpinaon.service;

import com.carpinaon.dto.turismo.EventoTurismoRequestDTO;
import com.carpinaon.dto.turismo.EventoTurismoResponseDTO;
import com.carpinaon.model.EventoTurismo;
import com.carpinaon.repository.EventoTurismoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

// Service de eventos de turismo - consulta e administração dos eventos da cidade
@Service
public class EventoTurismoService {

    @Autowired
    private EventoTurismoRepository eventoTurismoRepository;

    // Lista os próximos eventos (a partir de hoje)
    public List<EventoTurismoResponseDTO> listarProximos() {
        LocalDateTime agora = LocalDateTime.now();
        return eventoTurismoRepository.findByDataInicioBetween(
                        agora,
                        agora.plusMonths(6)
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Lista todos os eventos
    public List<EventoTurismoResponseDTO> listarTodos() {
        return eventoTurismoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Busca um evento pelo id
    public EventoTurismoResponseDTO buscar(Long id) {
        EventoTurismo evento = eventoTurismoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));
        return toResponse(evento);
    }

    // Cria um evento novo
    public EventoTurismoResponseDTO criar(EventoTurismoRequestDTO request) {
        EventoTurismo evento = new EventoTurismo();
        aplicarRequest(evento, request);
        return toResponse(eventoTurismoRepository.save(evento));
    }

    // Atualiza um evento existente
    public EventoTurismoResponseDTO atualizar(Long id, EventoTurismoRequestDTO request) {
        EventoTurismo evento = eventoTurismoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));
        aplicarRequest(evento, request);
        return toResponse(eventoTurismoRepository.save(evento));
    }

    // Deleta evento (físico - evento não tem campo ativo)
    public void deletar(Long id) {
        EventoTurismo evento = eventoTurismoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));
        eventoTurismoRepository.delete(evento);
    }

    // Copia os campos do request pra entidade
    private void aplicarRequest(EventoTurismo evento, EventoTurismoRequestDTO request) {
        evento.setTitulo(request.getTitulo());
        evento.setDescricao(request.getDescricao());
        evento.setCategoria(request.getCategoria());
        evento.setDataInicio(request.getDataInicio());
        evento.setDataFim(request.getDataFim());
        evento.setLocal(request.getLocal());
        evento.setImagemUrl(request.getImagemUrl());
        evento.setRating(request.getRating());
    }

    // Converte entidade pra DTO de resposta
    private EventoTurismoResponseDTO toResponse(EventoTurismo evento) {
        return new EventoTurismoResponseDTO(
                evento.getId(),
                evento.getTitulo(),
                evento.getDescricao(),
                evento.getCategoria(),
                evento.getDataInicio(),
                evento.getDataFim(),
                evento.getLocal(),
                evento.getImagemUrl(),
                evento.getRating(),
                evento.getCreatedAt(),
                evento.getUpdatedAt()
        );
    }
}
