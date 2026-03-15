package com.fisiolg.repositories;

import com.fisiolg.entities.Fisio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface FisioRepository extends JpaRepository<Fisio, Long> {


    Optional<Fisio> findByUsuarioId(Long usuarioId);


    Optional<Fisio> findByNombre(String nombre);
}