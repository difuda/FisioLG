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

@RestController
@RequestMapping("/api/citas")
@CrossOrigin(origins = "*")
public class CitaController {

    @Autowired private CitaRepository citaRepository;
    @Autowired private AgendaService agendaService;
    @Autowired private PacienteRepository pacienteRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ServicioRepository servicioRepository;
    @Autowired private EmailService emailService;

    @GetMapping("/admin/todas")
    public List<Map<String, Object>> getTodasParaAdmin() {
        List<Cita> lista = citaRepository.findAll();
        List<Map<String, Object>> eventos = new ArrayList<>();
        for (Cita c : lista) {
            Map<String, Object> e = new HashMap<>();
            e.put("id", c.getId());
            e.put("fisioId", c.getFisio() != null ? c.getFisio().getId() : null);
            e.put("estado", c.getEstado());
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

    @GetMapping("/disponibles")
    public List<Map<String, Object>> getCitasDisponibles() {
        List<Cita> libres = citaRepository.findByEstado(EstadoCita.LIBRE);
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

    @PutMapping("/actualizar/{id}")
    @Transactional
    public ResponseEntity<?> actualizarCita(@PathVariable Long id, @RequestBody Map<String, Object> datos) {
        return citaRepository.findById(id).map(cita -> {
            if (datos.containsKey("pacienteId") && datos.get("pacienteId") != null) {
                Long pId = Long.valueOf(datos.get("pacienteId").toString());
                Paciente p = pacienteRepository.findById(pId).orElse(null);
                cita.setPaciente(p);
                cita.setEstado(EstadoCita.CONFIRMADA);
                if (datos.containsKey("notasClinicas")) {
                    cita.setNotas(datos.get("notasClinicas").toString());
                }
            } else {
                cita.setPaciente(null);
                cita.setEstado(EstadoCita.LIBRE);
                cita.setNotas(null);
            }
            return ResponseEntity.ok(citaRepository.save(cita));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/registro/{tipo}")
    @Transactional
    public ResponseEntity<?> registroYReserva(@PathVariable String tipo, @RequestParam Long citaId, @RequestBody Map<String, String> datos) {
        String dniForm = datos.get("dni").toUpperCase().trim();
        String emailForm = datos.get("email").trim();


        Paciente paciente = pacienteRepository.findByDni(dniForm)
                .orElseGet(() -> pacienteRepository.findByEmail(emailForm).orElse(new Paciente()));


        paciente.setNombre(datos.get("nombre").toUpperCase());
        paciente.setApellidos(datos.get("apellidos").toUpperCase());
        paciente.setDni(dniForm);
        paciente.setTelefono(datos.get("telefono"));
        paciente.setEmail(emailForm);


        paciente = pacienteRepository.save(paciente);


        if ("miembro".equalsIgnoreCase(tipo) && datos.get("password") != null) {

            User usuario = userRepository.findByEmail(emailForm).orElse(new User());
            usuario.setEmail(emailForm);
            usuario.setUsername(emailForm);
            usuario.setPassword(datos.get("password"));
            usuario.setRol("PACIENTE");

            userRepository.save(usuario);
        }


        return actualizarCita(citaId, Map.of("pacienteId", paciente.getId()));
    }

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

        if (pacienteId != null) {
            pacienteAsignado = pacienteRepository.findById(pacienteId).orElseThrow();
            emailDestino = pacienteAsignado.getEmail();
        } else {
            String emailLimpio = (emailNuevo != null) ? emailNuevo.trim() : "";
            String dniLimpio = (dniNuevo != null) ? dniNuevo.toUpperCase().trim() : "";

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

        cita.setPaciente(pacienteAsignado);
        cita.setServicio(servicio);
        cita.setEstado(EstadoCita.CONFIRMADA);
        citaRepository.save(cita);

        if (emailDestino != null && !emailDestino.isEmpty()) {
            emailService.enviarCorreo(emailDestino, "Confirmación de Reserva", "Tu cita ha sido reservada.");
        }

        return ResponseEntity.ok(Map.of("message", "Cita reservada con éxito"));
    }

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

    @GetMapping("/admin/estadisticas")
    public ResponseEntity<Map<String, Long>> getEstadisticasAdmin() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", citaRepository.count());
        stats.put("libres", citaRepository.countByEstado(EstadoCita.LIBRE));
        stats.put("confirmadas", citaRepository.countByEstado(EstadoCita.CONFIRMADA));
        stats.put("anuladas", citaRepository.countByEstado(EstadoCita.ANULADA));
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<Map<String, Object>> getDetallePacienteYHistorial(@PathVariable Long pacienteId) {
        Map<String, Object> respuesta = new HashMap<>();


        Paciente p = pacienteRepository.findById(pacienteId).orElse(null);
        if (p != null) {
            Map<String, Object> datosPaciente = new HashMap<>();
            datosPaciente.put("nombre", p.getNombre() + " " + p.getApellidos());
            datosPaciente.put("dni", p.getDni());
            datosPaciente.put("telefono", p.getTelefono());
            datosPaciente.put("email", p.getEmail());
            respuesta.put("paciente", datosPaciente);
        }


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

    @PostMapping("/generar-jornada")
    public ResponseEntity<?> generarJornada(@RequestParam("fecha") String fechaStr) {
        try {
            LocalDate fecha = LocalDate.parse(fechaStr);
            agendaService.generarJornadaCompleta(fecha);
            return ResponseEntity.ok(Map.of("status", "ok", "message", "Jornada generada con éxito"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error: " + e.getMessage()));
        }
    }
}