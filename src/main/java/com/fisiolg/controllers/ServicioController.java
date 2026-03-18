package com.fisiolg.controllers;

import com.fisiolg.entities.Servicio;
import com.fisiolg.repositories.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/servicios")
public class ServicioController {

    @Autowired
    private ServicioRepository servicioRepository;

    @GetMapping
    public List<Servicio> obtenerTodos() {
        return servicioRepository.findAll();
    }
}
