package com.fisiolg.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "usuarios")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 150)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false, length = 150)
    private String email;

    @Column(nullable = false, length = 50)
    private String rol;

    @JsonIgnore
    @Column(name = "otp_code", length = 6)
    private String otpCode;

    @JsonIgnore
    @Column(name = "token_recuperacion")
    private String tokenRecuperacion;

    private boolean activo = true;

    @Column(name = "fecha_registro", updatable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @JsonIgnore
    @OneToOne(mappedBy = "usuario")
    private Paciente paciente;

    @JsonIgnore
    @OneToOne(mappedBy = "usuario")
    private Fisio fisio;

    public User() {}

    public User(String username, String password, String email, String rol) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.rol = rol;
        this.activo = true;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getOtpCode() { return otpCode; }
    public void setOtpCode(String otpCode) { this.otpCode = otpCode; }

    public String getTokenRecuperacion() { return tokenRecuperacion; }
    public void setTokenRecuperacion(String tokenRecuperacion) { this.tokenRecuperacion = tokenRecuperacion; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public Fisio getFisio() { return fisio; }
    public void setFisio(Fisio fisio) { this.fisio = fisio; }
}