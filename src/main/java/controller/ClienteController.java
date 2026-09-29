package controller;

import dto.request.ActualizarClienteRequest;
import dto.request.CrearClienteRequest;
import dto.response.ClienteResponse;
import dto.response.ClienteResumenResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import service.ClienteService;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // --- Endpoints base (prerrequisito del taller) ---

    @PostMapping
    public ResponseEntity<ClienteResponse> crear(@Valid @RequestBody CrearClienteRequest request) {
        ClienteResponse creado = clienteService.crear(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(creado.id()).toUri();
        return ResponseEntity.created(location).body(creado);                    // 201
    }

    @GetMapping
    public List<ClienteResponse> listar() {
        return clienteService.listar();
    }

    @GetMapping("/{id}")
    public ClienteResponse obtener(@PathVariable UUID id) {
        return clienteService.obtener(id);
    }

    // --- Tarea 2 ---

    @GetMapping("/{id}/resumen")
    public ClienteResumenResponse resumen(@PathVariable UUID id) {
        return clienteService.obtenerResumen(id);                                 // 200
    }

    // --- Tarea 3 ---

    @PatchMapping("/{id}")
    public ClienteResponse actualizarParcial(@PathVariable UUID id,
                                             @Valid @RequestBody ActualizarClienteRequest request) {
        return clienteService.actualizarParcial(id, request);                     // 200
    }
}
