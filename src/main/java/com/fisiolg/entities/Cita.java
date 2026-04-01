package com.fisiolg.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad central que representa una reserva en el sistema de FisioLG.
 * CONTEXTO DE NEGOCIO:
 * - Las citas se estructuran en bloques fijos de 40 minutos.
 * - REGLA CLAVE: El cliente reserva un "hueco horario" (una cita), no a un profesional específico.
 * Si dos profesionales coinciden trabajando en el mismo horario, el sistema mostrará
 * disponibilidad, pero el cliente no sabe ni elige a qué profesional pertenece esa cita asignada.
 * - Toda la información vinculada (paciente, notas) está sujeta a la estricta política
 * de protección de datos y privacidad de la clínica.
 */
@Entity
@Table(name = "citas")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Fecha y hora de inicio de la cita.
     * Representa el comienzo exacto del bloque de 40 minutos reservado por el cliente.
     */
    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    /**
     * Turno al que pertenece esta cita dentro de la jornada laboral de la clínica.
     */
    @OneToOne
    @JoinColumn(name = "turno_id")
    private Turno turno;

    /**
     * Cliente que realiza la reserva. Los datos de esta relación deben manejarse
     * con la máxima confidencialidad (LOPD/GDPR).
     */
    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    /**
     * Fisioterapeuta asignado a la cita.
     * IMPORTANTE: Esta asignación es estrictamente interna. El cliente reserva la hora,
     * no al fisioterapeuta. En el calendario frontal, el cliente solo ve un hueco
     * disponible en una hora determinada.
     */
    @ManyToOne
    @JoinColumn(name = "fisio_id")
    private Fisio fisio;

    /**
     * Tipo de servicio o tratamiento que se va a realizar durante los 40 minutos.
     */
    @ManyToOne
    @JoinColumn(name = "servicio_id")
    private Servicio servicio;

    /**
     * Estado actual de la reserva (ej. CONFIRMADA, CANCELADA).
     */
    @Enumerated(EnumType.STRING)
    private EstadoCita estado;

    /**
     * Anotaciones clínicas o administrativas internas sobre la cita.
     * Contiene datos de salud sensibles: aplicar políticas de privacidad correspondientes.
     */
    @Column(columnDefinition = "TEXT")
    private String notas;

    /**
     * Indicador para el sistema de automatización (RecordatorioService).
     * Evita que se envíen correos duplicados al cliente, protegiendo su experiencia de usuario.
     */
    @Column(name = "recordatorio_enviado")
    private boolean recordatorioEnviado = false;

    public Cita() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public Turno getTurno() { return turno; }
    public void setTurno(Turno turno) { this.turno = turno; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public Fisio getFisio() { return fisio; }
    public void setFisio(Fisio fisio) { this.fisio = fisio; }

    public Servicio getServicio() { return servicio; }
    public void setServicio(Servicio servicio) { this.servicio = servicio; }

    public EstadoCita getEstado() { return estado; }
    public void setEstado(EstadoCita estado) { this.estado = estado; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public boolean isRecordatorioEnviado() { return recordatorioEnviado; }
    public void setRecordatorioEnviado(boolean recordatorioEnviado) { this.recordatorioEnviado = recordatorioEnviado; }
}