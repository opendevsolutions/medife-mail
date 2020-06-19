package ar.com.opendevsolutions.commons.mail;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

public class MailNotificacionReclamoVenta extends MailNotificacion{

    String nombreTareaCargarReclamo = "Cargar Reclamo";
    String nombreTareaSupervisorAutorizarReclamo = "Supervisor Autorizar Reclamo";
    String nombreTareaABM = "ABM: Analizar reclamo";
    String nombreTareaRRHH = "RRHH: Analizar Reclamo";
    String nombreTareaSupervisorVerVentaDevuelta = "Supervisor: Ver Venta Devuelta";

    public MailNotificacionReclamoVenta(String usuario, String instanciaProceso,
                                        String destinatario, EmailStructure message, Map<String, Object> contenido) {
        this.usuario = usuario;
        this.instanciaProceso = instanciaProceso;
        this.destinatario = destinatario;
        this.message = message;
        this.contenido = contenido;
    }

    public MailNotificacionReclamoVenta() {
    }

    @Override
    public String construirBody() {
        String line1 = getCabeceraDeCuerpo();
        String line2 = " Usuario creador de trámite: " + usuario;
        String line3 = " Fecha y Hora: " + DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").format(ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")));
        String additionalMessage = contenido != null && contenido.containsKey("message")?(String) contenido.get("message"):"";
        String observaciones = contenido != null && contenido.containsKey("observaciones")? " Observaciones: " + (String) contenido.get("observaciones"):"";
        return additionalMessage + LINE_SEPARATOR +line1 + LINE_SEPARATOR + line2 + LINE_SEPARATOR + line3 + LINE_SEPARATOR + observaciones;
    }

    private String getCabeceraDeCuerpo() {
        String line1;
        if( Optional.ofNullable(tarea).orElse("").equals(nombreTareaSupervisorAutorizarReclamo) ){
            line1 = " El Trámite " + instanciaProceso + " en la tarea " + tarea + " ha sido rechazado. ";
        } else {
            if( Optional.ofNullable(tarea).orElse("").equals(nombreTareaSupervisorVerVentaDevuelta)){
                line1 = " El Trámite " + instanciaProceso + " se ha finalizado con éxito.";
            } else {
                line1 = " Trámite: " + instanciaProceso;
            }
        }
        return line1;
    }

    public String obtenerTituloMail() {
        return "Reclamar Venta - Tramite: "+instanciaProceso;
    }
}
