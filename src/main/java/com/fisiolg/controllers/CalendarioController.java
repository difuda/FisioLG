package com.fisiolg.controllers;

import com.fisiolg.entities.Cita;
import com.fisiolg.entities.EstadoCita;
import com.fisiolg.repositories.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

        List<Cita> citasDelDia = citaRepository.findAll().stream()
                .filter(c -> c.getFechaHora().toLocalDate().toString().equals(fecha))
                .collect(Collectors.toList());


        List<String> todosLosHuecos = Arrays.asList(
                "15:30", "16:10", "16:50", "17:30", "18:10", "18:50", "19:30", "20:10"
        );

        List<String> disponibles = new ArrayList<>();

        for (String hora : todosLosHuecos) {
            LocalTime horaHueco = LocalTime.parse(hora);


            long citasOcupadas = citasDelDia.stream()
                    .filter(c -> c.getFechaHora().toLocalTime().equals(horaHueco))
                    .filter(c -> c.getEstadoId() != EstadoCita.LIBRE)
                    .count();


            if (citasOcupadas < 2) {
                disponibles.add(hora);
            }
        }

        return disponibles;
    }
}
