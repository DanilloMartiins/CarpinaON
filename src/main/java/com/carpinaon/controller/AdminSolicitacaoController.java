package com.carpinaon.controller;

import com.carpinaon.dto.solicitacao.SolicitacaoAdminResumoDTO;
import com.carpinaon.dto.solicitacao.SolicitacaoResponseDTO;
import com.carpinaon.model.enums.StatusSolicitacao;
import com.carpinaon.service.SolicitacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

// Controller da área administrativa - gestão de solicitações (só servidor público)
@RestController
@RequestMapping("/api/v1/admin/solicitacoes")
@PreAuthorize("hasRole('ADMIN')")
public class AdminSolicitacaoController {

    @Autowired
    private SolicitacaoService solicitacaoService;

    // GET /api/v1/admin/solicitacoes - todas as solicitações com filtros
    @GetMapping
    public ResponseEntity<Page<SolicitacaoAdminResumoDTO>> listar(
            @RequestParam(required = false) StatusSolicitacao status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(required = false) String termo,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(solicitacaoService.listarAdmin(status, dataInicio, dataFim, termo, pageable));
    }

    // GET /api/v1/admin/solicitacoes/{id} - detalhe completo (cidadão + histórico + anexos)
    @GetMapping("/{id}")
    public ResponseEntity<SolicitacaoResponseDTO> detalhar(@PathVariable Long id) {
        return ResponseEntity.ok(solicitacaoService.buscarPorId(id));
    }
}