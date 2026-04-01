package com.fisiolg.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entidad central para la gestión de acceso, autenticación y seguridad de FisioLG.
 * CONTEXTO DE NEGOCIO:
 * - Gestiona el acceso de dos perfiles clave:
 * 1. Pacientes: Acceden para reservar sus bloques horarios de 40 minutos (sin ver al profesional).
 * 2. Fisios/Admin: Acceden al backoffice para gestionar internamente a quién pertenece cada bloque reservado.
 * - PRIVACIDAD EXTREMA: Esta clase maneja credenciales y tokens. El uso de la anotación
 * @JsonIgnore es una medida de seguridad crítica para evitar que contraseñas o datos
 * vinculados se filtren accidentalmente en las respuestas de la API pública (cumplimiento RGPD).
 */
@Entity
@Table(name = "usuarios")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de usuario único para el inicio de sesión (suele coincidir con el email para los pacientes).
     */
    @Column(unique = true, nullable = false, length = 150)
    private String username;

    /**
     * Contraseña encriptada del usuario.
     * La anotación @JsonIgnore garantiza que jamás viaje al frontend en formato texto.
     */
    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false, length = 150)
    private String email;

    /**
     * Define los permisos en el sistema (ej. "PACIENTE", "FISIO", "ADMIN").
     * Separa el portal público de reservas del panel de gestión interna.
     */
    @Column(nullable = false, length = 50)
    private String rol;

    /**
     * Código de un solo uso (One Time Password) para verificaciones de seguridad de doble factor.
     * Oculto de la API pública por seguridad.
     */
    @JsonIgnore
    @Column(name = "otp_code", length = 6)
    private String otpCode;

    /**
     * Token de seguridad seguro (UUID) para restablecer la contraseña en caso de olvido.
     */
    @JsonIgnore
    @Column(name = "token_recuperacion")
    private String tokenRecuperacion;

    /**
     * Permite deshabilitar el acceso de un usuario (ej. un fisio que deja la clínica)
     * sin tener que borrar sus datos históricos de la base de datos.
     */
    private boolean activo = true;

    @Column(name = "fecha_registro", updatable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    /**
     * Relación con los datos personales y clínicos si el usuario es un cliente.
     * Oculto en la serialización JSON para evitar exponer el historial clínico a través del perfil de usuario.
     */
    @JsonIgnore
    @OneToOne(mappedBy = "usuario")
    private Paciente paciente;

    /**
     * Relación con los datos profesionales si el usuario es un trabajador de la clínica.
     */
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