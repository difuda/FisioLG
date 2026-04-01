package com.fisiolg.controllers;

import com.fisiolg.entities.Cita;
import com.fisiolg.entities.EstadoCita;
import com.fisiolg.entities.Paciente;
import com.fisiolg.entities.Servicio;
import com.fisiolg.entities.User;
import com.fisiolg.repositories.CitaRepository;
import com.fisiolg.repositories.PacienteRepository;
import com.fisiolg.repositories.ServicioRepository;
import com.fisiolg.repositories.UserRepository;
import com.fisiolg.services.AgendaService;
import com.fisiolg.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controlador principal para la gestión de citas y operaciones relacionadas con la agenda.
 * Actúa como intermediario entre las peticiones del frontend (pacientes y administradores)
 * y la lógica de negocio/base de datos.
 */
@RestController
@RequestMapping("/api/citas") // Define el prefijo de la URL para todos los endpoints de este controlador.
@CrossOrigin(origins = "*") // Permite el intercambio de recursos entre distintos orígenes (frontend - backend).
public class CitaController {

    // Inyección de dependencias: Spring Boot proporciona automáticamente las instancias de estos repositorios y servicios.
    @Autowired private CitaRepository citaRepository;
    @Autowired private AgendaService agendaService;
    @Autowired private PacienteRepository pacienteRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ServicioRepository servicioRepository;
    @Autowired private EmailService emailService;

    /**
     * Endpoint exclusivo para el panel de administración.
     * Recupera todas las citas registradas en la base de datos y las formatea para mostrarlas en el calendario o tablas del administrador.
     */
    @GetMapping("/admin/todas")
    public List<Map<String, Object>> getTodasParaAdmin() {
        List<Cita> lista = citaRepository.findAll();
        List<Map<String, Object>> eventos = new ArrayList<>();

        for (Cita c : lista) {
            Map<String, Object> e = new HashMap<>();
            e.put("id", c.getId());
            e.put("fisioId", c.getFisio() != null ? c.getFisio().getId() : null);
            e.put("estado", c.getEstado());

            // Determina el nombre a mostrar: el del paciente o la etiqueta "DISPONIBLE" si el hueco está libre.
            String nombreFinal = (c.getPaciente() != null)
                    ? c.getPaciente().getNombre() + " " + c.getPaciente().getApellidos()
                    : "DISPONIBLE";

            e.put("pacienteNombre", nombreFinal);
            e.put("pacienteId", c.getPaciente() != null ? c.getPaciente().getId() : null);
            e.put("pacienteEmail", c.getPaciente() != null ? c.getPaciente().getEmail() : "");
            e.put("fechaHora", c.getFechaHora() != null ? c.getFechaHora().toString() : null);
            e.put("notas", c.getNotas());

            eventos.add(e);
        }
        return eventos;
    }

    /**
     * Obtiene exclusivamente los huecos libres para mostrarlos al paciente en el proceso de reserva.
     * Agrupa las citas por fecha/hora para evitar mostrar horas duplicadas si ambas profesionales están libres a la misma hora.
     */
    @GetMapping("/disponibles")
    public List<Map<String, Object>> getCitasDisponibles() {
        List<Cita> libres = citaRepository.findByEstado(EstadoCita.LIBRE);

        // Uso de un Map para garantizar que cada hora solo aparezca una vez (evitando duplicidades visuales).
        Map<LocalDateTime, Cita> huecosUnicos = new HashMap<>();
        for (Cita c : libres) {
            huecosUnicos.putIfAbsent(c.getFechaHora(), c);
        }

        return huecosUnicos.values().stream().map(c -> {
            Map<String, Object> e = new HashMap<>();
            e.put("id", c.getId());
            e.put("fechaHora", c.getFechaHora().toString());
            return e;
        }).collect(Collectors.toList());
    }

    /**
     * Actualiza el estado de una cita existente (por ejemplo, confirmarla o liberarla).
     * @Transactional asegura que, si ocurre un error durante la actualización en la BD, se reviertan los cambios.
     */
    @PutMapping("/actualizar/{id}")
    @Transactional
    public ResponseEntity<?> actualizarCita(@PathVariable Long id, @RequestBody Map<String, Object> datos) {
        return citaRepository.findById(id).map(cita -> {
            // Si se proporciona un pacienteId, se asigna el paciente y se marca la cita como CONFIRMADA.
            if (datos.containsKey("pacienteId") && datos.get("pacienteId") != null) {
                Long pId = Long.valueOf(datos.get("pacienteId").toString());
                Paciente p = pacienteRepository.findById(pId).orElse(null);
                cita.setPaciente(p);
                cita.setEstado(EstadoCita.CONFIRMADA);
                if (datos.containsKey("notasClinicas")) {
                    cita.setNotas(datos.get("notasClinicas").toString());
                }
            } else {
                // Si no hay pacienteId, se asume que la cita se libera o cancela.
                cita.setPaciente(null);
                cita.setEstado(EstadoCita.LIBRE);
                cita.setNotas(null);
            }
            return ResponseEntity.ok(citaRepository.save(cita));
        }).orElse(ResponseEntity.notFound().build()); // Devuelve Error 404 si el ID de la cita no existe.
    }

