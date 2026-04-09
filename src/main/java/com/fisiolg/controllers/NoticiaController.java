package com.fisiolg.controllers;

import com.fisiolg.entities.Noticia;
import com.fisiolg.repositories.NoticiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/noticias")
@CrossOrigin(origins = "*") // Fundamental para que el navegador no bloquee la conexión
public class NoticiaController {

    @Autowired
    private NoticiaRepository noticiaRepository;

    // 1. Método para que la web de los clientes lea la noticia (GET)
    // Coincide con la ruta: http://localhost:8080/api/noticias/actual
    @GetMapping("/actual")
    public ResponseEntity<Map<String, String>> obtenerNoticiaActual() {
        // Buscamos la noticia con ID 1 (asumimos que usamos siempre esa fila para la portada)
        Noticia noticia = noticiaRepository.findById(1L).orElse(null);

        Map<String, String> respuesta = new HashMap<>();
        if (noticia != null && noticia.getTexto() != null && !noticia.getTexto().isEmpty()) {
            respuesta.put("texto", noticia.getTexto());
        } else {
            respuesta.put("texto", "Bienvenidos a Fisioterapia Lucía Garza"); // Mensaje por defecto si la BD está vacía
        }

        return ResponseEntity.ok(respuesta);
    }

    // 2. Método para que el administrador actualice la noticia (POST)
    // Coincide con la ruta: http://localhost:8080/api/noticias/actualizar
    @PostMapping("/actualizar")
    public ResponseEntity<Map<String, String>> actualizarNoticia(@RequestBody Map<String, String> request) {
        String nuevoTexto = request.get("texto");

        // Buscamos si ya existe la noticia con ID 1. Si no existe, creamos una nueva.
        Noticia noticia = noticiaRepository.findById(1L).orElse(new Noticia());
        noticia.setId(1L); // Forzamos que siempre sea la ID 1 para no llenar la BD de filas inútiles
        noticia.setTexto(nuevoTexto);

        noticiaRepository.save(noticia); // El Repositorio guarda en PostgreSQL

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Noticia de portada actualizada correctamente");

        return ResponseEntity.ok(respuesta);
    }
}
