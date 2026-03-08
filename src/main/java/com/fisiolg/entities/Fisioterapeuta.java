package com.fisiolg.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "fisio")
public class Fisioterapeuta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String especialidad;
    private String colegiado;
    private String password;
    private String rol;

    public Fisioterapeuta() {
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getColegiado() { return colegiado; }
    public void setColegiado(String colegiado) { this.colegiado = colegiado; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

}