    /**
     * Maneja el flujo combinado de registro de un nuevo paciente (o actualización de uno existente)
     * y la reserva simultánea de una cita.
     * Gestiona tanto a usuarios "invitados" como la creación de cuentas "miembro" con contraseña.
     */
    @PostMapping("/registro/{tipo}")
    @Transactional
    public ResponseEntity<?> registroYReserva(@PathVariable String tipo, @RequestParam Long citaId, @RequestBody Map<String, String> datos) {
        // Limpieza de datos recibidos del formulario
        String dniForm = datos.get("dni").toUpperCase().trim();
        String emailForm = datos.get("email").trim();

        // Lógica clave: Busca al paciente por DNI o Email para evitar crear registros duplicados en la BD.
        Paciente paciente = pacienteRepository.findByDni(dniForm)
                .orElseGet(() -> pacienteRepository.findByEmail(emailForm).orElse(new Paciente()));

        // Actualiza o establece los datos del paciente con la información del formulario.
        paciente.setNombre(datos.get("nombre").toUpperCase());
        paciente.setApellidos(datos.get("apellidos").toUpperCase());
        paciente.setDni(dniForm);
        paciente.setTelefono(datos.get("telefono"));
        paciente.setEmail(emailForm);

        // Guarda el paciente actualizado en la base de datos.
        paciente = pacienteRepository.save(paciente);

        // Si el usuario eligió crear una cuenta ("miembro") y proporcionó contraseña, se crea su usuario de acceso.
        if ("miembro".equalsIgnoreCase(tipo) && datos.get("password") != null) {
            User usuario = userRepository.findByEmail(emailForm).orElse(new User());
            usuario.setEmail(emailForm);
            usuario.setUsername(emailForm);
            usuario.setPassword(datos.get("password")); // Aquí se guardaría la contraseña (idealmente encriptada).
            usuario.setRol("PACIENTE");
            userRepository.save(usuario);
        }

        // Delega la actualización final de la cita al método actualizarCita pasándole el ID del paciente recién guardado.
        return actualizarCita(citaId, Map.of("pacienteId", paciente.getId()));
    }

    /**
     * Permite al administrador registrar una cita manualmente (por ejemplo, llamadas telefónicas).
     * Puede asignar la cita a un paciente existente o crear uno nuevo sobre la marcha.
     */
    @PostMapping("/reservar-manual")
    @Transactional
    public ResponseEntity<?> reservarCitaManual(
            @RequestParam(required = false) Long pacienteId,
            @RequestParam(required = false) String nombreNuevo,
            @RequestParam(required = false) String apellidosNuevo,
            @RequestParam(required = false) String dniNuevo,
            @RequestParam(required = false) String telefonoNuevo,
            @RequestParam(required = false) String emailNuevo,
            @RequestParam Long citaId,
            @RequestParam Long servicioId) {

        Cita cita = citaRepository.findById(citaId).orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        Servicio servicio = servicioRepository.findById(servicioId).orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        Paciente pacienteAsignado;
        String emailDestino = "";

        // Si el admin seleccionó un paciente existente por su ID...
        if (pacienteId != null) {
            pacienteAsignado = pacienteRepository.findById(pacienteId).orElseThrow();
            emailDestino = pacienteAsignado.getEmail();
        } else {
            // Si el admin está creando un paciente nuevo desde el panel...
            String emailLimpio = (emailNuevo != null) ? emailNuevo.trim() : "";
            String dniLimpio = (dniNuevo != null) ? dniNuevo.toUpperCase().trim() : "";

            // Verifica si el paciente ya existía por DNI/Email para no duplicarlo, o crea uno nuevo.
            pacienteAsignado = pacienteRepository.findByEmail(emailLimpio)
                    .orElseGet(() -> pacienteRepository.findByDni(dniLimpio).orElse(new Paciente()));

            pacienteAsignado.setNombre(nombreNuevo.toUpperCase());
            pacienteAsignado.setApellidos(apellidosNuevo.toUpperCase());
            pacienteAsignado.setDni(dniLimpio);
            pacienteAsignado.setTelefono(telefonoNuevo);
            pacienteAsignado.setEmail(emailLimpio);

            pacienteAsignado = pacienteRepository.save(pacienteAsignado);
            emailDestino = emailLimpio;
        }

        // Asigna el paciente, el servicio y marca la cita como confirmada.
        cita.setPaciente(pacienteAsignado);
        cita.setServicio(servicio);
        cita.setEstado(EstadoCita.CONFIRMADA);
        citaRepository.save(cita);

        // Envía un correo de confirmación automático si hay un email válido.
        if (emailDestino != null && !emailDestino.isEmpty()) {
            emailService.enviarCorreo(emailDestino, "Confirmación de Reserva", "Tu cita ha sido reservada.");
        }

        return ResponseEntity.ok(Map.of("message", "Cita reservada con éxito"));
    }

