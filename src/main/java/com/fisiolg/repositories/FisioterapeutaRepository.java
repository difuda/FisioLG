package com.fisiolg.repositories;

import com.fisiolg.entities.Fisioterapeuta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FisioterapeutaRepository extends JpaRepository<Fisioterapeuta, Long> {


    Optional<Fisioterapeuta> findByNombreAndPassword(String nombre, String password);
}