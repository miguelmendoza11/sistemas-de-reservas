package service.imp;

import dto.request.CrearHabitacionEstandarRequest;
import dto.request.CrearSuitePresidencialRequest;
import dto.response.HabitacionEstandarResponse;
import dto.response.HabitacionResponse;
import dto.response.SuitePresidencialResponse;
import exception.RecursoNoEncontradoException;
import java.util.List;
import java.util.UUID;
import mapper.HabitacionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.HabitacionRepository;
import service.HabitacionService;

@Service
public class HabitacionServiceImpl implements HabitacionService {

    private final HabitacionRepository habitacionRepository;
    private final HabitacionMapper habitacionMapper;

    public HabitacionServiceImpl(HabitacionRepository habitacionRepository, HabitacionMapper habitacionMapper) {
        this.habitacionRepository = habitacionRepository;
        this.habitacionMapper = habitacionMapper;
    }

    @Override
    @Transactional
    public HabitacionEstandarResponse crearEstandar(CrearHabitacionEstandarRequest request) {
        return habitacionMapper.toEstandarResponse(habitacionRepository.save(habitacionMapper.toEntity(request)));
    }

    @Override
    @Transactional
    public SuitePresidencialResponse crearSuite(CrearSuitePresidencialRequest request) {
        return habitacionMapper.toSuiteResponse(habitacionRepository.save(habitacionMapper.toEntity(request)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<HabitacionResponse> listar() {
        return habitacionMapper.toResponseList(habitacionRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public HabitacionResponse obtener(UUID id) {
        return habitacionRepository.findById(id)
                .map(habitacionMapper::toResponse)
                .orElseThrow(() -> new RecursoNoEncontradoException("Habitación", id));
    }
}
