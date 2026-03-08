package com.fisiolg.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {


    @Autowired
    private JavaMailSender mailSender;

    public void enviarCodigo(String destinatario, String codigo) {
        SimpleMailMessage message = new SimpleMailMessage();


        message.setFrom("fisioterapialuciagarza@gmail.com");
        message.setTo(destinatario);
        message.setSubject("Código de Verificación - Clínica FisioLG");


        message.setText("Hola,\n\n" +
                "Para continuar con su reserva en FisioLG, use el siguiente código:\n\n" +
                "CÓDIGO: " + codigo + "\n\n" +
                "Si no ha solicitado este código, ignore este correo.");


        mailSender.send(message);
    }
}
