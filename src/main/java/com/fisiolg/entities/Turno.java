package com.fisiolg.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "turnos")
public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;


    @ManyToOne
    @JoinColumn(name = "fisio_id", nullable = true)
    private Fisio fisio;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @Enumerated(EnumType.STRING)
    private EstadoCita estado = EstadoCita.LIBRE;

    @Column(columnDefinition = "TEXT")
    private String motivo;


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

