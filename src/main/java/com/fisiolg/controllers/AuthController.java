package com.fisiolg.controllers;

import com.fisiolg.entities.User;
import com.fisiolg.repositories.UserRepository;
import com.fisiolg.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestParam String email) {

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setUsername(email);
                    newUser.setPassword("OTP_USER");
                    newUser.setRol("INVITADO");
                    return newUser;
                });


        String otp = String.format("%06d", new Random().nextInt(1000000));


        user.setTokenRecuperacion(otp);
        userRepository.save(user);


        emailService.enviarCodigo(email, otp);

        return ResponseEntity.ok(Map.of("status", "success", "message", "Código enviado"));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String code) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();


            if (user.getTokenRecuperacion() != null && user.getTokenRecuperacion().equals(code)) {

                user.setTokenRecuperacion(null); // Limpiamos el código tras usarlo
                userRepository.save(user);

                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "Verificación exitosa",
                        "rol", user.getRol()
                ));
            }
        }
        return ResponseEntity.status(401).body(Map.of("status", "error", "message", "Código incorrecto"));
    }
}