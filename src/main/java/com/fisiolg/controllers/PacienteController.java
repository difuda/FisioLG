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

/**
 * Controlador REST para gestionar la información, registro y recuperación de contraseñas de los pacientes.
 */
@RestController
@RequestMapping("/api/pacientes")
// TODO: Para producción, cambiar "*" por el dominio específico del frontend para asegurar la protección de datos.
@CrossOrigin(origins = "*")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PacienteRepository pacienteRepository;

    /**
     * Obtiene la lista completa de pacientes.
     */
    @GetMapping
    public ResponseEntity<?> listarPacientes() {
        try {
            return ResponseEntity.ok(pacienteService.listarTodos());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error interno al listar los pacientes"));
        }
    }

    /**
     * Busca pacientes por coincidencias en su nombre o DNI.
     */
    @GetMapping("/buscar")
    public ResponseEntity<?> buscarPacientes(@RequestParam String term) {
        try {
            if (term == null || term.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "El término de búsqueda no puede estar vacío"));
            }
            List<Paciente> resultados = pacienteRepository.findByNombreContainingIgnoreCaseOrDniContainingIgnoreCase(term, term);
            return ResponseEntity.ok(resultados);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error interno al buscar pacientes"));
        }
    }

    /**
     * Registra un nuevo paciente o actualiza los datos de uno existente.
     */
    @PostMapping("/registro")
    public ResponseEntity<?> guardar(@RequestBody Paciente paciente) {
        try {
            Paciente guardado = pacienteService.registrarOActualizar(paciente);
            return ResponseEntity.ok(guardado);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error al guardar el paciente: " + e.getMessage()));
        }
    }

    /**
     * Solicita la recuperación de contraseña generando un token y enviando un enlace al correo.
     */
    @PostMapping("/olvido-password")
    public ResponseEntity<?> solicitarRecuperacion(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "El email es obligatorio"));
            }

            String token = pacienteService.generarTokenRecuperacion(email);

            if (token != null) {
                // TODO: Cambiar localhost por la URL real del servidor en producción
                String enlace = "http://localhost:8080/restablecer-password.html?token=" + token;

                emailService.enviarCorreo(email, "Recuperar Contraseña - Fisioterapia y Osteopatía Lucía Garza",
                        "Hola. Has solicitado restablecer tu contraseña. Haz clic aquí: " + enlace);

                return ResponseEntity.ok(Map.of("message", "Email enviado con éxito"));
            }
            return ResponseEntity.status(404).body(Map.of("error", "El email no está registrado"));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error al procesar la solicitud de recuperación"));
        }
    }

    /**
     * Restablece la contraseña del paciente utilizando el token de recuperación.
     */
    @PostMapping("/restablecer-password")
    public ResponseEntity<?> restablecer(@RequestBody Map<String, String> request) {
        try {
            String token = request.get("token");
            String nuevaPass = request.get("password");

            if (token == null || nuevaPass == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Faltan datos para restablecer la contraseña"));
            }

            boolean exito = pacienteService.completarRecuperacion(token, nuevaPass);
            if (exito) {
                return ResponseEntity.ok(Map.of("message", "Contraseña cambiada correctamente"));
            }
            return ResponseEntity.badRequest().body(Map.of("error", "Token inválido o caducado"));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error interno al restablecer la contraseña"));
        }
    }

    /**
     * Obtiene el perfil detallado de un paciente específico mediante su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPerfil(@PathVariable Long id) {
        try {
            return pacienteRepository.findById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error interno al obtener el perfil"));
        }
    }
}