package ar.com.opendevsolutions.commons.rest;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import ar.com.opendevsolutions.commons.exception.ValidationException;
import ar.com.opendevsolutions.commons.service.EmailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value="/email",produces ="application/json")
public class EmailRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailRest.class);
    private EmailService commonsService;
    private String errorMessage = "Error desconocido en el servidor, por favor reintente o contacte al soporte";
    private HttpStatus errorStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    @Autowired
    public EmailRest(EmailService commonsService) {
        this.commonsService = commonsService;
    }

    public void setCommonsService(EmailService commonsService) {
        this.commonsService = commonsService;
    }

    public EmailService getCommonsService() {
        return commonsService;
    }

    @ExceptionHandler({ValidationException.class, Exception.class})
    public ResponseEntity<ApiError> handleException(Exception ex, WebRequest request) {
        logError(ex);

        if (ex instanceof ValidationException) {
            errorMessage = ex.getMessage();
            errorStatus = HttpStatus.BAD_REQUEST;
        }

        ApiError apiError = new ApiError(errorStatus,
                errorMessage,
                ex.getMessage());
        return ResponseEntity.status(errorStatus).body(apiError);
    }

    private void logError(Exception ex) {
        String error = String.format("Ha ocurrido un error en el servidor, %s", ex.getMessage());
        String stacktrace = Arrays.toString(ex.getStackTrace());
        LOGGER.error(error);
        LOGGER.error(stacktrace);
    }

    @ApiOperation(value = "Enviar Mail")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Mail enviado correctamente"),
            @ApiResponse(code = 401, message = "Sin Autorización"),
            @ApiResponse(code = 403, message = "No tiene permisos para acceder a este recurso"),
            @ApiResponse(code = 404, message = "Error, no existe el recurso")
    })
    @PostMapping(value = "/{user}/{from}/{to}/{pid}", produces = "application/json")
    public void enviarMail(@PathVariable(value = "user") String usuario,
                           @PathVariable(value = "from") String remitente,
                           @PathVariable(value = "to") String destinatario,
                           @PathVariable(value = "pid") String instanciaProceso,
                           @RequestParam(value = "tarea", required = false) String tarea,
                           @RequestBody(required = false) Map<String,Object> contenido) throws MessagingException, IOException {
        this.commonsService.enviarMail(usuario, remitente.toLowerCase(),  destinatario.toLowerCase(), instanciaProceso, contenido, tarea);
    }

    @ApiOperation(value = "Enviar Mail utilizando el from por default")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Mail enviado correctamente"),
            @ApiResponse(code = 401, message = "Sin Autorización"),
            @ApiResponse(code = 403, message = "No tiene permisos para acceder a este recurso"),
            @ApiResponse(code = 404, message = "Error, no existe el recurso")
    })
    @PostMapping(value = "/{user}/{to}/{pid}", produces = "application/json")
    public void enviarMailDefaultFrom(@PathVariable(value = "user") String usuario,
                           @PathVariable(value = "to") String destinatario,
                           @PathVariable(value = "pid") String instanciaProceso,
                           @RequestParam(value = "tarea", required = false) String tarea,
                           @RequestBody(required = false) Map<String,Object> contenido) throws MessagingException, IOException {
        this.commonsService.enviarMail(usuario, null, destinatario.toLowerCase(), instanciaProceso, contenido, tarea);
    }

}
