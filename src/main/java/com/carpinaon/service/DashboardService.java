package com.carpinaon.service;

import com.carpinaon.dto.dashboard.DashboardDTO;
import com.carpinaon.dto.dashboard.StatusCountDTO;
import com.carpinaon.model.enums.PerfilUsuario;
import com.carpinaon.model.enums.StatusSolicitacao;
import com.carpinaon.repository.SolicitacaoRepository;
import com.carpinaon.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Service do dashboard - resume os números principais pro servidor
@Service
public class DashboardService {

    @Autowired
    private SolicitacaoRepository solicitacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Monta o resumo do dashboard (contadores por status, novas hoje, total de cidadãos)
    public DashboardDTO obterResumo() {
        Long totalSolicitacoes = solicitacaoRepository.count();
        Long novasHoje = solicitacaoRepository.countByCreatedAtAfter(LocalDate.now().atStartOfDay());
        Long totalCidadaos = usuarioRepository.countByRole(PerfilUsuario.CIDADAO);

        // Conta cada status sempre na mesma ordem (RECEBIDA -> ... -> CANCELADA)
        List<StatusCountDTO> porStatus = new ArrayList<>();
        for (StatusSolicitacao status : StatusSolicitacao.values()) {
            Long total = solicitacaoRepository.countByStatus(status);
            porStatus.add(new StatusCountDTO(status, status.getDescricao(), total));
        }

        return new DashboardDTO(totalSolicitacoes, novasHoje, totalCidadaos, porStatus);
    }
}