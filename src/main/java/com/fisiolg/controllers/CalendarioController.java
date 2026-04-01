package com.fisiolg.controllers;

import com.fisiolg.entities.Cita;
import com.fisiolg.entities.EstadoCita;
import com.fisiolg.repositories.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controlador REST encargado de suministrar los datos al calendario del frontend (FullCalendar).
 * Gestiona la disponibilidad de horarios y la visualización de los eventos programados.
 */
@RestController // Indica que esta clase procesa peticiones web y devuelve datos en formato JSON, no vistas HTML.
@RequestMapping("/api/calendario") // Ruta base para todas las peticiones relacionadas con el calendario.
@CrossOrigin(origins = "*") // Permite peticiones desde cualquier origen (CORS), necesario para conectar el frontend con el backend.
public class CalendarioController {

    @Autowired // Inyecta el repositorio para poder consultar la base de datos de citas sin instanciarlo manualmente.
    private CitaRepository citaRepository;

    /**
     * Endpoint que calcula y devuelve las horas exactas en las que un paciente puede reservar.
     * Implementa la regla de negocio principal: turnos de 40 minutos y máximo 2 profesionales simultáneas.
     */
    @GetMapping("/huecos")
    public List<String> getHuecosDisponibles(@RequestParam String fecha) {
        // Convierte la fecha recibida como texto (desde el frontend) a un objeto LocalDate de Java.
        LocalDate fechaBusqueda = LocalDate.parse(fecha);

        // Extrae todas las citas de la base de datos y filtra solo las que coinciden con el día seleccionado.
        List<Cita> citasDelDia = citaRepository.findAll().stream()
                .filter(c -> c.getFechaHora().toLocalDate().equals(fechaBusqueda))
                .collect(Collectors.toList());

        // Define estáticamente los bloques de la jornada laboral de la clínica (intervalos de 40 minutos).
        List<String> todosLosHuecos = Arrays.asList(
                "15:30", "16:10", "16:50", "17:30", "18:10", "18:50", "19:30", "20:10"
        );

        List<String> disponibles = new ArrayList<>();

        // Recorre cada uno de los bloques horarios para comprobar si hay sitio libre.
        for (String horaStr : todosLosHuecos) {
            LocalTime horaHueco = LocalTime.parse(horaStr);

            // Cuenta cuántas citas YA están ocupadas (estado distinto a LIBRE) en ese bloque de 40 minutos exacto.
            long ocupadas = citasDelDia.stream()
                    .filter(c -> c.getFechaHora().toLocalTime().equals(horaHueco))
                    .filter(c -> c.getEstado() != EstadoCita.LIBRE)
                    .count();

            // REGLA CLAVE: Como hay 2 fisioterapeutas (Lucía y Ánxela), un hueco sigue estando disponible
            // siempre y cuando haya menos de 2 citas ocupadas a esa misma hora.
            if (ocupadas < 2) {
                disponibles.add(horaStr);
            }
        }

        // Devuelve la lista limpia de horas disponibles al cliente web.
        return disponibles;
    }

    /**
     * Endpoint que carga todas las citas de la base de datos y las formatea para que la librería
     * FullCalendar (en el panel de administración) pueda dibujarlas correctamente en pantalla.
     */
    @GetMapping("/eventos")
    public List<Map<String, Object>> getEventos() {
        // Recupera el listado completo de citas de la clínica.
        List<Cita> lista = citaRepository.findAll();
        List<Map<String, Object>> eventos = new ArrayList<>();

        for (Cita c : lista) {
            try {
                // Crea un mapa de datos (clave-valor) que FullCalendar exige para renderizar un evento.
                Map<String, Object> e = new HashMap<>();
                e.put("id", c.getId()); // ID único de la cita para poder hacer clic y gestionarla.
                e.put("start", c.getFechaHora().toString()); // Fecha y hora de inicio del bloque.

                // Determina el estado textual de la cita para aplicarle colores o estilos en el frontend.
                String estadoTexto = "DISPONIBLE";
                if (c.getEstado() != null) {
                    estadoTexto = (c.getEstado() == EstadoCita.CONFIRMADA) ? "CONFIRMADA" :
                            (c.getEstado() == EstadoCita.CONFIRMADA) ? "RESERVADO" : "DISPONIBLE";
                }
                e.put("estado", estadoTexto);

                // Configura el título que se verá escrito dentro de la "pastilla" del calendario.
                String titulo = "LIBRE";
                if (c.getPaciente() != null) {
                    // Si hay un paciente asignado, muestra su nombre en mayúsculas.
                    titulo = (c.getPaciente().getNombre() + " " + c.getPaciente().getApellidos()).toUpperCase();
                } else if (c.getNotas() != null && !c.getNotas().isEmpty()) {
                    // Si es un bloqueo manual con notas pero sin paciente, muestra la nota (ej: "REUNIÓN").
                    titulo = c.getNotas().toUpperCase();
                }

                e.put("title", titulo);

                // Añade el ID de la fisio para poder filtrar o mostrar quién atiende la cita.
                e.put("fisioId", c.getFisio() != null ? c.getFisio().getId() : null);

                // Añade el evento ya formateado a la lista final.
                eventos.add(e);
            } catch (Exception ex) {
                // Captura cualquier error individual (ej: datos corruptos) para no colgar toda la carga del calendario.
                System.err.println("Error procesando evento cita " + c.getId() + ": " + ex.getMessage());
            }
        }
        // Devuelve la colección de eventos en formato JSON.
        return eventos;
    }
}