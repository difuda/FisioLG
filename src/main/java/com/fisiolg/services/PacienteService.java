package com.fisiolg.services;

import com.fisiolg.entities.Paciente;
import com.fisiolg.entities.User;
import com.fisiolg.repositories.PacienteRepository;
import com.fisiolg.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

/**
 * Servicio principal para la gestión de Pacientes en FisioLG.
 * Gestiona el registro, actualización y manejo de credenciales de los clientes.
 * * CONTEXTO DE NEGOCIO: Los pacientes gestionados aquí son los clientes que
 * accederán al sistema para reservar su cita en bloques de 40 minutos.
 * Es vital recordar que el cliente reserva una "Cita" genérica en un tramo horario,
 * no selecciona directamente al profesional. Todos los datos personales tratados
 * en esta clase cumplen estrictamente con las políticas de privacidad y protección
 * de datos vigentes de la clínica.
 */
@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Recupera la lista completa de pacientes registrados en la base de datos de la clínica.
     */
    public List<Paciente> listarTodos() {
        return pacienteRepository.findAll();
    }

    /**
     * Registra un nuevo paciente o actualiza uno existente basándose en el DNI o el Email.
     * Aplica sanitización básica (mayúsculas y eliminación de espacios) para mantener la BD limpia.
     * Si se proporciona una contraseña y el paciente no tiene usuario previo, crea automáticamente
     * las credenciales de acceso seguras para que el cliente pueda entrar y gestionar sus citas.
     * * @param datos Objeto Paciente con la información a registrar/actualizar enviada desde el frontend.
     * @return El paciente persistido en la base de datos.
     */
    @Transactional
    public Paciente registrarOActualizar(Paciente datos) {
        // Sanitización de identificadores clave para evitar duplicados por errores tipográficos
        String emailLimpio = (datos.getEmail() != null) ? datos.getEmail().trim() : "";
        String dniLimpio = (datos.getDni() != null) ? datos.getDni().trim().toUpperCase() : "";

        // Busca si el paciente ya existe por DNI o Email; si no, instancia un objeto nuevo
        Paciente paciente = pacienteRepository.findByDni(dniLimpio)
                .orElseGet(() -> pacienteRepository.findByEmail(emailLimpio).orElse(new Paciente()));

        // Actualización de datos personales con formato estandarizado
        if (datos.getNombre() != null) paciente.setNombre(datos.getNombre().toUpperCase().trim());
        if (datos.getApellidos() != null) paciente.setApellidos(datos.getApellidos().toUpperCase().trim());
        if (datos.getTelefono() != null) paciente.setTelefono(datos.getTelefono().trim());

        if (!dniLimpio.isEmpty()) paciente.setDni(dniLimpio);
        if (!emailLimpio.isEmpty()) paciente.setEmail(emailLimpio);

        // Creación de credenciales de usuario (Login) vinculadas al paciente.
        // Este paso es crucial para habilitar el portal del cliente respetando la privacidad.
        if (datos.getPassword() != null && !datos.getPassword().isEmpty() && paciente.getUsuario() == null) {
            User nuevoUsuario = new User();
            nuevoUsuario.setUsername(emailLimpio); // El username es el email por defecto para facilitar el login
            nuevoUsuario.setEmail(emailLimpio);
            nuevoUsuario.setPassword(datos.getPassword());
            nuevoUsuario.setRol("PACIENTE"); // Asignación de rol estricto por seguridad

            nuevoUsuario = userRepository.save(nuevoUsuario);
            paciente.setUsuario(nuevoUsuario);
        }

        return pacienteRepository.save(paciente);
    }

    /**
     * Genera un token único y seguro (UUID) para el proceso de recuperación de contraseña,
     * permitiendo al paciente recuperar su acceso sin intervención manual de la clínica.
     *
     * @param email Email del usuario que solicita la recuperación.
     * @return El token generado, o null si el correo no corresponde a ningún usuario.
     */
    @Transactional
    public String generarTokenRecuperacion(String email) {
        return userRepository.findByEmail(email).map(u -> {
            String token = UUID.randomUUID().toString();
            u.setTokenRecuperacion(token);
            userRepository.save(u);
            return token;
        }).orElse(null);
    }

    /**
     * Valida el token de recuperación provisto y actualiza la contraseña del usuario.
     * Invalida el token inmediatamente después de su uso para garantizar la seguridad de la cuenta.
     * * @param token Token de seguridad enviado previamente al correo del usuario.
     * @param nuevaPass La nueva contraseña a establecer.
     * @return true si la recuperación fue exitosa, false si el token no es válido o ya fue usado.
     */
    @Transactional
    public boolean completarRecuperacion(String token, String nuevaPass) {
        return userRepository.findByTokenRecuperacion(token)
                .map(u -> {
                    u.setPassword(nuevaPass);
                    u.setTokenRecuperacion(null); // Medida de seguridad: limpieza del token tras su uso
                    userRepository.save(u);
                    return true;
                }).orElse(false);
    }
}