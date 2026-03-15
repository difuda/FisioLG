package com.fisiolg.services;

import com.fisiolg.entities.Cita;
import com.fisiolg.entities.Fisio;
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


        if (dia == DayOfWeek.WEDNESDAY || dia == DayOfWeek.FRIDAY) {
            crearHuecos(fecha, LocalTime.of(9, 0), LocalTime.of(15, 0), 2L);
        } else {
            crearHuecos(fecha, LocalTime.of(15, 0), LocalTime.of(21, 0), 2L);
        }


        crearHuecos(fecha, LocalTime.of(9, 0), LocalTime.of(13, 0), 1L);
        crearHuecos(fecha, LocalTime.of(15, 30), LocalTime.of(20, 50), 1L);
    }

    private void crearHuecos(LocalDate fecha, LocalTime inicio, LocalTime fin, Long fisioId) {
        LocalTime horaActual = inicio;


        while (!horaActual.plusMinutes(40).isAfter(fin)) {
            LocalDateTime fechaHoraActual = LocalDateTime.of(fecha, horaActual);


            boolean yaExiste = citaRepository.existsByFechaHoraAndFisioId(fechaHoraActual, fisioId);

            if (!yaExiste) {
                Cita nuevaCita = new Cita();
                nuevaCita.setFechaHora(fechaHoraActual);


                Fisio f = new Fisio();
                f.setId(fisioId);
                nuevaCita.setFisio(f);

                nuevaCita.setEstado(EstadoCita.LIBRE);
                nuevaCita.setRecordatorioEnviado(false);

                citaRepository.save(nuevaCita);
            }


            horaActual = horaActual.plusMinutes(40);
        }
    }
}