package com.fisiolg.services;

import com.fisiolg.entities.Cita;
import com.fisiolg.entities.Fisio;
import com.fisiolg.entities.EstadoCita;
import com.fisiolg.repositories.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Servicio encargado de la generación automática de la agenda de la clínica.
 */
@Service
public class AgendaService {

    @Autowired
    private CitaRepository citaRepository;

    /**
     * Genera los huecos disponibles para una jornada laboral completa,
     * teniendo en cuenta los horarios específicos de cada profesional.
     */
    @Transactional
    public void generarJornadaCompleta(LocalDate fecha) {
        DayOfWeek dia = fecha.getDayOfWeek();

        // No se genera agenda para los fines de semana
        if (dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY) return;

        // Horarios específicos para el profesional 2 (FisioId: 2L)
        if (dia == DayOfWeek.WEDNESDAY || dia == DayOfWeek.FRIDAY) {
            crearHuecos(fecha, LocalTime.of(9, 0), LocalTime.of(15, 0), 2L);
        } else {
            crearHuecos(fecha, LocalTime.of(15, 0), LocalTime.of(21, 0), 2L);
        }

        // Horarios específicos para el profesional 1 (FisioId: 1L)
        crearHuecos(fecha, LocalTime.of(9, 0), LocalTime.of(13, 0), 1L);
        crearHuecos(fecha, LocalTime.of(15, 30), LocalTime.of(20, 50), 1L);
    }

    /**
     * Crea bloques exactos de 40 minutos para un profesional en un rango horario determinado.
     */
    private void crearHuecos(LocalDate fecha, LocalTime inicio, LocalTime fin, Long fisioId) {
        LocalTime horaActual = inicio;

        // Bucle para generar citas cada 40 minutos
        while (!horaActual.plusMinutes(40).isAfter(fin)) {
            LocalDateTime fechaHoraActual = LocalDateTime.of(fecha, horaActual);

            // Verificación de seguridad para evitar duplicar huecos si se relanza el proceso
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