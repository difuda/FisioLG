package com.fisiolg.repositories;

import com.fisiolg.entities.Turno;
import com.fisiolg.entities.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDateTime;



@Repository
public interface TurnoRepository extends JpaRepository<Turno, Long> {

    List<Turno> findByEstado(EstadoCita estado);

    List<Turno> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);

    List<Turno> findByFisioId(Long fisioId);

}
