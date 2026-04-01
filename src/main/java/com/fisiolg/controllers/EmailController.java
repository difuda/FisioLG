package com.fisiolg.controllers;

import com.fisiolg.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * Controlador REST para gestionar el envío de correos electrónicos.
 * Permite el envío de códigos de verificación y notificaciones sobre las reservas de citas a los clientes.
 */
@RestController
@RequestMapping("/api/mail")
// TODO: Para producción, cambiar "*" por el dominio específico del frontend para asegurar la protección de datos.
@CrossOrigin(origins = "*")
public class EmailController {

    @Autowired
    private EmailService emailService;

    /**
     * Endpoint para enviar un código de verificación al correo del usuario.
     * * @param payload Mapa que debe contener las claves 'email' y 'codigo'.
     * @return ResponseEntity con el estado de la operación (éxito o error).
     */
    @PostMapping("/enviar-codigo")
    public ResponseEntity<?> enviar(@RequestBody Map<String, String> payload) {
        try {
            String email = payload.get("email");
            String codigo = payload.get("codigo");

            // Validación básica para asegurar que se reciben los datos necesarios
            if (email == null || codigo == null) {
                return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "Faltan datos obligatorios para el envío"));
            }

            // Llamada al servicio encargado de la lógica de envío
            emailService.enviarCodigo(email, codigo);

            return ResponseEntity.ok(Map.of("status", "success", "message", "Código enviado a " + email));

        } catch (Exception e) {
            // Control de excepciones para devolver un error 500 estructurado en lugar de romper el flujo
            return ResponseEntity.status(500).body(Map.of("status", "error", "message", "Error al enviar el mail: " + e.getMessage()));
        }
    }

    /**
     * Endpoint para notificar al cliente la confirmación de la cita que ha reservado.
     * * @param payload Mapa que debe contener las claves 'email' y 'mensaje'.
     * @return ResponseEntity con el estado de la operación.
     */
    @PostMapping("/notificar-cita")
    public ResponseEntity<?> notificarCita(@RequestBody Map<String, String> payload) {
        try {
            String email = payload.get("email");
            String mensaje = payload.get("mensaje");

            // Validación de datos unificada con el criterio del endpoint anterior
            if (email == null || mensaje == null) {
                return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "Faltan datos obligatorios para la notificación"));
            }

            // El asunto queda fijado para confirmar la reserva realizada por el cliente
            emailService.enviarCorreo(email, "Confirmación de Cita - FisioLG", mensaje);

            return ResponseEntity.ok(Map.of("status", "success", "message", "Notificación enviada correctamente"));

        } catch (Exception e) {
            // Se añade bloque try-catch para capturar posibles fallos del servidor de correo
            return ResponseEntity.status(500).body(Map.of("status", "error", "message", "Error al enviar la notificación: " + e.getMessage()));
        }
    }
}