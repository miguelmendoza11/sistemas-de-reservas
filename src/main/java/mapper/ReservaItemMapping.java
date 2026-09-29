package mapper;

import comportamentales.state.EstadoReserva;
import com.mycompany.sistema.reservas.dominio.modelo.EstadoReservaConverter;
import com.mycompany.sistema.reservas.dominio.modelo.Reserva;
import dto.response.ReservaItemResponse;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Configuración de mapeo compartida (sin @Mapper): la heredan ReservaMapper y ClienteMapper,
 * así el sub-DTO se mapea igual en ambos sitios sin duplicar código.
 */
public interface ReservaItemMapping {

    @Mapping(target = "idReserva", source = "id")
    @Mapping(target = "numeroHabitacion", source = "habitacion.numero.valor")
    @Mapping(target = "fechaInicio", source = "periodo.fechaInicio")
    @Mapping(target = "fechaFin", source = "periodo.fechaFin")
    @Mapping(target = "estado", source = "estado", qualifiedByName = "estadoATexto")
    @Mapping(target = "costoTotal", source = "total")
    ReservaItemResponse toItem(Reserva reserva);

    /** El estado es un objeto (patrón State): se reutiliza el converter de JPA => PENDIENTE|CONFIRMADA|CANCELADA. */
    @Named("estadoATexto")
    default String estadoATexto(EstadoReserva estado) {
        return new EstadoReservaConverter().convertToDatabaseColumn(estado);
    }
}
