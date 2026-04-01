package com.fisiolg.controllers;

import com.fisiolg.entities.User;
import com.fisiolg.entities.Paciente;
import com.fisiolg.repositories.UserRepository;
import com.fisiolg.repositories.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador REST para gestionar la autenticación y el acceso al sistema FisioLG.
 */
@RestController
@RequestMapping("/api/login")
// TODO: Para producción, cambiar "*" por el dominio específico del frontend para asegurar la protección de datos.
@CrossOrigin(origins = "*")
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    /**
     * Endpoint para procesar el inicio de sesión de los clientes.
     * @param datos Mapa que debe contener las credenciales 'email' y 'password'.
     * @return ResponseEntity con el estado de la operación y los datos del usuario si es correcto.
     */
    @PostMapping("/entrar")
    public ResponseEntity<?> login(@RequestBody Map<String, String> datos) {
        try {
            String email = datos.get("email");
            String pass = datos.get("password");

            // Validación básica para evitar NullPointerException
            if (email == null || pass == null || email.isEmpty() || pass.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "error",
                        "message", "Faltan credenciales"
                ));
            }

            // Búsqueda del usuario en la base de datos
            Optional<User> userOpt = userRepository.findByEmailAndPassword(email, pass);

            if (userOpt.isPresent()) {
                User u = userOpt.get();

                // Obtenemos el nombre real del paciente vinculado a ese usuario
                String nombrePaciente = pacienteRepository.findByEmail(u.getEmail())
                        .map(Paciente::getNombre)
                        .orElse("Paciente");

                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "id", u.getId(),
                        "rol", u.getRol(),
                        "email", u.getEmail(),
                        "nombre", nombrePaciente, // Ahora sí, el nombre real
                        "mensaje", "Bienvenido/a al sistema"
                ));
            } else {
                // Devolvemos un error 401 Unauthorized si las credenciales fallan
                return ResponseEntity.status(401).body(Map.of(
                        "status", "error",
                        "message", "Credenciales incorrectas"
                ));
            }
        } catch (Exception e) {
            // Control de excepciones estructurado
            return ResponseEntity.status(500).body(Map.of(
                    "status", "error",
                    "message", "Error interno al procesar el login: " + e.getMessage()
            ));
        }
    }
}