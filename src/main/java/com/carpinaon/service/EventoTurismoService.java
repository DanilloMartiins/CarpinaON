package com.carpinaon.service;

import com.carpinaon.dto.turismo.EventoTurismoResponseDTO;
import com.carpinaon.model.EventoTurismo;
import com.carpinaon.repository.EventoTurismoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

// Service de eventos de turismo - lista os eventos da cidade
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

    // Converte entidade pra DTO de resposta
    private EventoTurismoResponseDTO toResponse(EventoTurismo evento) {
        return new EventoTurismoResponseDTO(
                evento.getId(),
                evento.getTitulo(),
                evento.getDescricao(),
                evento.getCategoria(),
                evento.getDataInicio(),
                evento.getDataFim(),
                evento.getLocal()
        );
    }
}