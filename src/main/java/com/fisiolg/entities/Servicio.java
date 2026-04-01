package com.fisiolg.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Entidad que representa los diferentes tratamientos o servicios que ofrece la clínica.
 * CONTEXTO DE NEGOCIO:
 * - El paciente selecciona el servicio que desea recibir durante su proceso de reserva.
 * - REGLA CLAVE: Independientemente del servicio elegido, la reserva se encaja en el
 * sistema de bloques horarios de 40 minutos. El cliente sigue reservando ese "hueco",
 * sin elegir al profesional específico que ejecutará el servicio.
 * - El catálogo de servicios es público, por lo que no contiene datos sensibles de pacientes.
 */
@Entity
@Table(name = "servicios")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre comercial del tratamiento (ej. "Fisioterapia Deportiva").
     */
    private String nombre;

    /**
     * Descripción detallada del servicio, mostrada al cliente en el portal web
     * para que sepa en qué consistirán sus 40 minutos de sesión.
     */
    private String descripcion;

    /**
     * Precio del servicio. Se utiliza BigDecimal para garantizar la precisión matemática
     * en la facturación y futura pasarela de pagos.
     */
    private BigDecimal precio;

    /**
     * Duración estimada del servicio.
     * NOTA DE NEGOCIO: En la operativa estándar de FisioLG, este valor suele ser
     * de 40 minutos para mantener la sincronía perfecta con el calendario de citas.
     */
    @Column(name = "duracion_minutos")
    private Integer duracionMinutos;

    /**
     * Flag (bandera) lógica para habilitar o deshabilitar temporalmente un servicio
     * sin borrarlo de la base de datos. Esto es vital para mantener la integridad
     * del historial de citas pasadas de los clientes.
     */
    private Boolean activo;


    public Servicio() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Integer getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(Integer duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}