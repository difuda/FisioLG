package com.fisiolg.entities;

import jakarta.persistence.*;

/**
 * Entidad que representa a los profesionales (fisioterapeutas) de la clínica.
 * CONTEXTO DE NEGOCIO:
 * - REGLA CLAVE: En la plataforma orientada al cliente, la figura del 'Fisio'
 * es transparente/invisible. El cliente reserva un bloque de 40 minutos en la clínica,
 * no a un fisioterapeuta concreto.
 * - Esta entidad se utiliza estrictamente para la gestión interna: asignación de citas
 * en el backoffice, control de disponibilidad general y acceso al sistema por parte
 * de los trabajadores.
 */
@Entity
@Table(name = "fisio")
public class Fisio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre completo del profesional para uso en paneles de administración internos.
     */
    @Column(nullable = false, length = 100)
    private String nombre;

    /**
     * Especialidad del fisioterapeuta (ej. Deportiva, Neurológica).
     * Útil para métricas internas o asignaciones específicas manuales desde recepción.
     */
    private String especialidad;

    /**
     * Número de colegiado obligatorio por ley para la prestación de servicios sanitarios.
     * Dato legal y de facturación interno.
     */
    @Column(name = "numero_colegiado", length = 50)
    private String numeroColegiado;

    /**
     * Credenciales de acceso del profesional al sistema (Backoffice).
     * Permite al fisio entrar con su propio usuario para ver su agenda de citas
     * asignadas en sus bloques de 40 minutos.
     */
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