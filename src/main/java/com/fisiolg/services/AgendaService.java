package com.fisiolg.services;

import com.fisiolg.entities.Cita;
import com.fisiolg.entities.EstadoCita;
import com.fisiolg.repositories.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class AgendaService {

    @Autowired
    private CitaRepository citaRepository;

    public void generarJornadaCompleta(LocalDate fecha) {
        DayOfWeek dia = fecha.getDayOfWeek();


        if (dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY) return;
        LocalTime inicioA, finA;

        if (dia == DayOfWeek.WEDNESDAY || dia == DayOfWeek.FRIDAY) {
            inicioA = LocalTime.of(9, 0);
            finA = LocalTime.of(15, 0);
        } else {
            inicioA = LocalTime.of(15, 0);
            finA = LocalTime.of(21, 0);
        }
        crearHuecos(fecha, inicioA, finA, 1L);


        crearHuecos(fecha, LocalTime.of(9, 0), LocalTime.of(13, 0), 2L);
        crearHuecos(fecha, LocalTime.of(15, 30), LocalTime.of(20, 45), 2L);
    }

    private void crearHuecos(LocalDate fecha, LocalTime inicio, LocalTime fin, Long fisioId) {
        LocalTime horaActual = inicio;

        while (!horaActual.plusMinutes(40).isAfter(fin)) {
            LocalDateTime fechaHoraActual = LocalDateTime.of(fecha, horaActual);


            boolean yaExiste = citaRepository.existsByFechaHoraAndFisioId(fechaHoraActual, fisioId);

            if (!yaExiste) {
                Cita nuevaCita = new Cita();
                nuevaCita.setFechaHora(fechaHoraActual);
                nuevaCita.setFisioId(fisioId);
                nuevaCita.setEstadoId(EstadoCita.LIBRE);
                nuevaCita.setEstado("LIBRE");
                nuevaCita.setRecordatorioEnviado(false);

                citaRepository.save(nuevaCita);
            } else {

                System.out.println("Omitiendo duplicado: " + fechaHoraActual + " para fisio " + fisioId);
            }

            horaActual = horaActual.plusMinutes(40);
        }
    }
}