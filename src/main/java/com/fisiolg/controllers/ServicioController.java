package com.fisiolg.controllers;

import com.fisiolg.entities.Servicio;
import com.fisiolg.repositories.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestionar el catálogo de servicios ofrecidos en la clínica.
 */
@RestController
@RequestMapping("/api/servicios")
// TODO: Para producción, cambiar "*" por el dominio específico del frontend para asegurar la protección de datos.
@CrossOrigin(origins = "*")
public class ServicioController {

    @Autowired
    private ServicioRepository servicioRepository;

    /**
     * Obtiene la lista completa de servicios disponibles para que el cliente pueda elegir en su reserva.
     * @return ResponseEntity con la lista de servicios o un mensaje de error estructurado si falla la conexión.
     */
    @GetMapping
    public ResponseEntity<?> obtenerTodos() {
        try {
            List<Servicio> servicios = servicioRepository.findAll();
            return ResponseEntity.ok(servicios);
        } catch (Exception e) {
            // Control de excepciones para evitar caídas de la aplicación si la base de datos no responde
            return ResponseEntity.status(500).body(Map.of(
                    "status", "error",
                    "message", "Error interno al obtener el catálogo de servicios: " + e.getMessage()
            ));
        }
    }
}