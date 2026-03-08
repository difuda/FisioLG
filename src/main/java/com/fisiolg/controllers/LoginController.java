package com.fisiolg.controllers;

import com.fisiolg.entities.Fisioterapeuta;
import com.fisiolg.repositories.FisioterapeutaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/login")
@CrossOrigin(origins = "*")
public class LoginController {

    @Autowired
    private FisioterapeutaRepository fisioRepository;

    @PostMapping("/entrar")
    public Map<String, Object> login(@RequestBody Map<String, String> datos) {
        String nombre = datos.get("usuario");
        String pass = datos.get("password");


        Optional<Fisioterapeuta> fisio = fisioRepository.findByNombreAndPassword(nombre, pass);

        if (fisio.isPresent()) {
            return Map.of(
                    "status", "success",
                    "rol", fisio.get().getRol(),
                    "id", fisio.get().getId(),
                    "nombre", fisio.get().getNombre()
            );
        } else {
            return Map.of("status", "error", "message", "Usuario o clave incorrectos");
        }
    }
}
