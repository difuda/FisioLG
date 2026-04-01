package com.fisiolg.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad que representa al cliente/paciente de la clínica.
 * CONTEXTO DE NEGOCIO:
 * - Este es el usuario final que accede a la plataforma para reservar su cita.
 * - REGLA CLAVE: El paciente reserva exclusivamente un bloque horario de 40 minutos
 * según la disponibilidad mostrada, sin seleccionar ni conocer al profesional asignado.
 * - PRIVACIDAD: Almacena información personal sensible. El tratamiento de estos datos
 * está estrictamente ligado al cumplimiento de las políticas de privacidad y protección
 * de datos vigentes (RGPD).
 */
@Entity
@Table(name = "pacientes")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Documento de identidad único del paciente.
     * Dato de carácter sensible protegido bajo normativas de privacidad.
     */
    @Column(unique = true, nullable = false)
    private String dni;

    @Column(nullable = false, length = 100)
    private String nombre;

    private String apellidos;
    private String telefono;
    private String direccion;

    /**
     * Correo electrónico de contacto, utilizado como credencial de acceso principal
     * y para el envío automatizado de recordatorios de citas.
     */
    @Column(unique = true, length = 150)
    private String email;

    /**
     * Confirmación explícita de aceptación de las políticas de protección de datos.
     * Es obligatorio que esté a 'true' para operar legalmente con el cliente.
     */
    @Column(name = "acepto_rgpd")
    private boolean aceptoRgpd = false;

    /**
     * Registro temporal para auditorías de alta en el sistema.
     */
    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    /**
     * Relación con las credenciales de seguridad (Login) del cliente.
     * Permite al paciente acceder a su panel para gestionar sus citas de 40 minutos.
     */
    @OneToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private User usuario;

    /**
     * Campo transitorio utilizado únicamente para recibir la contraseña desde el frontend
     * durante el registro. No se guarda en esta tabla por motivos de seguridad
     * (se encripta y delega a la entidad User).
     */
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