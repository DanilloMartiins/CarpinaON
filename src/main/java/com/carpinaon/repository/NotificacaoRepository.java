package com.carpinaon.repository;

import com.carpinaon.model.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {

    List<Notificacao> findByUsuarioIdOrderByCreatedAtDesc(Long usuarioId);

    List<Notificacao> findByUsuarioIdAndLidaFalse(Long usuarioId);

    long countByUsuarioIdAndLidaFalse(Long usuarioId);
}
