package com.carpinaon.repository;

import com.carpinaon.model.HistoricoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricoStatusRepository extends JpaRepository<HistoricoStatus, Long> {

    List<HistoricoStatus> findBySolicitacaoIdOrderByChangedAtDesc(Long solicitacaoId);
}
