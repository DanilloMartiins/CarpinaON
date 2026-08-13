package com.carpinaon.repository;

import com.carpinaon.model.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {

    List<Servico> findByCategoriaId(Long categoriaId);

    List<Servico> findByAtivoTrue();

    List<Servico> findByCategoriaIdAndAtivoTrue(Long categoriaId);

    List<Servico> findByNomeContainingIgnoreCase(String nome);
}
