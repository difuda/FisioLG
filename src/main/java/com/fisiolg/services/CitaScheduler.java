package com.fisiolg.services;

import com.fisiolg.entities.Cita;
import com.fisiolg.entities.EstadoCita;
import com.fisiolg.repositories.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
public class CitaScheduler {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private EmailService emailService;


    @Scheduled(cron = "0 0 9 * * *")
    public void enviarRecordatoriosDiarios() {


        LocalDate manana = LocalDate.now().plusDays(1);
        LocalDateTime inicioManana = manana.atStartOfDay();
        LocalDateTime finManana = manana.atTime(LocalTime.MAX);


        List<Cita> citasManana = citaRepository.findByEstadoAndRecordatorioEnviadoFalseAndFechaHoraBetween(
                EstadoCita.CONFIRMADA,
                inicioManana,
                finManana
        );


        for (Cita cita : citasManana) {
            String fechaFormateada = cita.getFechaHora().toString();

            emailService.enviarConfirmacionCita(cita.getPaciente().getEmail(), fechaFormateada);


            cita.setRecordatorioEnviado(true);
            citaRepository.save(cita);
        }
    }
}