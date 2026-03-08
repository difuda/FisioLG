package com.fisiolg.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "citas")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "paciente_id")
    private Long pacienteId;

    @Column(name = "fisio_id")
    private Long fisioId;

    @Column(name = "servicio_id")
    private Long servicioId;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "estado_id")
    private EstadoCita estadoId;

    @Column(name = "estado")
    private String estado;

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;

    @Column(name = "recordatorio_enviado")
    private Boolean recordatorioEnviado;

    @Column(name = "notas_clinicas", columnDefinition = "TEXT")
    private String notasClinicas;


    public Cita() {}



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }

    public Long getFisioId() {
        return fisioId;
    }

    public void setFisioId(Long fisioId) {
        this.fisioId = fisioId;
    }

    public Long getServicioId() {
        return servicioId;
    }

    public void setServicioId(Long servicioId) {
        this.servicioId = servicioId;
    }

    public EstadoCita getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(EstadoCita estadoId) {
        this.estadoId = estadoId;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Boolean isRecordatorioEnviado() {
        return recordatorioEnviado;
    }

    public void setRecordatorioEnviado(Boolean recordatorioEnviado) {
        this.recordatorioEnviado = recordatorioEnviado;
    }

    public String getNotasClinicas() {
        return notasClinicas;
    }

    public void setNotasClinicas(String notasClinicas) {
        this.notasClinicas = notasClinicas;
    }
}