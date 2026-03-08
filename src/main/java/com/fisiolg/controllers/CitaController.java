package com.fisiolg.controllers;

import com.fisiolg.repositories.PacienteRepository;
import com.fisiolg.services.AgendaService;
import com.fisiolg.entities.Cita;
import com.fisiolg.entities.EstadoCita;
import com.fisiolg.repositories.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/citas")
@CrossOrigin(origins = "*")
public class CitaController {

    @Autowired
    private CitaRepository citaRepository;
    @Autowired
    private AgendaService agendaService;
    @Autowired
    private PacienteRepository pacienteRepository;

    @GetMapping("/admin/todas")
    public List<Map<String, Object>> getTodasParaAdmin() {
        List<Cita> lista = citaRepository.findAll();
        List<Map<String, Object>> eventos = new ArrayList<>();

        for (Cita c : lista) {
            Map<String, Object> e = new HashMap<>();
            e.put("id", c.getId());
            e.put("fisioId", c.getFisioId());
            e.put("estado", c.getEstado());
            e.put("notasClinicas", c.getNotasClinicas());


            String nombreFinal = "RESERVADA";


            if (c.getNotasClinicas() != null && !c.getNotasClinicas().isEmpty()) {
                nombreFinal = c.getNotasClinicas().toUpperCase();
            }

            else if (c.getPacienteId() != null) {
                nombreFinal = pacienteRepository.findById(c.getPacienteId())
                        .map(p -> p.getNombre().toUpperCase() + " " + (p.getApellidos() != null ? p.getApellidos().toUpperCase() : ""))
                        .orElse("PACIENTE ID: " + c.getPacienteId());
            }

            e.put("pacienteNombre", nombreFinal);

            if (c.getFechaHora() != null) {
                e.put("fechaHora", c.getFechaHora().toString());
            }
            eventos.add(e);
        }
        return eventos;
    }

    @GetMapping("/disponibles")
    public List<Map<String, Object>> getCitasDisponibles() {
        return citaRepository.findAll().stream()
                .filter(c -> c.getEstadoId() == EstadoCita.LIBRE)
                .map(c -> {
                    Map<String, Object> e = new HashMap<>();
                    e.put("id", c.getId());
                    e.put("title", "DISPONIBLE");
                    e.put("start", c.getFechaHora().toString());
                    e.put("color", "#28a745");
                    return e;
                })
                .collect(Collectors.toList());
    }


    @PutMapping("/actualizar/{id}")
    public Cita actualizarCita(@PathVariable Long id, @RequestBody Map<String, Object> datos) {
        return citaRepository.findById(id).map(cita -> {

            if (datos.containsKey("estado")) {

                cita.setEstado(EstadoCita.CONFIRMADA.name());
                cita.setEstadoId(EstadoCita.CONFIRMADA);
            }

            if (datos.containsKey("pacienteId")) {
                cita.setPacienteId(Long.valueOf(datos.get("pacienteId").toString()));
            }

            if (datos.containsKey("notasClinicas")) {
                cita.setNotasClinicas((String) datos.get("notasClinicas"));
            }

            if (datos.containsKey("fisioId")) {
                cita.setFisioId(Long.valueOf(datos.get("fisioId").toString()));
            }

            return citaRepository.save(cita);

        }).orElseThrow(() -> new RuntimeException("No se encontró la cita con ID: " + id));
    }

    @PostMapping("/admin/citas/asignar")
    public String asignarProfesional(@RequestParam Long citaId, @RequestParam Long fisioId) {
        Cita cita = citaRepository.findById(citaId).orElseThrow();


        cita.setFisioId(fisioId);
        cita.setEstado(EstadoCita.CONFIRMADA.name());

        citaRepository.save(cita);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/generar-jornada")
    public Map<String, String> generarJornada(@RequestParam("fecha") String fechaStr) {
        Map<String, String> response = new HashMap<>();
        try {
            java.time.LocalDate fecha = java.time.LocalDate.parse(fechaStr);
            agendaService.generarJornadaCompleta(fecha);

            response.put("status", "ok");
            response.put("message", "Citas de 40 min generadas correctamente para el " + fecha);
            return response;
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error: " + e.getMessage());
            return response;
        }
    }

    @PutMapping("/reservar-manual/{id}")
    public Cita reservarManual(@PathVariable Long id, @RequestParam String nombre) {
        System.out.println("Procesando cita ID: " + id + " (Nombre recibido: '" + nombre + "')");

        return citaRepository.findById(id).map(cita -> {
            try {

                if (nombre == null || nombre.trim().isEmpty()) {
                    // Lógica para LIBERAR la cita
                    cita.setNotasClinicas(null);
                    cita.setEstado("LIBRE");
                    cita.setEstadoId(EstadoCita.LIBRE);
                    System.out.println("♻️ Cita anulada y liberada");
                } else {

                    cita.setNotasClinicas(nombre.toUpperCase());
                    cita.setEstado("CONFIRMADA");
                    cita.setEstadoId(EstadoCita.CONFIRMADA);
                    System.out.println("✅ Cita reservada para: " + nombre.toUpperCase());
                }

                return citaRepository.save(cita);

            } catch (Exception e) {
                System.err.println("❌ Error al procesar en DB: " + e.getMessage());
                throw new RuntimeException("Error interno al guardar: " + e.getMessage());
            }
        }).orElseThrow(() -> new RuntimeException("Cita con ID " + id + " no encontrada"));
    }
}

