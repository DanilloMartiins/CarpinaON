package com.carpinaon.service;

import com.carpinaon.dto.notificacao.NotificacaoResponseDTO;
import com.carpinaon.model.Notificacao;
import com.carpinaon.repository.NotificacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// Service de notificações - lista as notificações do cidadão
@Service
public class NotificacaoService {

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    // Lista notificações do usuário (mais recentes primeiro)
    public List<NotificacaoResponseDTO> listarPorUsuario(Long usuarioId) {
        return notificacaoRepository.findByUsuarioIdOrderByCreatedAtDesc(usuarioId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Conta quantas não lidas o usuário tem (badge do app)
    public long contarNaoLidas(Long usuarioId) {
        return notificacaoRepository.countByUsuarioIdAndLidaFalse(usuarioId);
    }

    // Marca uma notificação como lida
    public NotificacaoResponseDTO marcarComoLida(Long id, Long usuarioId) {
        Notificacao notificacao = notificacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificação não encontrada"));

        // Só o dono pode marcar a própria notificação
        if (!notificacao.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("Notificação não pertence ao usuário");
        }

        notificacao.setLida(true);
        return toResponse(notificacaoRepository.save(notificacao));
    }

    // Converte entidade pra DTO de resposta
    private NotificacaoResponseDTO toResponse(Notificacao notificacao) {
        return new NotificacaoResponseDTO(
                notificacao.getId(),
                notificacao.getTitulo(),
                notificacao.getMensagem(),
                notificacao.getLida(),
                notificacao.getCreatedAt()
        );
    }
}