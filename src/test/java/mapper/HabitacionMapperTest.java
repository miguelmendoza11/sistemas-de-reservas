package mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.sistema.reservas.dominio.modelo.Habitacion;
import com.mycompany.sistema.reservas.dominio.modelo.HabitacionEstandar;
import com.mycompany.sistema.reservas.dominio.modelo.NumeroHabitacion;
import com.mycompany.sistema.reservas.dominio.modelo.SuitePresidencial;
import dto.request.CrearSuitePresidencialRequest;
import dto.response.HabitacionEstandarResponse;
import dto.response.HabitacionResponse;
import dto.response.SuitePresidencialResponse;
import java.util.List;
import org.junit.jupiter.api.Test;

class HabitacionMapperTest {

    private final HabitacionMapper mapper = new HabitacionMapperImpl();

    @Test
    void suiteSeMapeaComoSuiteConSusCamposPropios() {
        Habitacion suite = new SuitePresidencial(new NumeroHabitacion("P05-501"), 2, 350.0, true, true);

        HabitacionResponse response = mapper.toResponse(suite);

        SuitePresidencialResponse s = assertInstanceOf(SuitePresidencialResponse.class, response);
        assertEquals("P05-501", s.numero());
        assertEquals(350.0, s.precioPorNoche());
        assertTrue(s.jacuzziPrivado());
        assertTrue(s.incluyeMayordomo());
    }

    @Test
    void estandarSeMapeaComoEstandarConSusCamposPropios() {
        Habitacion estandar = new HabitacionEstandar(new NumeroHabitacion("P01-101"), 2, 80.0, 2);

        HabitacionEstandarResponse e = assertInstanceOf(HabitacionEstandarResponse.class, mapper.toResponse(estandar));

        assertEquals("P01-101", e.numero());
        assertEquals(2, e.camasIndividuales());
    }

    @Test
    void listaPolimorficaSerializaElCampoTipo() throws Exception {
        List<HabitacionResponse> lista = mapper.toResponseList(List.of(
                new HabitacionEstandar(new NumeroHabitacion("P01-101"), 2, 80.0, 2),
                new SuitePresidencial(new NumeroHabitacion("P05-501"), 2, 350.0, true, true)));

        String json = new ObjectMapper()
                .writerFor(new TypeReference<List<HabitacionResponse>>() {})
                .writeValueAsString(lista);

        assertTrue(json.contains("\"tipo\":\"ESTANDAR\""), json);
        assertTrue(json.contains("\"tipo\":\"SUITE\""), json);
        assertTrue(json.contains("\"camasIndividuales\":2"), json);
        assertTrue(json.contains("\"jacuzziPrivado\":true"), json);
    }

    @Test
    void requestSeConvierteEnLaSubclaseConcreta() {
        SuitePresidencial suite = mapper.toEntity(
                new CrearSuitePresidencialRequest("P05-501", 350.0, 2, true, false));

        assertEquals("P05-501", suite.getNumero().valor());
        assertTrue(suite.isIncluyeMayordomo());
        assertFalse(suite.isJacuzziPrivado());
        assertNotNull(suite.getId());
    }

    @Test
    void numeroInvalidoLoRechazaElDominio() {
        assertThrows(IllegalArgumentException.class, () -> mapper.toEntity(
                new CrearSuitePresidencialRequest("501", 350.0, 2, true, true)));
    }
}
