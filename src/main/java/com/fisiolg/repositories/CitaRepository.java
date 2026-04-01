package com.fisiolg.repositories;

import com.fisiolg.entities.Cita;
import com.fisiolg.entities.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para gestionar las operaciones de base de datos de la entidad Cita.
 */
@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    /**
     * Obtiene una lista de citas filtradas por su estado actual (ej. CONFIRMADA, LIBRE, CANCELADA).
     */
    List<Cita> findByEstado(EstadoCita estado);

    /**
     * Obtiene todas las citas comprendidas dentro de un rango de fechas específico, útil para mostrar la agenda.
     */
    List<Cita> findByFechaHoraBetween(LocalDateTime start, LocalDateTime end);

    /**
     * Cuenta el número total de citas que se encuentran en un estado determinado (útil para estadísticas).
     */
    long countByEstado(EstadoCita estado);

    /**
     * Obtiene el historial completo de citas de un paciente específico, ordenado de más reciente a más antigua.
     */
    List<Cita> findByPacienteIdOrderByFechaHoraDesc(Long pacienteId);

    /**
     * Comprueba si un profesional específico ya tiene una cita asignada en una fecha y hora exactas.
     * Fundamental para evitar solapamientos en los bloques de 40 minutos.
     */
    boolean existsByFechaHoraAndFisioId(LocalDateTime fechaHora, Long fisioId);

    /**
     * Busca citas en un estado y rango de tiempo específicos a las que aún no se les ha enviado el recordatorio por correo.
     * Ideal para tareas de notificación automática.
     */
    List<Cita> findByEstadoAndRecordatorioEnviadoFalseAndFechaHoraBetween(EstadoCita estado, LocalDateTime inicio, LocalDateTime fin);

}