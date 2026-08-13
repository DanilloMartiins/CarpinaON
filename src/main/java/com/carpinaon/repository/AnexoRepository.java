package com.carpinaon.repository;

import com.carpinaon.model.Anexo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnexoRepository extends JpaRepository<Anexo, Long> {

    List<Anexo> findBySolicitacaoId(Long solicitacaoId);
}
