package com.fisiolg.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pacientes")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String dni;

    @Column(nullable = false, length = 100)
    private String nombre;

    private String apellidos;
    private String telefono;
    private String direccion;

    @Column(unique = true, length = 150)
    private String email;

    @Column(name = "acepto_rgpd")
    private boolean aceptoRgpd = false;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @OneToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private User usuario;

    @Transient
    private String password;

    public Paciente() {}


    public Paciente(String nombre, String dni, String telefono, String email, boolean aceptoRgpd) {
        this.nombre = nombre;
        this.dni = dni;
        this.telefono = telefono;
        this.email = email;
        this.aceptoRgpd = aceptoRgpd;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean isAceptoRgpd() { return aceptoRgpd; }
    public void setAceptoRgpd(boolean aceptoRgpd) { this.aceptoRgpd = aceptoRgpd; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public User getUsuario() { return usuario; }
    public void setUsuario(User usuario) { this.usuario = usuario; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}