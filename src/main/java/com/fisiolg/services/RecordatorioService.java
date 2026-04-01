package com.fisiolg.services;

import com.fisiolg.entities.Cita;
import com.fisiolg.entities.EstadoCita;
import com.fisiolg.repositories.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio automatizado para la gestión y envío de recordatorios de citas.
 * CONTEXTO DE NEGOCIO: Este servicio notifica al cliente sobre su reserva en el
 * bloque horario correspondiente (citas de 40 minutos). Como el cliente reserva
 * el hueco y no al profesional de forma directa, el recordatorio se centra
 * exclusivamente en la fecha y la hora en la clínica.
 * Cumple con las normativas de protección de datos vigentes al utilizar
 * la información de contacto estrictamente para la prestación del servicio.
 */
@Service
public class RecordatorioService {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private EmailService emailService;

    /**
     * Tarea programada (Cron Job) que se ejecuta automáticamente todos los días a las 09:00 AM.
     * Busca todas las citas confirmadas programadas para el día siguiente y envía un
     * correo electrónico recordatorio a los clientes que aún no han sido notificados.
     */
    @Scheduled(cron = "0 0 9 * * *")
    public void avisarCitasManana() {

        System.out.println("--- INICIO TEST RECORDATORIOS ---");
        System.out.println("PASO 1: El reloj funciona. Arrancando tarea...");

        LocalDateTime mananaI = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime mananaF = LocalDateTime.now().plusDays(1).withHour(23).withMinute(59).withSecond(59);

        System.out.println("PASO 2: Buscando citas entre " + mananaI + " y " + mananaF);

        List<Cita> proximas = citaRepository.findByEstadoAndRecordatorioEnviadoFalseAndFechaHoraBetween(EstadoCita.CONFIRMADA, mananaI, mananaF);

        System.out.println("PASO 3: La base de datos ha devuelto " + proximas.size() + " citas válidas.");

        for (Cita c : proximas) {
            System.out.println("-> Analizando Cita ID: " + c.getId() + " | Estado: " + c.getEstado());

            if (c.getPaciente() != null && c.getEstado() == EstadoCita.CONFIRMADA) {

                String email = c.getPaciente().getEmail();
                System.out.println("-> Paciente encontrado. Email registrado: " + email);

                if (email != null && !email.isEmpty()) {
                    System.out.println("PASO 4: Intentando conectar con el servidor de correo para enviar...");
                    try {
                        emailService.enviarCorreo(
                                email,
                                "Recordatorio de Cita - FisioLG",
                                "Hola " + c.getPaciente().getNombre() + ", te recordamos que tienes una cita programada para mañana."
                        );

                        c.setRecordatorioEnviado(true);
                        citaRepository.save(c);

                        System.out.println("✅ ÉXITO: Correo enviado a " + email + " y cita actualizada.");
                    } catch (Exception e) {
                        System.out.println("❌ ERROR CRÍTICO AL ENVIAR CORREO: " + e.getMessage());
                    }
                } else {
                    System.out.println("❌ BLOQUEO: El paciente no tiene un email válido.");
                }
            } else {
                System.out.println("❌ BLOQUEO: La cita no tiene paciente asociado o no está confirmada.");
            }
        }
        System.out.println("--- FIN TEST RECORDATORIOS ---");
    }
}