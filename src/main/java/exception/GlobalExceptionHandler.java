package exception;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Traduce las excepciones del dominio rico a códigos HTTP con semántica correcta
 * (en lugar de un 500 genérico).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ProblemDetail> noEncontrado(RecursoNoEncontradoException ex) {
        return respuesta(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /** Invariantes de los Value Objects y constructores (email inválido, número PXX-XXX, etc.). */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> datosInvalidos(IllegalArgumentException ex) {
        return respuesta(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /** Transiciones de estado inválidas (confirmar una reserva cancelada, cliente inactivo...). */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ProblemDetail> estadoInvalido(IllegalStateException ex) {
        return respuesta(HttpStatus.CONFLICT, ex.getMessage());
    }

    /** Violaciones de unicidad (email o número de habitación repetidos). */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> conflictoDeDatos(DataIntegrityViolationException ex) {
        return respuesta(HttpStatus.CONFLICT, "El recurso ya existe o viola una restricción de integridad");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> validacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> errores.putIfAbsent(e.getField(), e.getDefaultMessage()));
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Solicitud inválida");
        problema.setProperty("errores", errores);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problema);
    }

    private ResponseEntity<ProblemDetail> respuesta(HttpStatus status, String detalle) {
        return ResponseEntity.status(status).body(ProblemDetail.forStatusAndDetail(status, detalle));
    }
}
