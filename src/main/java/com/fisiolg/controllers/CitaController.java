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
        Paciente paciente = pacienteRepository.findByDni(datos.get("dni").toUpperCase())
                .orElse(new Paciente());

        paciente.setNombre(datos.get("nombre").toUpperCase());
        paciente.setApellidos(datos.get("apellidos").toUpperCase());
        paciente.setDni(datos.get("dni").toUpperCase());
        paciente.setTelefono(datos.get("telefono"));
        paciente.setEmail(datos.get("email"));
        pacienteRepository.save(paciente);


        if ("miembro".equalsIgnoreCase(tipo) && datos.get("password") != null) {
            User usuario = userRepository.findByEmail(datos.get("email")).orElse(new User());
            usuario.setEmail(datos.get("email"));
            usuario.setPassword(datos.get("password"));
            usuario.setRol("PACIENTE");
            userRepository.save(usuario);
        }

        return actualizarCita(citaId, Map.of("pacienteId", paciente.getId()));
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


    @GetMapping("/paciente/{id}")
    public List<Cita> getPorPaciente(@PathVariable Long id) {
        return citaRepository.findByPacienteIdOrderByFechaHoraDesc(id);
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

            pacienteAsignado = new Paciente();
            pacienteAsignado.setNombre(nombreNuevo);
            pacienteAsignado.setApellidos(apellidosNuevo);
            pacienteAsignado.setDni(dniNuevo);
            pacienteAsignado.setTelefono(telefonoNuevo);

            if (emailNuevo != null && !emailNuevo.trim().isEmpty()) {
                pacienteAsignado.setEmail(emailNuevo);
                emailDestino = emailNuevo;
            }


            pacienteRepository.save(pacienteAsignado);
        }


        cita.setPaciente(pacienteAsignado);
        cita.setServicio(servicio);
        cita.setEstado(EstadoCita.CONFIRMADA);

        citaRepository.save(cita);


        if (emailDestino != null && !emailDestino.isEmpty()) {
            String fechaStr = cita.getFechaHora().toLocalDate().toString();
            String horaStr = cita.getFechaHora().toLocalTime().toString();

            emailService.enviarCorreo(
                    emailDestino,
                    "Confirmación de Reserva - FisioLG",
                    "Hola " + pacienteAsignado.getNombre() + ",\n\nTu cita ha sido reservada para el día "
                            + fechaStr + " a las " + horaStr + " para tu sesión de " + servicio.getNombre() + ".\n\n¡Te esperamos en la clínica!"
            );
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
        long total = citaRepository.count();
        long libres = citaRepository.countByEstado(EstadoCita.LIBRE);
        long confirmadas = citaRepository.countByEstado(EstadoCita.CONFIRMADA);
        long anuladas = citaRepository.countByEstado(EstadoCita.ANULADA);

        Map<String, Long> stats = new HashMap<>();
        stats.put("total", total);
        stats.put("libres", libres);
        stats.put("confirmadas", confirmadas);
        stats.put("anuladas", anuladas);

        return ResponseEntity.ok(stats);
    }
}