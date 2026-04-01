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

/**
 * Controlador encargado de gestionar la autenticación y la verificación de identidad.
 * Gestiona el envío y validación de los códigos OTP (One-Time Password) por correo electrónico.
 */
@RestController // Indica a Spring que esta clase responderá a peticiones web devolviendo datos (formato JSON).
@RequestMapping("/api/auth") // Ruta base para todos los endpoints de esta clase.
@CrossOrigin(origins = "*") // Permite que el frontend (HTML/JS) se comunique con este backend sin bloqueos de seguridad CORS.
public class AuthController {

    @Autowired // Inyecta automáticamente el repositorio para consultar y guardar datos en la tabla de usuarios.
    private UserRepository userRepository;

    @Autowired // Inyecta el servicio de correos para poder enviar los emails con los códigos.
    private EmailService emailService;

    /**
     * Endpoint para generar y enviar un código de verificación (OTP) al correo del paciente.
     * Si el paciente no existe en la base de datos, lo crea como un usuario "INVITADO".
     */
    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestParam String email) {

        // Busca al usuario por su email. Si no lo encuentra, crea uno nuevo al vuelo para permitirle reservar.
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setUsername(email); // Usa el email como nombre de usuario por defecto.
                    newUser.setPassword("OTP_USER"); // Contraseña genérica temporal, la seguridad real es el código al correo.
                    newUser.setRol("INVITADO"); // Asigna el rol invitado para limitarle el acceso administrativo.
                    return newUser;
                });

        // Genera un código numérico aleatorio de 6 dígitos (ej: "045812").
        String otp = String.format("%06d", new Random().nextInt(1000000));

        // Guarda el código generado en el perfil del usuario en la base de datos para comprobarlo después.
        user.setTokenRecuperacion(otp);
        userRepository.save(user);

        // Llama al servicio de email para enviar el código real a la bandeja de entrada del paciente.
        emailService.enviarCodigo(email, otp);

        // Devuelve una respuesta exitosa al frontend para que cambie a la pantalla de "Introduce tu código".
        return ResponseEntity.ok(Map.of("status", "success", "message", "Código enviado"));
    }

    /**
     * Endpoint para comprobar si el código que ha escrito el paciente en la web coincide con el que se le envió.
     */
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String code) {

        // Busca al usuario en la base de datos usando el email introducido.
        Optional<User> userOpt = userRepository.findByEmail(email);

        // Si el usuario existe, procede a comprobar la coincidencia del código.
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Verifica que haya un código guardado y que sea exactamente igual al escrito por el paciente.
            if (user.getTokenRecuperacion() != null && user.getTokenRecuperacion().equals(code)) {

                // ¡Éxito! El código es correcto. Por seguridad, se anula (null) en la BD para evitar su reutilización.
                user.setTokenRecuperacion(null);
                userRepository.save(user);

                // Devuelve confirmación positiva al frontend, incluyendo el rol para redirigirlo al panel correcto.
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "Verificación exitosa",
                        "rol", user.getRol()
                ));
            }
        }

        // Si el código no coincide o el usuario no existe, rechaza el acceso devolviendo un error 401 (No Autorizado).
        return ResponseEntity.status(401).body(Map.of("status", "error", "message", "Código incorrecto"));
    }
}