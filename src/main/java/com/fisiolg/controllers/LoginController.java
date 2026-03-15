package com.fisiolg.controllers;

import com.fisiolg.entities.User;
import com.fisiolg.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/login")
@CrossOrigin(origins = "*")
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/entrar")
    public Map<String, Object> login(@RequestBody Map<String, String> datos) {

        String email = datos.get("email");
        String pass = datos.get("password");


        Optional<User> userOpt = userRepository.findByEmailAndPassword(email, pass);

        if (userOpt.isPresent()) {
            User u = userOpt.get();


            return Map.of(
                    "status", "success",
                    "id", u.getId(),
                    "rol", u.getRol(),
                    "email", u.getEmail(),
                    "mensaje", "Bienvenido/a al sistema"
            );
        } else {

            return Map.of(
                    "status", "error",
                    "message", "Credenciales incorrectas"
            );
        }
    }
}
