package mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.mycompany.sistema.reservas.dominio.modelo.Cliente;
import com.mycompany.sistema.reservas.dominio.modelo.Email;
import com.mycompany.sistema.reservas.dominio.modelo.HabitacionEstandar;
import com.mycompany.sistema.reservas.dominio.modelo.NumeroHabitacion;
import com.mycompany.sistema.reservas.dominio.modelo.RangoFechas;
import com.mycompany.sistema.reservas.dominio.modelo.Reserva;
import com.mycompany.sistema.reservas.dominio.modelo.SuitePresidencial;
import dto.request.ActualizarClienteRequest;
import dto.response.ClienteResumenResponse;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class ClienteMapperTest {

    private final ClienteMapper mapper = new ClienteMapperImpl();

    private Cliente clienteConDosPenalizaciones() {
        Cliente c = new Cliente("Ana Gómez", new Email("ana@correo.com"));
        c.registrarPenalizacion();
        c.registrarPenalizacion();
        return c;
    }

    // ---------- PATCH ----------

    @Test
    void patchSoloNombreNoTocaEmailNiEstadoNiPenalizaciones() {
        Cliente c = clienteConDosPenalizaciones();

        mapper.updateClienteFromDto(new ActualizarClienteRequest("Nuevo Nombre Modificado", null), c);

        assertEquals("Nuevo Nombre Modificado", c.getNombre());
        assertEquals("ana@correo.com", c.getEmail().valor());
        assertTrue(c.isActivo());
        assertEquals(2, c.getPenalizaciones());
    }

    @Test
    void patchSoloEmailNoTocaElNombre() {
        Cliente c = clienteConDosPenalizaciones();

        mapper.updateClienteFromDto(new ActualizarClienteRequest(null, "nuevo@correo.com"), c);

        assertEquals("Ana Gómez", c.getNombre());
        assertEquals("nuevo@correo.com", c.getEmail().valor());
        assertEquals(2, c.getPenalizaciones());
    }

    @Test
    void patchConEmailInvalidoLoRechazaElValueObject() {
        Cliente c = clienteConDosPenalizaciones();

        assertThrows(IllegalArgumentException.class,
                () -> mapper.updateClienteFromDto(new ActualizarClienteRequest(null, "no-es-un-email"), c));
        assertEquals("ana@correo.com", c.getEmail().valor());
    }

    // ---------- RESUMEN ----------

    @Test
    void resumenCalculaTotalesYListaSinCiclos() {
        Cliente c = new Cliente("Ana Gómez", new Email("ana@correo.com"));
        LocalDateTime base = LocalDateTime.of(2026, 11, 1, 14, 0);

        // 100 x 2 noches = 200
        Reserva r1 = new Reserva(c, new HabitacionEstandar(new NumeroHabitacion("P01-101"), 2, 100.0, 1),
                new RangoFechas(base, base.plusDays(2)));
        // 350 x 2 noches = 700 (más reciente)
        Reserva r2 = new Reserva(c, new SuitePresidencial(new NumeroHabitacion("P05-501"), 2, 350.0, true, true),
                new RangoFechas(base.plusDays(10), base.plusDays(12)));
        c.agregarReserva(r1);
        c.agregarReserva(r2);

        ClienteResumenResponse resumen = mapper.toResumen(c);

        assertEquals("ana@correo.com", resumen.email());
        assertEquals(2, resumen.totalReservasRealizadas());
        assertEquals(900.0, resumen.montoTotalGastado());
        assertEquals(2, resumen.reservasRecientes().size());
        assertEquals("P05-501", resumen.reservasRecientes().get(0).numeroHabitacion()); // más reciente primero
        assertEquals("PENDIENTE", resumen.reservasRecientes().get(0).estado());
        assertEquals(700.0, resumen.reservasRecientes().get(0).costoTotal());
    }
}
