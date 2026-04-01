package com.fisiolg.repositories;

import com.fisiolg.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositorio para gestionar las operaciones de base de datos de las cuentas de Usuario (credenciales de acceso).
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca un usuario por su nombre de usuario (username).
     */
    Optional<User> findByUsername(String username);

    /**
     * Busca un usuario utilizando su dirección de correo electrónico.
     * Útil para validar existencias o iniciar procesos de recuperación de cuenta.
     */
    Optional<User> findByEmail(String email);

    /**
     * Valida las credenciales de un usuario comprobando que el email y la contraseña coinciden exactamente.
     * Es el método principal utilizado en el proceso de inicio de sesión (Login).
     */
    Optional<User> findByEmailAndPassword(String email, String password);

    /**
     * Busca un usuario a partir de un token de recuperación de contraseña específico.
     * Fundamental para validar que el enlace enviado por correo es legítimo antes de permitir el cambio de clave.
     */
    Optional<User> findByTokenRecuperacion(String token);

    /**
     * Comprueba rápidamente si ya existe una cuenta de usuario registrada con un correo electrónico concreto.
     */
    boolean existsByEmail(String email);
}
