package com.fisiolg.controllers;

import com.fisiolg.entities.Paciente;
import com.fisiolg.repositories.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
@CrossOrigin(origins = "*")
public class PacienteController {

    @Autowired
    private PacienteRepository pacienteRepository;

    @GetMapping
    public List<Paciente> listarPacientes() {
        return pacienteRepository.findAll();
    }

    @PostMapping
    public Paciente guardar(@RequestBody Paciente paciente) {
        return pacienteRepository.save(paciente);
    }
}
