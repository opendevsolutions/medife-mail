package ar.com.opendevsolutions.commons.service;

import ar.com.opendevsolutions.commons.mail.MailNotificacion;
import ar.com.opendevsolutions.commons.mail.MailNotificacionAltaGrupoFamiliar;
import ar.com.opendevsolutions.commons.mail.MailNotificacionReclamoVenta;

public enum EmailType {
    RECLAMOVENTA, ALTAGRUPOFAMILIAR, BAJAGRUPOFAMILIAR, ALTARESPONSABLEPAGO;

    MailNotificacion getMailNotificacion() {
        switch (this){
            case RECLAMOVENTA: return new MailNotificacionReclamoVenta();
            case ALTAGRUPOFAMILIAR: return new MailNotificacionAltaGrupoFamiliar();
            case BAJAGRUPOFAMILIAR: return new MailNotificacion();
            case ALTARESPONSABLEPAGO: return new MailNotificacion();
                default: return new MailNotificacion();
        }
    }
}
