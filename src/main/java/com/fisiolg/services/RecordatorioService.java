package com.fisiolg.services;

import com.fisiolg.entities.Cita;
import com.fisiolg.entities.EstadoCita;
import com.fisiolg.repositories.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecordatorioService {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private EmailService emailService;


    @Scheduled(cron = "0 0 9 * * *")
    public void avisarCitasManana() {

        System.out.println("⏰ [09:00 AM] Iniciando envío automático de recordatorios...");

        LocalDateTime mananaI = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime mananaF = LocalDateTime.now().plusDays(1).withHour(23).withMinute(59).withSecond(59);


        List<Cita> proximas = citaRepository.findByEstadoAndRecordatorioEnviadoFalseAndFechaHoraBetween(EstadoCita.CONFIRMADA, mananaI, mananaF);

        for (Cita c : proximas) {


            if (c.getPaciente() != null && c.getEstado() == EstadoCita.CONFIRMADA) {


                String email = c.getPaciente().getEmail();
                String nombre = c.getPaciente().getNombre();
                String hora = c.getFechaHora().toLocalTime().toString();


                if (email != null && !email.isEmpty()) {
                    emailService.enviarCorreo(
                            email,
                            "Recordatorio de Cita - FisioLG",
                            "Hola " + nombre + ", te recordamos que tienes una cita programada para mañana a las " + hora + ". " +
                                    "\n\nSi no puedes asistir, por favor avísanos con antelación. ¡Gracias!"
                    );


                    c.setRecordatorioEnviado(true);
                    citaRepository.save(c);

                    System.out.println("✅ Recordatorio enviado con éxito a: " + email);
                }
            }
        }
        System.out.println("✅ Proceso de recordatorios finalizado.");
    }
}