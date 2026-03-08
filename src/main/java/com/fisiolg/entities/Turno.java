package com.fisiolg.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "turnos")
public class Turno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;

    private String motivo;

    @Enumerated(EnumType.STRING)
    private EstadoCita estado = EstadoCita.PENDIENTE;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "fisio_id")
    private Fisioterapeuta fisio;

    public Turno() {
    }


    public Long getId() {

        return id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public LocalDateTime getFechaHora() {

        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {

        this.fechaHora = fechaHora;
    }

    public String getMotivo() {

        return motivo;
    }

    public void setMotivo(String motivo) {

        this.motivo = motivo;
    }

    public EstadoCita getEstado() {

        return estado;
    }

    public void setEstado(EstadoCita estado) {

        this.estado = estado;
    }

    public Paciente getPaciente() {

        return paciente;
    }

    public void setPaciente(Paciente paciente) {

        this.paciente = paciente;
    }

    public Fisioterapeuta getFisio() {

        return fisio;
    }

    public void setFisio(Fisioterapeuta fisio) {

        this.fisio = fisio;
    }
}
