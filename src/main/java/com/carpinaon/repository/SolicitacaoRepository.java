package com.carpinaon.repository;

import com.carpinaon.model.Solicitacao;
import com.carpinaon.model.enums.StatusSolicitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long> {

    List<Solicitacao> findByUsuarioIdOrderByCreatedAtDesc(Long usuarioId);

    Optional<Solicitacao> findByProtocolo(String protocolo);

    List<Solicitacao> findByStatus(StatusSolicitacao status);

    List<Solicitacao> findByUsuarioIdAndStatus(Long usuarioId, StatusSolicitacao status);

    long countByStatus(StatusSolicitacao status);

    long countByUsuarioId(Long usuarioId);
}
