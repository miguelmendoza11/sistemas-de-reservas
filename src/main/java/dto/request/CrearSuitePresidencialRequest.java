package dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record CrearSuitePresidencialRequest(
        @NotBlank @Pattern(regexp = "^P\\d{2}-\\d{3}$", message = "Formato inválido. Use PXX-XXX (Ej: P03-302)")
        String numero,
        @Positive double precioPorNoche,
        @Positive int capacidadMaxima,
        boolean incluyeMayordomo,
        boolean jacuzziPrivado
) {}
