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

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Paciente> listarTodos() {
        return pacienteRepository.findAll();
    }

    @Transactional
    public Paciente registrarOActualizar(Paciente datos) {
        String emailLimpio = (datos.getEmail() != null) ? datos.getEmail().trim() : "";
        String dniLimpio = (datos.getDni() != null) ? datos.getDni().trim().toUpperCase() : "";


        Paciente paciente = pacienteRepository.findByDni(dniLimpio)
                .orElseGet(() -> pacienteRepository.findByEmail(emailLimpio).orElse(new Paciente()));


        if (datos.getNombre() != null) paciente.setNombre(datos.getNombre().toUpperCase().trim());
        if (datos.getApellidos() != null) paciente.setApellidos(datos.getApellidos().toUpperCase().trim());
        if (datos.getTelefono() != null) paciente.setTelefono(datos.getTelefono().trim());

        if (!dniLimpio.isEmpty()) paciente.setDni(dniLimpio);
        if (!emailLimpio.isEmpty()) paciente.setEmail(emailLimpio);


        if (datos.getPassword() != null && !datos.getPassword().isEmpty() && paciente.getUsuario() == null) {
            User nuevoUsuario = new User();
            nuevoUsuario.setUsername(emailLimpio);
            nuevoUsuario.setEmail(emailLimpio);
            nuevoUsuario.setPassword(datos.getPassword());
            nuevoUsuario.setRol("PACIENTE");

            nuevoUsuario = userRepository.save(nuevoUsuario);
            paciente.setUsuario(nuevoUsuario);
        }

        return pacienteRepository.save(paciente);
    }

    @Transactional
    public String generarTokenRecuperacion(String email) {
        return userRepository.findByEmail(email).map(u -> {
            String token = UUID.randomUUID().toString();
            u.setTokenRecuperacion(token);
            userRepository.save(u);
            return token;
        }).orElse(null);
    }

    @Transactional
    public boolean completarRecuperacion(String token, String nuevaPass) {
        return userRepository.findByTokenRecuperacion(token)
                .map(u -> {
                    u.setPassword(nuevaPass);
                    u.setTokenRecuperacion(null);
                    userRepository.save(u);
                    return true;
                }).orElse(false);
    }
}