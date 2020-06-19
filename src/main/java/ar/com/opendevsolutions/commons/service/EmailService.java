package ar.com.opendevsolutions.commons.service;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import ar.com.opendevsolutions.commons.mail.EmailStructure;
import ar.com.opendevsolutions.commons.mail.MailNotificacion;

@Service
@Transactional
public class EmailService {


    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    @Value("${smtp.host.noauth}")
    String smtpHostNoAuth;

    @Value("${smtp.host.auth}")
    String smtpHostAuth;

    @Value("${smtp.host.auth.account}")
    String mailAccountAuth;

    @Value("${smtp.host.auth.pass}")
    String mailKey;

    @Value("${smtp.useauth}")
    Boolean useAuthentication;

    @Value("${smtp.host.auth.port}")
    String smtpAuthPort;

    String smtpNoAuthPort = "25";

    @Value("${smtp.defaultfrom}")
    String defaultFrom;

    RestTemplate restTemplate;

    public EmailService() {
        this.restTemplate = new RestTemplate();
    }

    public void enviarMail(EmailType tipoMail, String usuario, String remitente,
                           String destinatario, String instanciaProceso, Map<String, Object> contenido, String tarea ) throws MessagingException, IOException {

        if( destinatario != null && !destinatario.isEmpty() && !destinatario.toLowerCase().equals("null") && !destinatario.toLowerCase().equals("undefined")) {
            EmailStructure message = new EmailStructure();
            construirMail(tipoMail, usuario, Optional.ofNullable(remitente).orElse(defaultFrom), destinatario, instanciaProceso, contenido, tarea, message);
            if (useAuthentication){
                sendAuthenticatedEmail(usuario, remitente, destinatario, message);
            } else {
                Session session = conexionSMTP(useAuthentication);
                MimeMessage emailWithoutAuth = new MimeMessage(session);
                emailWithoutAuth.setFrom(new InternetAddress(message.getFrom()));
                emailWithoutAuth.setRecipients(Message.RecipientType.TO, InternetAddress.parse(message.getTo()));
                emailWithoutAuth.setSubject(message.getSubject(),"UTF-8");
                emailWithoutAuth.setText(message.getText(), "UTF-8");
                emailWithoutAuth.setSentDate(message.getSentDate());
                Transport.send(emailWithoutAuth);
            }
        } else {
            LOGGER.error(" Se ha evitado enviar un email  correspondiente al tramite " + instanciaProceso + " con un destinatario vacio con el remitente "  + Optional.ofNullable(remitente).orElse(defaultFrom));
        }


    }

    private void sendAuthenticatedEmail(String usuario, String remitente, String destinatario, EmailStructure message) throws MessagingException, IOException {
        Email email = EmailBuilder.startingBlank().from(Optional.ofNullable(remitente).orElse(defaultFrom.toLowerCase()))
                .to(usuario.toLowerCase(), destinatario.toLowerCase())
                .withSubject(message.getSubject())
                .withPlainText(message.getText())
                .buildEmail();

        Mailer mailer = MailerBuilder
                .withSMTPServer(smtpHostAuth, Integer.valueOf(smtpAuthPort), mailAccountAuth, mailKey)
                .withTransportStrategy(TransportStrategy.SMTP)
                .withDebugLogging(true)
                .async()
                .buildMailer();

        mailer.sendMail(email);
    }

    private void construirMail(EmailType tipoMail,
                               String usuario, String remitente, String destinatario,
                               String instanciaProceso, Map<String, Object> contenido, String tarea,
                               EmailStructure message) throws MessagingException {
        MailNotificacion mailNotificacion = (tipoMail==null)? new MailNotificacion():tipoMail.getMailNotificacion();
        mailNotificacion.setearValores(usuario, instanciaProceso, remitente, destinatario, message, contenido, tarea, mailAccountAuth);
        mailNotificacion.construirMail(message, remitente, destinatario, mailNotificacion.obtenerTituloMail(), mailNotificacion.construirBody());
    }


    private Session conexionSMTP(Boolean auth) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "false");
        props.put("mail.smtp.starttls.enable", "false");
        props.put("mail.smtp.host", smtpHostNoAuth);
        props.put("mail.smtp.port", smtpNoAuthPort);
        props.put("mail.debug", "true");
        return  Session.getDefaultInstance(props);
    }


}
