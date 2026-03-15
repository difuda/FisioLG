package com.fisiolg.controllers;

import com.fisiolg.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/mail")
@CrossOrigin(origins = "*")
public class EmailController {

    @Autowired
    private EmailService emailService;


    @PostMapping("/enviar-codigo")
    public ResponseEntity<?> enviar(@RequestBody Map<String, String> payload) {
        try {
            String email = payload.get("email");
            String codigo = payload.get("codigo");

            if (email == null || codigo == null) {
                return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "Faltan datos"));
            }

            emailService.enviarCodigo(email, codigo);

            return ResponseEntity.ok(Map.of("status", "success", "message", "Código enviado a " + email));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("status", "error", "message", "Error al enviar el mail: " + e.getMessage()));
        }
    }


    @PostMapping("/notificar-cita")
    public ResponseEntity<?> notificarCita(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String mensaje = payload.get("mensaje");

        emailService.enviarCorreo(email, "Confirmación de Cita - FisioLG", mensaje);

        return ResponseEntity.ok(Map.of("status", "success", "message", "Notificación enviada"));
    }
}