package service;

import dto.request.ActualizarClienteRequest;
import dto.request.CrearClienteRequest;
import dto.response.ClienteResponse;
import dto.response.ClienteResumenResponse;
import java.util.List;
import java.util.UUID;

public interface ClienteService {
    ClienteResponse crear(CrearClienteRequest request);
    List<ClienteResponse> listar();
    ClienteResponse obtener(UUID id);
    ClienteResumenResponse obtenerResumen(UUID id);
    ClienteResponse actualizarParcial(UUID id, ActualizarClienteRequest request);
}
