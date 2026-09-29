package service;

import dto.request.CrearHabitacionEstandarRequest;
import dto.request.CrearSuitePresidencialRequest;
import dto.response.HabitacionEstandarResponse;
import dto.response.HabitacionResponse;
import dto.response.SuitePresidencialResponse;
import java.util.List;
import java.util.UUID;

public interface HabitacionService {
    HabitacionEstandarResponse crearEstandar(CrearHabitacionEstandarRequest request);
    SuitePresidencialResponse crearSuite(CrearSuitePresidencialRequest request);
    List<HabitacionResponse> listar();
    HabitacionResponse obtener(UUID id);
}
