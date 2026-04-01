package com.fisiolg.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Servicio encargado de la gestión y envío de correos electrónicos de la clínica.
 * Garantiza el cumplimiento de las normativas de protección de datos (RGPD y LSSI).
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Cláusula legal obligatoria para cumplir con el RGPD y la LSSI en todas las comunicaciones.
     */
    private static final String AVISO_LEGAL = "\n\n---\n" +
            "AVISO IMPORTANTE: De conformidad con lo establecido en la Ley 34/2002 de 11 de julio... " +
            "(Contenido legal completo de Lucía Garza)";

    /**
     * Envía un correo electrónico genérico con el aviso legal adjunto.
     * @param destinatario Email del receptor.
     * @param asunto Título del mensaje.
     * @param cuerpo Contenido principal del correo.
     */
    public void enviarCorreo(String destinatario, String asunto, String cuerpo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatario);
        message.setSubject(asunto);
        message.setText(cuerpo + AVISO_LEGAL);
        mailSender.send(message);
    }

    /**
     * Alias para el envío de códigos de verificación.
     */
    public void enviarCodigo(String to, String code) {
        enviarCodigoVerificacion(to, code);
    }

    /**
     * Envía el código de seguridad necesario para que el cliente complete su reserva.
     * @param to Email del cliente.
     * @param code Código generado por el sistema.
     */
    public void enviarCodigoVerificacion(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Código de Verificación - Clínica FisioLG");
        message.setText("Tu código de verificación es: " + code +
                "\n\nEste código es necesario para completar tu reserva." + AVISO_LEGAL);
        mailSender.send(message);
    }

    /**
     * Envía la confirmación de una cita ya reservada, incluyendo la fecha y hora de la sesión de 40 minutos.
     * @param to Email del cliente.
     * @param fechaHora Fecha y hora formateada de la cita.
     */
    public void enviarConfirmacionCita(String to, String fechaHora) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Confirmación de Reserva - Clínica FisioLG");
        message.setText("¡Hola! Tu reserva ha sido confirmada para el día: " + fechaHora +
                "\n\nRecuerda que estamos en la calle Principal. Si necesitas anularla, hazlo con 24h de antelación." + AVISO_LEGAL);
        mailSender.send(message);
    }
}