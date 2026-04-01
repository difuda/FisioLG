package com.fisiolg.repositories;

import com.fisiolg.entities.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para gestionar las operaciones de base de datos del catálogo de Servicios.
 * Al extender de JpaRepository, hereda automáticamente las operaciones CRUD básicas (guardar, buscar, listar, eliminar).
 */
@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {

    // Actualmente no se requieren consultas personalizadas, ya que el catálogo se lista completo.

}