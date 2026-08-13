package com.carpinaon.repository;

import com.carpinaon.model.EventoTurismo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventoTurismoRepository extends JpaRepository<EventoTurismo, Long> {

    List<EventoTurismo> findByCategoriaIgnoreCase(String categoria);

    List<EventoTurismo> findByDataInicioBetween(LocalDateTime inicio, LocalDateTime fim);

    List<EventoTurismo> findByLocalContainingIgnoreCase(String local);
}
