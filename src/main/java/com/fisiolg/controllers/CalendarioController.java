package com.fisiolg.controllers;

import com.fisiolg.entities.Cita;
import com.fisiolg.entities.EstadoCita;
import com.fisiolg.repositories.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/calendario")
@CrossOrigin(origins = "*")
public class CalendarioController {

    @Autowired
    private CitaRepository citaRepository;


    @GetMapping("/huecos")
    public List<String> getHuecosDisponibles(@RequestParam String fecha) {
        LocalDate fechaBusqueda = LocalDate.parse(fecha);


        List<Cita> citasDelDia = citaRepository.findAll().stream()
                .filter(c -> c.getFechaHora().toLocalDate().equals(fechaBusqueda))
                .collect(Collectors.toList());


        List<String> todosLosHuecos = Arrays.asList(
                "15:30", "16:10", "16:50", "17:30", "18:10", "18:50", "19:30", "20:10"
        );

        List<String> disponibles = new ArrayList<>();

        for (String horaStr : todosLosHuecos) {
            LocalTime horaHueco = LocalTime.parse(horaStr);


            long ocupadas = citasDelDia.stream()
                    .filter(c -> c.getFechaHora().toLocalTime().equals(horaHueco))
                    .filter(c -> c.getEstado() != EstadoCita.LIBRE)
                    .count();


            if (ocupadas < 2) {
                disponibles.add(horaStr);
            }
        }

        return disponibles;
    }


    @GetMapping("/eventos")
    public List<Map<String, Object>> getEventos() {
        List<Cita> lista = citaRepository.findAll();
        List<Map<String, Object>> eventos = new ArrayList<>();

        for (Cita c : lista) {
            try {
                Map<String, Object> e = new HashMap<>();
                e.put("id", c.getId());
                e.put("start", c.getFechaHora().toString());


                String estadoTexto = "DISPONIBLE";
                if (c.getEstado() != null) {
                    estadoTexto = (c.getEstado() == EstadoCita.CONFIRMADA) ? "CONFIRMADA" :
                            (c.getEstado() == EstadoCita.CONFIRMADA) ? "RESERVADO" : "DISPONIBLE";
                }
                e.put("estado", estadoTexto);


                String titulo = "LIBRE";
                if (c.getPaciente() != null) {
                    titulo = (c.getPaciente().getNombre() + " " + c.getPaciente().getApellidos()).toUpperCase();
                } else if (c.getNotas() != null && !c.getNotas().isEmpty()) {
                    titulo = c.getNotas().toUpperCase();
                }

                e.put("title", titulo);
                e.put("fisioId", c.getFisio() != null ? c.getFisio().getId() : null);

                eventos.add(e);
            } catch (Exception ex) {
                System.err.println("Error procesando evento cita " + c.getId() + ": " + ex.getMessage());
            }
        }
        return eventos;
    }
}
