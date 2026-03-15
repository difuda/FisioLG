package com.fisiolg.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;


    public void enviarCorreo(String destinatario, String asunto, String cuerpo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatario);
        message.setSubject(asunto);
        message.setText(cuerpo);
        mailSender.send(message);
    }


    public void enviarCodigo(String to, String code) {
        enviarCodigoVerificacion(to, code);
    }

    public void enviarCodigoVerificacion(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Código de Verificación - Clínica FisioLG");
        message.setText("Tu código de verificación es: " + code +
                "\n\nEste código es necesario para completar tu reserva.");
        mailSender.send(message);
    }


    public void enviarConfirmacionCita(String to, String fechaHora) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Confirmación de Reserva - Clínica FisioLG");
        message.setText("¡Hola! Tu reserva ha sido confirmada para el día: " + fechaHora +
                "\n\nRecuerda que estamos en la calle Principal. Si necesitas anularla, hazlo con 24h de antelación.");
        mailSender.send(message);
    }
}