package com.carpinaon.controller;

import com.carpinaon.dto.turismo.EventoTurismoResponseDTO;
import com.carpinaon.service.EventoTurismoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Controller de eventos de turismo - consulta dos eventos da cidade
@RestController
@RequestMapping("/api/v1/eventos")
public class EventoTurismoController {

    @Autowired
    private EventoTurismoService eventoTurismoService;

    // GET /api/v1/eventos/proximos - próximos eventos (pra home do app)
    @GetMapping("/proximos")
    public ResponseEntity<List<EventoTurismoResponseDTO>> listarProximos() {
        return ResponseEntity.ok(eventoTurismoService.listarProximos());
    }

    // GET /api/v1/eventos - todos os eventos
    @GetMapping
    public ResponseEntity<List<EventoTurismoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(eventoTurismoService.listarTodos());
    }

    // GET /api/v1/eventos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<EventoTurismoResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(eventoTurismoService.buscar(id));
    }
}