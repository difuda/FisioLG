package com.fisiolg.controllers;

import com.fisiolg.entities.Paciente;
import com.fisiolg.repositories.PacienteRepository;
import com.fisiolg.services.PacienteService;
import com.fisiolg.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pacientes")
@CrossOrigin(origins = "*")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PacienteRepository pacienteRepository;


    @GetMapping
    public List<Paciente> listarPacientes() {
        return pacienteService.listarTodos();
    }


    @GetMapping("/buscar")
    public ResponseEntity<List<Paciente>> buscarPacientes(@RequestParam String term) {
        // Asegúrate de que tu PacienteRepository tenga este método exacto
        List<Paciente> resultados = pacienteRepository.findByNombreContainingIgnoreCaseOrDniContainingIgnoreCase(term, term);
        return ResponseEntity.ok(resultados);
    }


    @PostMapping("/registro")
    public ResponseEntity<Paciente> guardar(@RequestBody Paciente paciente) {
        Paciente guardado = pacienteService.registrarOActualizar(paciente);
        return ResponseEntity.ok(guardado);
    }


    @PostMapping("/olvido-password")
    public ResponseEntity<?> solicitarRecuperacion(@RequestBody Map<String, String> request) {
        String email = request.get("email");


        String token = pacienteService.generarTokenRecuperacion(email);

        if (token != null) {
            // URL de tu frontend para recuperar pass
            String enlace = "http://localhost:8080/restablecer-password.html?token=" + token;

            emailService.enviarCorreo(email, "Recuperar Contraseña - FisioLG",
                    "Hola. Has solicitado restablecer tu contraseña. Haz clic aquí: " + enlace);

            return ResponseEntity.ok(Map.of("message", "Email enviado con éxito"));
        }
        return ResponseEntity.status(404).body(Map.of("error", "El email no está registrado"));
    }


    @PostMapping("/restablecer-password")
    public ResponseEntity<?> restablecer(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String nuevaPass = request.get("password");

        boolean exito = pacienteService.completarRecuperacion(token, nuevaPass);
        if (exito) {
            return ResponseEntity.ok(Map.of("message", "Contraseña cambiada correctamente"));
        }
        return ResponseEntity.badRequest().body(Map.of("error", "Token inválido o caducado"));
    }


    @GetMapping("/{id}")
    public ResponseEntity<Paciente> obtenerPerfil(@PathVariable Long id) {
        return pacienteRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
