package service;

import com.mycompany.sistema.reservas.dominio.modelo.RangoFechas;
import dto.response.ReservaItemResponse;
import java.util.UUID;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author daferarte
 */
public interface ReservaService {
    ReservaItemResponse crearReserva(UUID clienteId, UUID habitacionId, RangoFechas periodo);
    ReservaItemResponse confirmarReserva(UUID reservaId);
    ReservaItemResponse cancelarReserva(UUID reservaId);
}
