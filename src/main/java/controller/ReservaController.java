package controller;

import com.mycompany.sistema.reservas.dominio.modelo.RangoFechas;
import dto.request.CrearReservaRequest;
import dto.response.ReservaItemResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.ReservaService;


@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping
    public ResponseEntity<ReservaItemResponse> crear(@Valid @RequestBody CrearReservaRequest request) {
        RangoFechas periodo = new RangoFechas(request.fechaInicio(), request.fechaFin());
        ReservaItemResponse creada = reservaService.crearReserva(request.clienteId(), request.habitacionId(), periodo);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);           // 201
    }

    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<ReservaItemResponse> confirmar(@PathVariable UUID id) {
        return ResponseEntity.ok(reservaService.confirmarReserva(id));            // 200
    }
}
