package com.fisiolg.repositories;

import com.fisiolg.entities.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para gestionar las operaciones de base de datos de los pacientes (clientes).
 */
@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    /**
     * Busca un paciente por su Documento Nacional de Identidad exacto.
     * Útil para la validación durante el registro y evitar duplicidades.
     */
    Optional<Paciente> findByDni(String dni);

    /**
     * Busca un paciente a partir del ID de su cuenta de usuario vinculada.
     * Fundamental para identificar al cliente tras el login y proteger el acceso a sus datos personales.
     */
    Optional<Paciente> findByUsuarioId(Long usuarioId);

    /**
     * Comprueba rápidamente si ya existe un paciente registrado con un DNI concreto.
     */
    boolean existsByDni(String dni);

    /**
     * Realiza una búsqueda flexible de pacientes que contengan un término específico en su nombre o en su DNI.
     * Ignora mayúsculas y minúsculas para facilitar la búsqueda desde el frontend.
     */
    List<Paciente> findByNombreContainingIgnoreCaseOrDniContainingIgnoreCase(String nombre, String dni);

    /**
     * Busca un paciente por su dirección de correo electrónico.
     * Clave para el proceso de inicio de sesión y el flujo de recuperación de contraseñas olvidadas.
     */
    Optional<Paciente> findByEmail(String email);
}