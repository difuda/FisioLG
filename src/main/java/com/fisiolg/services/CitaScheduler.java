package com.fisiolg.services;

import com.fisiolg.entities.Cita;
import com.fisiolg.entities.EstadoCita;
import com.fisiolg.repositories.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Componente encargado de ejecutar tareas programadas automáticamente,
 * como el envío de recordatorios de citas a los clientes.
 */
@Component
public class CitaScheduler {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private EmailService emailService;

    /**
     * Tarea automática que se ejecuta diariamente a las 9:00 AM.
     * Busca las citas confirmadas para el día siguiente y envía un recordatorio por correo electrónico.
     */
    @Scheduled(cron = "0 0 9 * * *")
    @Transactional
    public void enviarRecordatoriosDiarios() {

        // Definimos el rango de tiempo exacto para el día de mañana
        LocalDate manana = LocalDate.now().plusDays(1);
        LocalDateTime inicioManana = manana.atStartOfDay();
        LocalDateTime finManana = manana.atTime(LocalTime.MAX);

        // Obtenemos solo las citas confirmadas que aún no han recibido el recordatorio
        List<Cita> citasManana = citaRepository.findByEstadoAndRecordatorioEnviadoFalseAndFechaHoraBetween(
                EstadoCita.CONFIRMADA,
                inicioManana,
                finManana
        );

        for (Cita cita : citasManana) {
            // TODO: Se recomienda usar un DateTimeFormatter para que el cliente vea la fecha en un formato amigable (ej. dd/MM/yyyy HH:mm)
            String fechaFormateada = cita.getFechaHora().toString();

            // Envío del correo a través del servicio de mensajería
            emailService.enviarConfirmacionCita(cita.getPaciente().getEmail(), fechaFormateada);

            // Marcamos el recordatorio como enviado para cumplir con la política de no duplicidad
            cita.setRecordatorioEnviado(true);
            citaRepository.save(cita);
        }
    }
}