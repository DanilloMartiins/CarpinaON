package com.carpinaon.controller;

import com.carpinaon.dto.servico.ServicoResponseDTO;
import com.carpinaon.service.ServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Controller de serviços - consulta dos serviços do catálogo
@RestController
@RequestMapping("/api/v1/servicos")
public class ServicoController {

    @Autowired
    private ServicoService servicoService;

    // GET /api/v1/servicos - todos os serviços ativos
    @GetMapping
    public ResponseEntity<List<ServicoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(servicoService.listarTodos());
    }

    // GET /api/v1/servicos/categoria/{categoriaId} - serviços de uma categoria
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ServicoResponseDTO>> listarPorCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(servicoService.listarPorCategoria(categoriaId));
    }

    // GET /api/v1/servicos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ServicoResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(servicoService.buscar(id));
    }
}