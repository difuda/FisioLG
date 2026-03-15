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


        Paciente paciente = pacienteRepository.findByEmail(datos.getEmail())
                .map(existente -> {
                    existente.setTelefono(datos.getTelefono());
                    existente.setNombre(datos.getNombre());
                    existente.setApellidos(datos.getApellidos());
                    return pacienteRepository.save(existente);
                })
                .orElseGet(() -> {
                    datos.setFechaRegistro(LocalDateTime.now());
                    datos.setAceptoRgpd(true);
                    return pacienteRepository.save(datos);
                });


        if (datos.getPassword() != null && !datos.getPassword().isEmpty()) {
            User usuario = userRepository.findByEmail(paciente.getEmail())
                    .orElse(new User());

            usuario.setEmail(paciente.getEmail());
            usuario.setUsername(paciente.getEmail());
            usuario.setPassword(datos.getPassword());
            usuario.setRol("PACIENTE");
            usuario.setPaciente(paciente);
            userRepository.save(usuario);
        }

        return paciente;
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