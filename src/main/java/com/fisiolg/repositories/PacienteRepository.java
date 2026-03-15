package com.fisiolg.repositories;

import com.fisiolg.entities.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    Optional<Paciente> findByDni(String dni);

    Optional<Paciente> findByUsuarioId(Long usuarioId);

    boolean existsByDni(String dni);

    List<Paciente> findByNombreContainingIgnoreCaseOrDniContainingIgnoreCase(String nombre, String dni);


    Optional<Paciente> findByEmail(String email);
}