    /**
     * Permite al administrador bloquear un hueco específico (por ejemplo, para reuniones o ausencias).
     * Pone el estado en ANULADA y quita cualquier paciente asociado, eliminándolo de la vista pública.
     */
    @PostMapping("/bloquear-manual")
    @Transactional
    public ResponseEntity<?> bloquearCitaManual(@RequestParam Long citaId) {
        Cita cita = citaRepository.findById(citaId).orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        cita.setEstado(EstadoCita.ANULADA);
        cita.setPaciente(null);
        cita.setServicio(null);
        citaRepository.save(cita);
        return ResponseEntity.ok(Map.of("message", "Hueco bloqueado correctamente."));
    }

    /**
     * Genera los datos estadísticos que alimentan las "tarjetas" del panel de control del administrador.
     * Cuenta el total de citas según su estado actual en la base de datos.
     */
    @GetMapping("/admin/estadisticas")
    public ResponseEntity<Map<String, Long>> getEstadisticasAdmin() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", citaRepository.count());
        stats.put("libres", citaRepository.countByEstado(EstadoCita.LIBRE));
        stats.put("confirmadas", citaRepository.countByEstado(EstadoCita.CONFIRMADA));
        stats.put("anuladas", citaRepository.countByEstado(EstadoCita.ANULADA));
        return ResponseEntity.ok(stats);
    }

    /**
     * Recupera el expediente completo de un paciente específico, incluyendo sus datos personales
     * y el historial cronológico de todas sus citas asociadas.
     */
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<Map<String, Object>> getDetallePacienteYHistorial(@PathVariable Long pacienteId) {
        Map<String, Object> respuesta = new HashMap<>();

        // Recupera y formatea los datos de contacto del paciente.
        Paciente p = pacienteRepository.findById(pacienteId).orElse(null);
        if (p != null) {
            Map<String, Object> datosPaciente = new HashMap<>();
            datosPaciente.put("nombre", p.getNombre() + " " + p.getApellidos());
            datosPaciente.put("dni", p.getDni());
            datosPaciente.put("telefono", p.getTelefono());
            datosPaciente.put("email", p.getEmail());
            respuesta.put("paciente", datosPaciente);
        }

        // Recupera el historial de citas ordenado desde la más reciente a la más antigua.
        List<Cita> citas = citaRepository.findByPacienteIdOrderByFechaHoraDesc(pacienteId);
        List<Map<String, Object>> historial = new ArrayList<>();

        for (Cita c : citas) {
            Map<String, Object> e = new HashMap<>();
            e.put("id", c.getId());
            e.put("fechaHora", c.getFechaHora().toString());
            e.put("estado", c.getEstado().toString());
            e.put("notas", c.getNotas() != null ? c.getNotas() : "");
            historial.add(e);
        }
        respuesta.put("historial", historial);

        return ResponseEntity.ok(respuesta);
    }

    /**
     * Endpoint llamado por el administrador para crear automáticamente todos los huecos libres (intervalos de 40 min)
     * para un día específico, delegando la lógica compleja al AgendaService.
     */
    @PostMapping("/generar-jornada")
    public ResponseEntity<?> generarJornada(@RequestParam("fecha") String fechaStr) {
        try {
            LocalDate fecha = LocalDate.parse(fechaStr);
            agendaService.generarJornadaCompleta(fecha); // Llama al servicio que contiene el algoritmo de generación.
            return ResponseEntity.ok(Map.of("status", "ok", "message", "Jornada generada con éxito"));
        } catch (Exception e) {
            // Captura errores (ej: día no laborable, jornada ya generada) y los devuelve al frontend.
            return ResponseEntity.badRequest().body(Map.of("message", "Error: " + e.getMessage()));
        }
    }
}