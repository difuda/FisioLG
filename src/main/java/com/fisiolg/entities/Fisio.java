package com.fisiolg.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "fisio")
public class Fisio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    private String especialidad;

    @Column(name = "numero_colegiado", length = 50)
    private String numeroColegiado;

    @OneToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private User usuario;

    public Fisio() {}

    public Fisio(String nombre, String especialidad, String numeroColegiado) {
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.numeroColegiado = numeroColegiado;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getNumeroColegiado() { return numeroColegiado; }
    public void setNumeroColegiado(String numeroColegiado) { this.numeroColegiado = numeroColegiado; }

    public User getUsuario() { return usuario; }
    public void setUsuario(User usuario) { this.usuario = usuario; }
}