package repository;

import com.mycompany.sistema.reservas.dominio.modelo.Cliente;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {

    // Cliente + reservas + habitación en UNA consulta (evita N+1 y LazyInitializationException)
    @EntityGraph(attributePaths = {"reservas", "reservas.habitacion"})
    Optional<Cliente> findConReservasById(UUID id);
}
