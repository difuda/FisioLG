package com.fisiolg.controllers;

import com.fisiolg.entities.Turno;
import com.fisiolg.entities.EstadoCita;
import com.fisiolg.repositories.TurnoRepository;
import com.fisiolg.repositories.FisioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/turnos")
@CrossOrigin(origins = "*")
public class TurnoController {

    @Autowired
    private TurnoRepository turnoRepository;

    @Autowired
    private FisioRepository fisioRepository;


    @GetMapping("/libres")
    public List<Turno> listarLibres() {
        return turnoRepository.findByEstado(EstadoCita.LIBRE);
    }


    @GetMapping("/agenda")
    public List<Turno> listarPorRango(
            @RequestParam String inicio,
            @RequestParam String fin) {
        return turnoRepository.findByFechaHoraBetween(
                LocalDateTime.parse(inicio),
                LocalDateTime.parse(fin));
    }


    @PostMapping("/generar-jornada")
    public List<Turno> generarJornada(@RequestBody List<Turno> turnos) {
        for (Turno t : turnos) {
            t.setEstado(EstadoCita.LIBRE);
        }
        return turnoRepository.saveAll(turnos);
    }


    @PutMapping("/{id}/reservar")
    public Turno reservarTurno(@PathVariable Long id, @RequestBody Turno datosReserva) {
        return turnoRepository.findById(id).map(turno -> {
            if (turno.getEstado() != EstadoCita.LIBRE) {
                throw new RuntimeException("Este hueco ya no está disponible");
            }
            turno.setEstado(EstadoCita.CONFIRMADA);
            turno.setPaciente(datosReserva.getPaciente());
            turno.setMotivo(datosReserva.getMotivo());
            return turnoRepository.save(turno);
        }).orElseThrow(() -> new RuntimeException("Turno no encontrado"));
    }


    @PutMapping("/{id}/anular")
    public Turno anularReserva(@PathVariable Long id) {
        return turnoRepository.findById(id).map(turno -> {
            turno.setEstado(EstadoCita.LIBRE);
            turno.setPaciente(null);
            turno.setMotivo(null);
            turno.setFisio(null);
            return turnoRepository.save(turno);
        }).orElseThrow(() -> new RuntimeException("No se encontró el turno"));
    }


    @PutMapping("/{id}/adjudicar")
    public Turno adjudicarFisio(@PathVariable Long id, @RequestParam Long fisioId) {
        return turnoRepository.findById(id).map(turno -> {
            return fisioRepository.findById(fisioId).map(fisio -> {
                turno.setFisio(fisio);
                return turnoRepository.save(turno);
            }).orElseThrow(() -> new RuntimeException("Fisio no encontrado"));
        }).orElseThrow(() -> new RuntimeException("Turno no encontrado"));
    }


    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        turnoRepository.deleteById(id);
    }
}