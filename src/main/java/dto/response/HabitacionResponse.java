package dto.response;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.UUID;

/**
 * Contrato polimórfico de salida. Jackson añade el campo "tipo" ("ESTANDAR" | "SUITE")
 * en cada elemento, también dentro de una List<HabitacionResponse>.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "tipo")
@JsonSubTypes({
        @JsonSubTypes.Type(value = HabitacionEstandarResponse.class, name = "ESTANDAR"),
        @JsonSubTypes.Type(value = SuitePresidencialResponse.class, name = "SUITE")
})
public sealed interface HabitacionResponse
        permits HabitacionEstandarResponse, SuitePresidencialResponse {

    UUID id();
    String numero();
    double precioPorNoche();
    int capacidadMaxima();
}
