package service.imp;


import com.mycompany.sistema.reservas.dominio.modelo.Cliente;
import com.mycompany.sistema.reservas.dominio.modelo.Habitacion;
import com.mycompany.sistema.reservas.dominio.modelo.RangoFechas;
import com.mycompany.sistema.reservas.dominio.modelo.Reserva;
import dto.response.ReservaItemResponse;
import mapper.ReservaMapper;
import org.springframework.transaction.annotation.Transactional;
import exception.RecursoNoEncontradoException;
import java.util.UUID;
import org.springframework.stereotype.Service;
import repository.ReservaRepository;
import repository.ClienteRepository;
import repository.HabitacionRepository;

import service.ReservaService;
import service.ReservaService;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author daferarte
 */
@Service
public class ReservaServiceImpl implements ReservaService {

    // Dependencias declaradas como interfaces inmutables
    private final ReservaRepository reservaRepository;
    private final HabitacionRepository habitacionRepository;
    private final ClienteRepository clienteRepository;
    private final ReservaMapper reservaMapper;

    // Inyección de dependencias por constructor (Buena práctica: sin @Autowired explícito en Spring moderno)
    public ReservaServiceImpl(ReservaRepository reservaRepository,
                              HabitacionRepository habitacionRepository,
                              ClienteRepository clienteRepository,
                              ReservaMapper reservaMapper) {
        this.reservaRepository = reservaRepository;
        this.habitacionRepository = habitacionRepository;
        this.clienteRepository = clienteRepository;
        this.reservaMapper = reservaMapper;
    }

    @Override
    @Transactional
    public ReservaItemResponse crearReserva(UUID clienteId, UUID habitacionId, RangoFechas periodo) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente", clienteId));

        Habitacion habitacion = habitacionRepository.findById(habitacionId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Habitación", habitacionId));

        // El dominio rico protege sus propias reglas al instanciarse
        Reserva nuevaReserva = new Reserva(cliente, habitacion, periodo);
        return reservaMapper.toItem(reservaRepository.save(nuevaReserva));
    }

    @Override
    @Transactional
    public ReservaItemResponse confirmarReserva(UUID reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Reserva", reservaId));

        // Delegación del comportamiento a la entidad rica (Semana 4 y 5)
        reserva.confirmar();

        return reservaMapper.toItem(reservaRepository.save(reserva));
    }

    @Override
    @Transactional
    public ReservaItemResponse cancelarReserva(UUID reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Reserva", reservaId));

        reserva.cancelar();

        return reservaMapper.toItem(reservaRepository.save(reserva));
    }
}
