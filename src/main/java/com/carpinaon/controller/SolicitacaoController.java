package com.carpinaon.controller;

import com.carpinaon.dto.solicitacao.SolicitacaoRequestDTO;
import com.carpinaon.dto.solicitacao.SolicitacaoResumidoDTO;
import com.carpinaon.dto.solicitacao.SolicitacaoResponseDTO;
import com.carpinaon.dto.solicitacao.StatusUpdateDTO;
import com.carpinaon.model.Usuario;
import com.carpinaon.service.SolicitacaoService;
import com.carpinaon.service.UsuarioLogadoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controller de solicitações - criar e acompanhar protocolos
@RestController
@RequestMapping("/api/v1/solicitacoes")
public class SolicitacaoController {

    @Autowired
    private SolicitacaoService solicitacaoService;

    @Autowired
    private UsuarioLogadoService usuarioLogadoService;

    // POST /api/v1/solicitacoes - abre um novo protocolo
    @PostMapping
    public ResponseEntity<SolicitacaoResponseDTO> criar(
            @RequestHeader(value = "Authorization") String authorization,
            @Valid @RequestBody SolicitacaoRequestDTO request) {

        Usuario usuario = usuarioLogadoService.buscar(authorization);
        SolicitacaoResponseDTO response = solicitacaoService.criar(usuario.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/v1/solicitacoes - meus protocolos (resumido)
    @GetMapping
    public ResponseEntity<List<SolicitacaoResumidoDTO>> listarMinhas(
            @RequestHeader(value = "Authorization") String authorization) {

        Usuario usuario = usuarioLogadoService.buscar(authorization);
        return ResponseEntity.ok(solicitacaoService.listarPorUsuario(usuario.getId()));
    }

    // GET /api/v1/solicitacoes/{protocolo} - detalhe de um protocolo
    @GetMapping("/{protocolo}")
    public ResponseEntity<SolicitacaoResponseDTO> buscar(@PathVariable String protocolo) {
        return ResponseEntity.ok(solicitacaoService.buscarPorProtocolo(protocolo));
    }

    // PATCH /api/v1/solicitacoes/{id}/status - atualiza status (só admin)
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SolicitacaoResponseDTO> atualizarStatus(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization") String authorization,
            @Valid @RequestBody StatusUpdateDTO request) {

        Usuario usuario = usuarioLogadoService.buscar(authorization);
        SolicitacaoResponseDTO response = solicitacaoService.atualizarStatus(
                id,
                request.getStatus(),
                request.getObservacao(),
                usuario.getId()
        );
        return ResponseEntity.ok(response);
    }
}