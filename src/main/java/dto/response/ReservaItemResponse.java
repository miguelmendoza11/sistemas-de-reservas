package dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

/** Sub-DTO: NO referencia de vuelta al cliente => sin ciclos de serialización. */
public record ReservaItemResponse(
        UUID idReserva,
        String numeroHabitacion,
        LocalDateTime fechaInicio,
        LocalDateTime fechaFin,
        String estado,
        double costoTotal
) {}
