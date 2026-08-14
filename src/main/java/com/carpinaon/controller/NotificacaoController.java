package com.carpinaon.controller;

import com.carpinaon.dto.notificacao.NotificacaoResponseDTO;
import com.carpinaon.model.Usuario;
import com.carpinaon.service.NotificacaoService;
import com.carpinaon.service.UsuarioLogadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// Controller de notificações - consulta e leitura das notificações do cidadão
@RestController
@RequestMapping("/api/v1/notificacoes")
public class NotificacaoController {

    @Autowired
    private NotificacaoService notificacaoService;

    @Autowired
    private UsuarioLogadoService usuarioLogadoService;

    // GET /api/v1/notificacoes - lista minhas notificações
    @GetMapping
    public ResponseEntity<List<NotificacaoResponseDTO>> listar(
            @RequestHeader(value = "Authorization") String authorization) {

        Usuario usuario = usuarioLogadoService.buscar(authorization);
        return ResponseEntity.ok(notificacaoService.listarPorUsuario(usuario.getId()));
    }

    // GET /api/v1/notificacoes/nao-lidas - contagem pra badge do app
    @GetMapping("/nao-lidas")
    public ResponseEntity<Map<String, Long>> contarNaoLidas(
            @RequestHeader(value = "Authorization") String authorization) {

        Usuario usuario = usuarioLogadoService.buscar(authorization);
        long quantidade = notificacaoService.contarNaoLidas(usuario.getId());
        return ResponseEntity.ok(Map.of("quantidade", quantidade));
    }

    // PATCH /api/v1/notificacoes/{id}/lida - marca como lida
    @PatchMapping("/{id}/lida")
    public ResponseEntity<NotificacaoResponseDTO> marcarComoLida(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization") String authorization) {

        Usuario usuario = usuarioLogadoService.buscar(authorization);
        return ResponseEntity.ok(notificacaoService.marcarComoLida(id, usuario.getId()));
    }
}