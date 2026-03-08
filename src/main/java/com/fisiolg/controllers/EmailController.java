package com.fisiolg.controllers;

import com.fisiolg.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/mail")
@CrossOrigin(origins = "*")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/enviar-codigo")
    public void enviar(@RequestBody Map<String, String> payload) {
        String email = payload.get("destinatario");
        String codigo = payload.get("codigo");

        emailService.enviarCodigo(email, codigo);
    }
}