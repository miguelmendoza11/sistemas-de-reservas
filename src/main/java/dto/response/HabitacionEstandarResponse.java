package dto.response;

import java.util.UUID;

public record HabitacionEstandarResponse(
        UUID id,
        String numero,
        double precioPorNoche,
        int capacidadMaxima,
        int camasIndividuales
) implements HabitacionResponse {}
