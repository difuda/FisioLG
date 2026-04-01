package com.fisiolg.entities;

import jakarta.persistence.*;
import java.time.LocalTime;

/**
 * Entidad que define los horarios generales de trabajo (disponibilidad) de cada profesional.
 * CONTEXTO DE NEGOCIO:
 * - Esta clase es el motor detrás del calendario que ve el cliente.
 * - A partir de la 'horaInicio' y 'horaFin', el sistema calculará y dividirá la jornada
 * en los bloques estrictos de 40 minutos que se ofrecen en la plataforma.
 * - REGLA CLAVE: Aunque cada registro de disponibilidad pertenece a un Fisio específico,
 * esta asignación es solo para la gestión interna de la clínica. El cliente que navega
 * por la web agrupará todas las disponibilidades y solo verá "huecos libres" de 40 mins,
 * sin saber qué profesional específico está detrás de ese turno.
 */
@Entity
@Table(name = "disponibilidad")
public class Disponibilidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Profesional al que pertenece este bloque horario.
     * Dato de gestión interna, opaco para el cliente final.
     */
    @ManyToOne
    @JoinColumn(name = "fisio_id")
    private Fisio fisio;

    /**
     * Día de la semana en formato numérico (1 = Lunes, 7 = Domingo).
     */
    @Column(name = "dia_semana", nullable = false)
    private Integer diaSemana; // 1 para Lunes, 7 para Domingo

    /**
     * Hora exacta a la que el profesional comienza su turno este día.
     * Base para iniciar el cálculo del primer bloque de 40 minutos.
     */
    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    /**
     * Hora exacta a la que el profesional finaliza su turno.
     * Marca el límite para el último bloque de 40 minutos disponible.
     */
    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;

    public Disponibilidad() {}

    public Disponibilidad(Fisio fisio, Integer diaSemana, LocalTime horaInicio, LocalTime horaFin) {
        this.fisio = fisio;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Fisio getFisio() { return fisio; }
    public void setFisio(Fisio fisio) { this.fisio = fisio; }

    public Integer getDiaSemana() { return diaSemana; }
    public void setDiaSemana(Integer diaSemana) { this.diaSemana = diaSemana; }

    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }

    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }
}