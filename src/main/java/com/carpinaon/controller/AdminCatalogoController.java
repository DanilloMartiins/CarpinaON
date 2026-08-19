package com.carpinaon.controller;

import com.carpinaon.dto.categoria.CategoriaRequestDTO;
import com.carpinaon.dto.categoria.CategoriaResponseDTO;
import com.carpinaon.dto.servico.ServicoRequestDTO;
import com.carpinaon.dto.servico.ServicoResponseDTO;
import com.carpinaon.dto.turismo.EventoTurismoRequestDTO;
import com.carpinaon.dto.turismo.EventoTurismoResponseDTO;
import com.carpinaon.service.CategoriaService;
import com.carpinaon.service.EventoTurismoService;
import com.carpinaon.service.ServicoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

// Controller da área administrativa - CRUD do catálogo (só servidor público)
@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCatalogoController {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private ServicoService servicoService;

    @Autowired
    private EventoTurismoService eventoTurismoService;

    // ================= CATEGORIAS =================

    // POST /api/v1/admin/categorias
    @PostMapping("/categorias")
    public ResponseEntity<CategoriaResponseDTO> criarCategoria(@Valid @RequestBody CategoriaRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.criar(request));
    }

    // PUT /api/v1/admin/categorias/{id}
    @PutMapping("/categorias/{id}")
    public ResponseEntity<CategoriaResponseDTO> atualizarCategoria(@PathVariable Long id, @Valid @RequestBody CategoriaRequestDTO request) {
        return ResponseEntity.ok(categoriaService.atualizar(id, request));
    }

    // DELETE /api/v1/admin/categorias/{id} (esconde sem apagar)
    @DeleteMapping("/categorias/{id}")
    public ResponseEntity<Void> deletarCategoria(@PathVariable Long id) {
        categoriaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // ================= SERVIÇOS =================

    // POST /api/v1/admin/servicos
    @PostMapping("/servicos")
    public ResponseEntity<ServicoResponseDTO> criarServico(@Valid @RequestBody ServicoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicoService.criar(request));
    }

    // PUT /api/v1/admin/servicos/{id}
    @PutMapping("/servicos/{id}")
    public ResponseEntity<ServicoResponseDTO> atualizarServico(@PathVariable Long id, @Valid @RequestBody ServicoRequestDTO request) {
        return ResponseEntity.ok(servicoService.atualizar(id, request));
    }

    // DELETE /api/v1/admin/servicos/{id} (esconde sem apagar)
    @DeleteMapping("/servicos/{id}")
    public ResponseEntity<Void> deletarServico(@PathVariable Long id) {
        servicoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // ================= EVENTOS =================

    // POST /api/v1/admin/eventos
    @PostMapping("/eventos")
    public ResponseEntity<EventoTurismoResponseDTO> criarEvento(@Valid @RequestBody EventoTurismoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventoTurismoService.criar(request));
    }

    // PUT /api/v1/admin/eventos/{id}
    @PutMapping("/eventos/{id}")
    public ResponseEntity<EventoTurismoResponseDTO> atualizarEvento(@PathVariable Long id, @Valid @RequestBody EventoTurismoRequestDTO request) {
        return ResponseEntity.ok(eventoTurismoService.atualizar(id, request));
    }

    // DELETE /api/v1/admin/eventos/{id}
    @DeleteMapping("/eventos/{id}")
    public ResponseEntity<Void> deletarEvento(@PathVariable Long id) {
        eventoTurismoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}