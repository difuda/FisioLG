package com.fisiolg.repositories;

import com.fisiolg.entities.EstadoCita;
import com.fisiolg.entities.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    boolean existsByFechaHoraAndFisioId(LocalDateTime fechaHora, Long fisioId);
    List<Cita> findByEstado(EstadoCita estado);
    List<Cita> findByFisioId(Long fisioId);
    List<Cita> findByFisioIdAndEstado(Long fisioId, EstadoCita estado);
}