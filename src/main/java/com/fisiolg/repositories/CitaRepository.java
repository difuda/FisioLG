package com.fisiolg.repositories;

import com.fisiolg.entities.Cita;
import com.fisiolg.entities.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {


    List<Cita> findByEstado(EstadoCita estado);


    List<Cita> findByFechaHoraBetween(LocalDateTime start, LocalDateTime end);


    long countByEstado(EstadoCita estado);


    List<Cita> findByPacienteIdOrderByFechaHoraDesc(Long pacienteId);


    boolean existsByFechaHoraAndFisioId(LocalDateTime fechaHora, Long fisioId);


    List<Cita> findByEstadoAndRecordatorioEnviadoFalseAndFechaHoraBetween(EstadoCita estado, LocalDateTime inicio, LocalDateTime fin);



}