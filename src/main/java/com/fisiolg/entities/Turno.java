package com.fisiolg.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad que representa un bloque horario específico (un hueco) generado en el calendario.
 * CONTEXTO DE NEGOCIO:
 * - Esta es la materialización de los bloques de 40 minutos. El sistema genera estos
 * turnos diariamente en base a la Disponibilidad de la clínica.
 * - REGLA CLAVE: Esto es exactamente lo que el cliente reserva. El paciente ve un
 * "Turno" a una "fechaHora" concreta en estado LIBRE y lo bloquea, sin saber en
 * ningún momento qué profesional (Fisio) está vinculado a este registro.
 */
@Entity
@Table(name = "turnos")
public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Fecha y hora exacta en la que comienza este bloque de 40 minutos.
     */
    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    /**
     * Profesional asignado a este bloque de tiempo.
     * IMPORTANTE: Esta relación es estrictamente para organización interna (cuadrantes).
     * El frontend agrupa los turnos por hora y oculta este dato al paciente para cumplir
     * con la regla de negocio de reserva por tramo horario.
     */
    @ManyToOne
    @JoinColumn(name = "fisio_id", nullable = true)
    private Fisio fisio;

    /**
     * Paciente que ha reservado este bloque horario.
     * Dato sujeto a las normativas de protección de datos (RGPD) de la clínica.
     */
    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    /**
     * Estado actual de este bloque de 40 minutos (ej. LIBRE, CONFIRMADA).
     * Determina si este hueco se renderiza como disponible en la web pública o si se oculta.
     */
    @Enumerated(EnumType.STRING)
    private EstadoCita estado = EstadoCita.LIBRE;

    /**
     * Motivo o notas breves sobre la reserva de este turno.
     * Al poder contener información de salud o sintomatología, debe tratarse
     * como información confidencial.
     */
    @Column(columnDefinition = "TEXT")
    private String motivo;

    /**
     * Relación con la reserva formal (Cita) una vez que el cliente confirma el proceso.
     * Un turno confirmado siempre desemboca en la creación de una Cita.
     */
    @OneToOne(mappedBy = "turno")
    private Cita cita;

    public Turno() {}

    public Turno(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
        this.estado = EstadoCita.LIBRE;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public Fisio getFisio() { return fisio; }
    public void setFisio(Fisio fisio) { this.fisio = fisio; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public EstadoCita getEstado() { return estado; }
    public void setEstado(EstadoCita estado) { this.estado = estado; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public Cita getCita() { return cita; }
    public void setCita(Cita cita) { this.cita = cita; }
}