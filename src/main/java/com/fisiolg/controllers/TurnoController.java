package com.fisiolg.controllers;

import com.fisiolg.entities.Turno;
import com.fisiolg.repositories.TurnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/turnos")
@CrossOrigin(origins = "*")
public class TurnoController {

    @Autowired
    private TurnoRepository turnoRepository;

    @GetMapping
    public List<Turno> listarTurnos() {
        return turnoRepository.findAll();
    }


    @PostMapping
    public Turno guardar(@RequestBody Turno turno) {
        return turnoRepository.save(turno);
    }


    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        turnoRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public Turno actualizar(@PathVariable Long id, @RequestBody Turno turnoDetalles) {
        return turnoRepository.findById(id).map(turno -> {
            turno.setMotivo(turnoDetalles.getMotivo());
            turno.setFechaHora(turnoDetalles.getFechaHora());
            turno.setEstado(turnoDetalles.getEstado());
            return turnoRepository.save(turno);
        }).orElseThrow(() -> new RuntimeException("No se encontró el turno con ID: " + id));
    }
}