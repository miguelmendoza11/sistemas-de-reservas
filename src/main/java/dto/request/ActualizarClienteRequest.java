package dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/** PATCH: ambos campos son opcionales (null = "no cambiar"). */
public record ActualizarClienteRequest(
        @Size(min = 1, max = 100) String nombre,
        @Email @Size(max = 150) String email
) {}
