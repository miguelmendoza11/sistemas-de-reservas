package mapper;

import com.mycompany.sistema.reservas.dominio.modelo.Habitacion;
import com.mycompany.sistema.reservas.dominio.modelo.HabitacionEstandar;
import com.mycompany.sistema.reservas.dominio.modelo.NumeroHabitacion;
import com.mycompany.sistema.reservas.dominio.modelo.SuitePresidencial;
import dto.request.CrearHabitacionEstandarRequest;
import dto.request.CrearSuitePresidencialRequest;
import dto.response.HabitacionEstandarResponse;
import dto.response.HabitacionResponse;
import dto.response.SuitePresidencialResponse;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.SubclassMapping;

@Mapper(componentModel = "spring")
public interface HabitacionMapper {

    // ---------- Request -> Entidad ----------
    // Las entidades ricas solo se construyen por constructor (validan sus invariantes y
    // generan su propio id), por eso la creación es explícita en lugar de por setters.
    default HabitacionEstandar toEntity(CrearHabitacionEstandarRequest request) {
        return new HabitacionEstandar(
                new NumeroHabitacion(request.numero()),
                request.capacidadMaxima(),
                request.precioPorNoche(),
                request.camasIndividuales());
    }

    default SuitePresidencial toEntity(CrearSuitePresidencialRequest request) {
        return new SuitePresidencial(
                new NumeroHabitacion(request.numero()),
                request.capacidadMaxima(),
                request.precioPorNoche(),
                request.incluyeMayordomo(),
                request.jacuzziPrivado());
    }

    // ---------- Entidad -> Response ----------
    @Mapping(target = "numero", source = "numero.valor")
    HabitacionEstandarResponse toEstandarResponse(HabitacionEstandar habitacion);

    @Mapping(target = "numero", source = "numero.valor")
    SuitePresidencialResponse toSuiteResponse(SuitePresidencial habitacion);

    /**
     * Despacho polimórfico en tiempo de ejecución: MapStruct genera un instanceof por subclase
     * y delega en el método específico, conservando camasIndividuales / jacuzziPrivado / etc.
     */
    @SubclassMapping(source = HabitacionEstandar.class, target = HabitacionEstandarResponse.class)
    @SubclassMapping(source = SuitePresidencial.class, target = SuitePresidencialResponse.class)
    HabitacionResponse toResponse(Habitacion habitacion);

    List<HabitacionResponse> toResponseList(List<Habitacion> habitaciones);
}
