package com.carpinaon.controller;

import com.carpinaon.dto.solicitacao.AnexoResponseDTO;
import com.carpinaon.model.Anexo;
import com.carpinaon.model.Usuario;
import com.carpinaon.service.AnexoService;
import com.carpinaon.service.SolicitacaoService;
import com.carpinaon.service.UsuarioLogadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

// Controller de anexos - upload e download de arquivos da solicitação
@RestController
@RequestMapping("/api/v1/solicitacoes/{solicitacaoId}/anexos")
public class AnexoController {

    @Autowired
    private AnexoService anexoService;

    @Autowired
    private SolicitacaoService solicitacaoService;

    @Autowired
    private UsuarioLogadoService usuarioLogadoService;

    // POST /api/v1/solicitacoes/{id}/anexos - sobe um arquivo (dono da solicitação ou admin)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AnexoResponseDTO> anexar(
            @PathVariable Long solicitacaoId,
            @RequestHeader(value = "Authorization") String authorization,
            @RequestParam("arquivo") MultipartFile arquivo) {

        Usuario usuario = usuarioLogadoService.buscar(authorization);
        Anexo anexo = anexoService.anexar(usuario.getId(), solicitacaoId, arquivo);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(solicitacaoService.converterAnexo(anexo));
    }

    // GET /api/v1/solicitacoes/{id}/anexos/{anexoId}/arquivo - baixa o arquivo
    @GetMapping("/{anexoId}/arquivo")
    public ResponseEntity<byte[]> baixar(
            @PathVariable Long solicitacaoId,
            @PathVariable Long anexoId,
            @RequestHeader(value = "Authorization") String authorization) {

        Usuario usuario = usuarioLogadoService.buscar(authorization);
        AnexoService.DadosDownload dados = anexoService.baixar(usuario.getId(), solicitacaoId, anexoId);

        // Encoda o nome pro header não quebrar com acento/espaço
        String nome = URLEncoder.encode(dados.nomeArquivo(), StandardCharsets.UTF_8).replace("+", "%20");
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dados.contentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nome + "\"")
                .body(dados.bytes());
    }
}