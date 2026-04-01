package com.fisiolg.repositories;

import com.fisiolg.entities.Turno;
import com.fisiolg.entities.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDateTime;

/**
 * Repositorio para gestionar las operaciones de base de datos de los Turnos (los bloques de la agenda).
 */
@Repository
public interface TurnoRepository extends JpaRepository<Turno, Long> {

    /**
     * Obtiene una lista de turnos filtrados por su estado actual.
     * Esencial para mostrar al cliente únicamente los huecos en estado LIBRE.
     */
    List<Turno> findByEstado(EstadoCita estado);

    /**
     * Obtiene todos los turnos comprendidos dentro de un rango de fechas específico.
     * Se utiliza para construir la vista de la agenda general en la clínica.
     */
    List<Turno> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Obtiene todos los turnos que han sido adjudicados a un fisioterapeuta en concreto.
     * Fundamental para que cada profesional visualice de forma privada su propia carga de trabajo.
     */
    List<Turno> findByFisioId(Long fisioId);

    /**
     * Obtiene los turnos de un paciente específico que son iguales o posteriores a una fecha dada.
     * Útil para que el paciente solo vea en su panel sus "Próximas citas" y no el historial pasado.
     */
    List<Turno> findByPacienteIdAndFechaHoraAfterOrderByFechaHoraAsc(Long pacienteId, LocalDateTime fechaHora);

}