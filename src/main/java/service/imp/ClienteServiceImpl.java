package service.imp;

import com.mycompany.sistema.reservas.dominio.modelo.Cliente;
import com.mycompany.sistema.reservas.dominio.modelo.Email;
import dto.request.ActualizarClienteRequest;
import dto.request.CrearClienteRequest;
import dto.response.ClienteResponse;
import dto.response.ClienteResumenResponse;
import exception.RecursoNoEncontradoException;
import java.util.List;
import java.util.UUID;
import mapper.ClienteMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.ClienteRepository;
import service.ClienteService;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    public ClienteServiceImpl(ClienteRepository clienteRepository, ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
    }

    @Override
    @Transactional
    public ClienteResponse crear(CrearClienteRequest request) {
        Cliente cliente = new Cliente(request.nombre(), new Email(request.email()));
        return clienteMapper.toResponse(clienteRepository.save(cliente));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponse> listar() {
        return clienteRepository.findAll().stream().map(clienteMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponse obtener(UUID id) {
        return clienteMapper.toResponse(buscar(id));
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResumenResponse obtenerResumen(UUID id) {
        Cliente cliente = clienteRepository.findConReservasById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente", id));
        return clienteMapper.toResumen(cliente); // se mapea DENTRO de la transacción
    }

    @Override
    @Transactional
    public ClienteResponse actualizarParcial(UUID id, ActualizarClienteRequest request) {
        Cliente cliente = buscar(id);                                   // 1. cargar
        clienteMapper.updateClienteFromDto(request, cliente);            // 2. aplicar solo lo no nulo
        return clienteMapper.toResponse(clienteRepository.save(cliente)); // 3. guardar y responder
    }

    private Cliente buscar(UUID id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente", id));
    }
}
