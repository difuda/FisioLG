package com.fisiolg.entities;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "disponibilidad")
public class Disponibilidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fisio_id")
    private Long fisioId;

    @Column(name = "dia_semana")
    private Integer diaSemana;

    @Column(name = "hora_inicio")
    private LocalTime horaInicio;

    @Column(name = "hora_fin")
    private LocalTime horaFin;


    public Disponibilidad() {
    }


    public Long getId() {
        return id; }
    public void setId(Long id) {
        this.id = id; }

    public Long getFisioId() {
        return fisioId; }
    public void setFisioId(Long fisioId) {
        this.fisioId = fisioId; }

    public Integer getDiaSemana() {
        return diaSemana; }
    public void setDiaSemana(Integer diaSemana) {
        this.diaSemana = diaSemana; }

    public LocalTime getHoraInicio() {
        return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio; }

    public LocalTime getHoraFin() {
        return horaFin; }
    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin; }
}
