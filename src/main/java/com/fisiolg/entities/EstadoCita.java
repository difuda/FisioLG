package com.fisiolg.entities;

/**
 * Enumeración que define el ciclo de vida y los estados posibles de un bloque horario en la clínica.
 * CONTEXTO DE NEGOCIO:
 * - Estos estados se aplican a los huecos de 40 minutos que se generan en el calendario.
 * - Son vitales para la lógica del frontend: dictan qué horas se muestran al cliente como
 * seleccionables (ej. LIBRE/DISPONIBLE) y cuáles se ocultan (ej. CONFIRMADA).
 * - REGLA CLAVE: El estado pertenece al "hueco" temporal, manteniendo la opacidad sobre
 * qué profesional específico (Fisio) pasará a estar ocupado.
 */
public enum EstadoCita {

    /**
     * Estado por defecto o no inicializado.
     */
    NONE,

    /**
     * El bloque de 40 minutos está completamente vacío y listo para ser mostrado en la web
     * para que un cliente lo reserve.
     */
    LIBRE,

    /**
     * El cliente ha seleccionado el hueco y está en proceso de reserva
     * (bloqueo temporal para evitar que otro cliente reserve la misma hora a la vez).
     */
    PENDIENTE,

    /**
     * La reserva es oficial. El cliente tiene su bloque de 40 minutos asegurado.
     * En este estado, los datos personales del paciente ya están vinculados y protegidos.
     */
    CONFIRMADA,

    /**
     * La cita ha sido cancelada (por el cliente o la clínica).
     * Este estado permite mantener un registro histórico sin borrar el dato de la BD.
     */
    ANULADA,

    /**
     * Estado alternativo para designar que un turno de un profesional está operativo
     * y suma disponibilidad al calendario global que ve el cliente.
     */
    DISPONIBLE
}