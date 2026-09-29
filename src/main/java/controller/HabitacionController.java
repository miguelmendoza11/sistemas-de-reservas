package controller;

import dto.request.CrearHabitacionEstandarRequest;
import dto.request.CrearSuitePresidencialRequest;
import dto.response.HabitacionEstandarResponse;
import dto.response.HabitacionResponse;
import dto.response.SuitePresidencialResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import service.HabitacionService;

@RestController
@RequestMapping("/api/habitaciones")
public class HabitacionController {

    private final HabitacionService habitacionService;

    public HabitacionController(HabitacionService habitacionService) {
        this.habitacionService = habitacionService;
    }

    @PostMapping("/estandar")
    public ResponseEntity<HabitacionEstandarResponse> crearEstandar(
            @Valid @RequestBody CrearHabitacionEstandarRequest request) {
        HabitacionEstandarResponse creada = habitacionService.crearEstandar(request);
        return ResponseEntity.created(location(creada.id())).body(creada);      // 201
    }

    @PostMapping("/suites")
    public ResponseEntity<SuitePresidencialResponse> crearSuite(
            @Valid @RequestBody CrearSuitePresidencialRequest request) {
        SuitePresidencialResponse creada = habitacionService.crearSuite(request);
        return ResponseEntity.created(location(creada.id())).body(creada);      // 201
    }

    /** Lista polimórfica homogénea: cada elemento incluye "tipo": "ESTANDAR" | "SUITE". */
    @GetMapping
    public List<HabitacionResponse> listar() {
        return habitacionService.listar();
    }

    @GetMapping("/{id}")
    public HabitacionResponse obtener(@PathVariable UUID id) {
        return habitacionService.obtener(id);
    }

    private URI location(UUID id) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/habitaciones/{id}").buildAndExpand(id).toUri();
    }
}
