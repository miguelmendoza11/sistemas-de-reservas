package mapper;

import com.mycompany.sistema.reservas.dominio.modelo.Cliente;
import com.mycompany.sistema.reservas.dominio.modelo.Email;
import com.mycompany.sistema.reservas.dominio.modelo.Reserva;
import dto.request.ActualizarClienteRequest;
import dto.response.ClienteResponse;
import dto.response.ClienteResumenResponse;
import dto.response.ReservaItemResponse;
import java.util.Comparator;
import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ClienteMapper extends ReservaItemMapping {

    @Mapping(target = "email", source = "email.valor")
    ClienteResponse toResponse(Cliente cliente);

    // ---------- Tarea 2: resumen agregado ----------
    @Mapping(target = "email", source = "email.valor")
    @Mapping(target = "totalReservasRealizadas", expression = "java(cliente.getReservas().size())")
    @Mapping(target = "montoTotalGastado", source = "reservas", qualifiedByName = "sumarCostos")
    @Mapping(target = "reservasRecientes", source = "reservas", qualifiedByName = "reservasOrdenadas")
    ClienteResumenResponse toResumen(Cliente cliente);

    @Named("sumarCostos")
    default double sumarCostos(List<Reserva> reservas) {
        return reservas.stream().mapToDouble(Reserva::getTotal).sum();
    }

    /** Historial completo, de la reserva más reciente a la más antigua. */
    @Named("reservasOrdenadas")
    default List<ReservaItemResponse> reservasOrdenadas(List<Reserva> reservas) {
        return reservas.stream()
                .sorted(Comparator.comparing((Reserva r) -> r.getPeriodo().fechaInicio(), Comparator.reverseOrder()))
                .map(this::toItem)
                .toList();
    }

    // ---------- Tarea 3: actualización parcial (PATCH) ----------
    /**
     * ignoreByDefault = true => lista blanca: SOLO nombre y email pueden cambiar.
     * id, activo, penalizaciones y reservas quedan fuera (Cliente ni siquiera tiene setters
     * para los tres primeros). Con IGNORE, un null en el DTO nunca pisa el valor existente.
     */
    @BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "nombre", source = "nombre")
    @Mapping(target = "email", source = "email")
    void updateClienteFromDto(ActualizarClienteRequest dto, @MappingTarget Cliente entity);

    /** String -> Value Object Email (valida el formato en el constructor del record). */
    default Email toEmail(String valor) {
        return valor == null ? null : new Email(valor);
    }
}
