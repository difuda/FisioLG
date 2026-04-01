package com.fisiolg.repositories;

import com.fisiolg.entities.Fisio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositorio para gestionar las operaciones de base de datos de los profesionales (Fisioterapeutas).
 */
@Repository
public interface FisioRepository extends JpaRepository<Fisio, Long> {

    /**
     * Busca un fisioterapeuta a partir del ID de su cuenta de usuario vinculada.
     * Fundamental para identificar al profesional una vez que ha iniciado sesión en el sistema y gestionar su privacidad.
     */
    Optional<Fisio> findByUsuarioId(Long usuarioId);

    /**
     * Busca un fisioterapeuta por su nombre exacto.
     * Útil para búsquedas internas o para la adjudicación manual de turnos.
     */
    Optional<Fisio> findByNombre(String nombre);
}