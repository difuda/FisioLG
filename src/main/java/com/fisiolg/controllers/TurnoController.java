package com.fisiolg.controllers;

import com.fisiolg.entities.Turno;
import com.fisiolg.entities.EstadoCita;
import com.fisiolg.repositories.TurnoRepository;
import com.fisiolg.repositories.FisioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestionar la agenda y los turnos de la clínica.
 */
@RestController
@RequestMapping("/api/turnos")
// TODO: Para producción, cambiar "*" por el dominio específico del frontend para asegurar la protección de datos.
@CrossOrigin(origins = "*")
public class TurnoController {

    @Autowired
    private TurnoRepository turnoRepository;

    @Autowired
    private FisioRepository fisioRepository;

    /**
     * Obtiene la lista de turnos que actualmente se encuentran libres.
     */
    @GetMapping("/libres")
    public ResponseEntity<?> listarLibres() {
        try {
            List<Turno> libres = turnoRepository.findByEstado(EstadoCita.LIBRE);
            return ResponseEntity.ok(libres);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error interno al listar turnos libres"));
        }
    }

    /**
     * Obtiene la agenda completa en un rango de fechas específico.
     */
    @GetMapping("/agenda")
    public ResponseEntity<?> listarPorRango(
            @RequestParam String inicio,
            @RequestParam String fin) {
        try {
            List<Turno> agenda = turnoRepository.findByFechaHoraBetween(
                    LocalDateTime.parse(inicio),
                    LocalDateTime.parse(fin));
            return ResponseEntity.ok(agenda);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", "Formato de fecha incorrecto o error interno: " + e.getMessage()));
        }
    }

    /**
     * Genera una nueva jornada estructurando los bloques de tiempo (ej. 40 minutos).
     */
    @PostMapping("/generar-jornada")
    public ResponseEntity<?> generarJornada(@RequestBody List<Turno> turnos) {
        try {
            for (Turno t : turnos) {
                t.setEstado(EstadoCita.LIBRE);
            }
            List<Turno> guardados = turnoRepository.saveAll(turnos);
            return ResponseEntity.ok(guardados);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error al generar la jornada: " + e.getMessage()));
        }
    }



    /**
     * Acción ejecutada por el cliente para reservar una cita en un hueco disponible.
     */
    @PutMapping("/{id}/reservar")
    public ResponseEntity<Turno> reservarTurno(@PathVariable Long id, @RequestBody Turno datosReserva) {
        return turnoRepository.findById(id).map(turno -> {
            if (turno.getEstado() != EstadoCita.LIBRE) {
                return ResponseEntity.badRequest().<Turno>build();
            }
            turno.setEstado(EstadoCita.CONFIRMADA);
            turno.setPaciente(datosReserva.getPaciente());
            turno.setMotivo(datosReserva.getMotivo());
            return ResponseEntity.ok(turnoRepository.save(turno));
        }).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Anula una reserva existente, liberando el hueco en el calendario.
     */
    @PutMapping("/{id}/anular")
    public ResponseEntity<Turno> anularReserva(@PathVariable Long id) {
        return turnoRepository.findById(id).map(turno -> {
            turno.setEstado(EstadoCita.LIBRE);
            turno.setPaciente(null);
            turno.setMotivo(null);
            turno.setFisio(null);
            return ResponseEntity.ok(turnoRepository.save(turno));
        }).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Acción interna para adjudicar un profesional específico a un turno ya creado o reservado.
     */
    @PutMapping("/{id}/adjudicar")
    public ResponseEntity<Turno> adjudicarFisio(@PathVariable Long id, @RequestParam Long fisioId) {
        return turnoRepository.findById(id).map(turno -> {
            return fisioRepository.findById(fisioId).map(fisio -> {
                turno.setFisio(fisio);
                return ResponseEntity.ok(turnoRepository.save(turno));
            }).orElse(ResponseEntity.notFound().build());
        }).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina un turno del sistema de forma permanente.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            turnoRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Turno eliminado correctamente"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error al eliminar el turno: " + e.getMessage()));
        }
    }
}