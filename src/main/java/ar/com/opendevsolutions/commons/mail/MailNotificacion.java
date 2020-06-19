package ar.com.opendevsolutions.commons.mail;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Value;

public class MailNotificacion {
    static final String LINE_SEPARATOR = "\n";
    static final String tituloDefault = "Notificación de mail para trámite";

    String usuario;
    String instanciaProceso;
    String destinatario;
    String remitente;
    EmailStructure message;
    Map<String, Object> contenido;

    @Value("${smtp.host.auth.account}")
    String mailAccountAuth;
    String tarea;

    public MailNotificacion() {
    }

    public MailNotificacion(String usuario, String instanciaProceso,
                            String destinatario, EmailStructure message, Map<String, Object> contenido) {
        this.usuario = usuario;
        this.instanciaProceso = instanciaProceso;
        this.destinatario = destinatario;
        this.message = message;
        this.contenido = contenido;
    }

    public String obtenerTituloMail() {
        return tituloDefault;
    }

    public String construirBody() {
        String line1 = " Trámite: " + instanciaProceso;
        String line2 = " Usuario creador de trámite: " + usuario;
        String line3 = " Fecha y Hora: " + DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").format(ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")));
        String additionalMessage = contenido != null && contenido.containsKey("message")?(String) contenido.get("message"):"";
        return additionalMessage + LINE_SEPARATOR +line1 + LINE_SEPARATOR + line2 + LINE_SEPARATOR + line3 + LINE_SEPARATOR;
    }

    public void construirMail(EmailStructure message, String from, String to, String title, String body)
            throws MessagingException {
        message.setFrom(from);
        //message.setFrom(new InternetAddress(from));
        //message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setRecipient(to);
        message.setSubject(title,"UTF-8");
        message.setText(body, "UTF-8");
        message.setSentDate(new Date());
    }

    public void setearValores(String usuario, String instanciaProceso, String remitente, String destinatario,
                              EmailStructure message, Map<String, Object> contenido, String tarea, String mailAccountAuth) {
        this.usuario = usuario;
        this.instanciaProceso = instanciaProceso;
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.message = message;
        this.contenido = contenido;
        this.mailAccountAuth = mailAccountAuth;
        this.tarea = tarea;
    }
}
