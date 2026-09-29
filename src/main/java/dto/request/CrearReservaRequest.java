package dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public record CrearReservaRequest(
        @NotNull UUID clienteId,
        @NotNull UUID habitacionId,
        @NotNull LocalDateTime fechaInicio,
        @NotNull LocalDateTime fechaFin
) {}
