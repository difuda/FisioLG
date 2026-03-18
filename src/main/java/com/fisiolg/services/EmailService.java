package com.fisiolg.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;


    private static final String AVISO_LEGAL = "\n\n---\n" +
            "AVISO IMPORTANTE: De conformidad con lo establecido en la Ley 34/2002 de 11 de julio, de Servicios de la Sociedad de la Información y del Comercio Electrónico, le comunicamos que en el supuesto que no desee seguir recibiendo las comunicaciones e informaciones que viene recibiendo mediante este sistema de comunicación electrónica, nos lo indique con un correo a fisioterapialuciagarza@gmail.com, para que de esta forma sus datos personales sean dados de baja de nuestra base de datos. Su solicitud será accionada en un plazo de 10 días desde su envío. En el supuesto que no recibamos contestación expresa por su parte, entenderemos que acepta y autoriza que nuestra empresa siga realizando las referidas comunicaciones. Este mensaje se dirige exclusivamente a su destinatario y puede contener información privilegiada o confidencial. Si no es Ud. el destinatario indicado, queda notificado que la utilización, divulgación y/o copia sin autorización está prohibida en virtud de la legislación vigente.\n\n" +
            "De acuerdo con lo dispuesto en el RGPD (UE) 2016/679 del Parlamento Europeo y La Ley Orgánica 3/2018, de 5 de diciembre, de Protección de Datos Personales y garantía de los derechos digitales, se le informa que sus datos personales, recogidos en cualquiera de nuestros medios, van a ser conservados y tratados del mismo modo y para el fin con que se recogieron, así como para gestionar las solicitudes que se hagan a LUCIA GARZA VAZQUEZ. La base legítima para el tratamiento es el consentimiento del interesado o la existencia de un contrato o acuerdo previo. Puede ejercer los derechos que ostenta, así como presentar las reclamaciones pertinentes ante la Agencia Española de Protección de Datos. Puede obtener más información en la siguiente dirección: LUCIA GARZA VAZQUEZ , ROSALIA DE CASTRO, N16 1B, 32500, CARBALLIÑO, O (OURENSE), Email: fisioterapialuciagarza@gmail.com.";


    public void enviarCorreo(String destinatario, String asunto, String cuerpo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatario);
        message.setSubject(asunto);
        message.setText(cuerpo + AVISO_LEGAL);
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
                "\n\nEste código es necesario para completar tu reserva." + AVISO_LEGAL);
        mailSender.send(message);
    }


    public void enviarConfirmacionCita(String to, String fechaHora) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Confirmación de Reserva - Clínica FisioLG");
        message.setText("¡Hola! Tu reserva ha sido confirmada para el día: " + fechaHora +
                "\n\nRecuerda que estamos en la calle Principal. Si necesitas anularla, hazlo con 24h de antelación." + AVISO_LEGAL);
        mailSender.send(message);
    }
}