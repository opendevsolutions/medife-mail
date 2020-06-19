package ar.com.opendevsolutions.commons.mail;

import java.util.Map;

public class MailNotificacionAltaGrupoFamiliar extends MailNotificacion {

    public MailNotificacionAltaGrupoFamiliar(String usuario, String instanciaProceso,
                                             String destinatario, EmailStructure message, Map<String, Object> contenido) {
        this.usuario = usuario;
        this.instanciaProceso = instanciaProceso;
        this.destinatario = destinatario;
        this.message = message;
        this.contenido = contenido;
    }

    public MailNotificacionAltaGrupoFamiliar() {

    }

    public String obtenerTituloMail() {
        return "Alta de grupo familiar - Trámite: ";
    }
}
