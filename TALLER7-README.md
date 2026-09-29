# Taller 7 – Contratos polimórficos, proyecciones anidadas y PATCH seguro

Solución adaptada a **este** repositorio (dominio rico con Value Objects, patrón State, sin Lombok).

## Cómo aplicarlo
- **Opción A (zip):** copia el contenido de `sistema-de-reservas-taller7.zip` sobre tu repo (reemplaza archivos).
- **Opción B (git):** `git apply taller7.patch` en la raíz del repo.
- Deja de versionar los artefactos: `git rm -r --cached target reservas_log.txt` (el `.gitignore` ya los excluye).

## Cómo ejecutarlo
Requiere **JDK 21+** y PostgreSQL con la base `hotel_reservas_db` (o define `DB_URL`, `DB_USER`, `DB_PASSWORD`).
```
mvn test                 # tests de mapeo (polimorfismo, resumen, PATCH)
mvn spring-boot:run      # API en http://localhost:8080
```
Luego ejecuta `solicitudes-avanzadas.http` de arriba hacia abajo (extensión *REST Client* de VS Code).

## Qué se agregó / cambió
| Área | Archivos |
|---|---|
| Arranque | `SistemaReservasApplication` (no existía clase `@SpringBootApplication`), `pom.xml` (MapStruct 1.6.3, validation, test, plugin Boot, Java 21), `application.properties` (credenciales por variable de entorno), `.gitignore` |
| Tarea 1 | `dto.response.HabitacionResponse` (sealed) + `HabitacionEstandarResponse` + `SuitePresidencialResponse`; `dto.request.CrearHabitacionEstandarRequest` / `CrearSuitePresidencialRequest`; `mapper.HabitacionMapper` (`@SubclassMapping`); `HabitacionController` + `HabitacionService(Impl)` |
| Tarea 2 | `dto.response.ClienteResumenResponse`, `ReservaItemResponse`; `mapper.ClienteMapper` (`@Named` + Streams + `expression`); `ClienteRepository.findConReservasById` (`@EntityGraph`) |
| Tarea 3 | `dto.request.ActualizarClienteRequest`; `ClienteMapper.updateClienteFromDto` (`@MappingTarget` + `IGNORE`); `PATCH /api/clientes/{id}` |
| Prerrequisito | `POST/GET /api/clientes`, `GET /api/clientes/{id}`, `POST /api/reservas` (antes solo existía `PATCH .../confirmar`) |
| HTTP | `exception.GlobalExceptionHandler`: 400 (datos inválidos), 404, 409 (estado inválido / duplicado) |
| Evidencias | `solicitudes-avanzadas.http` (4 pruebas + errores esperados), tests en `src/test/java/mapper` |

## Cambios al dominio existente (mínimos)
- `Habitacion` ahora es **abstracta** (con `JOINED`, una fila base sin subtipo rompería el mapeo polimórfico). Los 4 `Main*` que hacían `new Habitacion(...)` ahora usan `HabitacionEstandar` / `SuitePresidencial`.
- `Cliente`: se agregaron `setNombre` (validado) y `setEmail` (delegando en `actualizarEmail`). `id`, `activo` y `penalizaciones` **siguen sin setter**.
- `ReservaService` / `ReservaController` devuelven DTOs (antes entidad / `String`): ninguna `@Entity` llega a la capa web. `PATCH /api/reservas/{id}/confirmar` ahora responde JSON (`ReservaItemResponse`).
- `ReservaServiceImpl`: `@Transactional` de Spring y `RecursoNoEncontradoException` (404) en vez de `IllegalArgumentException`.

## Decisiones que conviene defender
- **Entrada polimórfica:** un record por tipo y un endpoint por tipo (`/estandar`, `/suites`) en el mismo controller. Las entidades se crean por constructor (validan invariantes y generan su `id`), por eso `toEntity` es un `default` explícito.
- **Salida polimórfica:** `sealed interface` + `@JsonTypeInfo` → `"tipo": "ESTANDAR" | "SUITE"`; `@SubclassMapping` genera el despacho `instanceof`.
- **Sin recursión:** las entidades nunca se serializan y `ReservaItemResponse` no referencia al cliente.
- **PATCH seguro:** `ignoreByDefault = true` = lista blanca (solo `nombre` y `email`). El enunciado pide `ignore = true` explícito para `id/activo/penalizaciones/reservas`, pero `Cliente` no expone esas propiedades como escribibles y MapStruct rechaza `@Mapping(target=...)` sobre propiedades inexistentes; la lista blanca cubre lo mismo y `ClienteMapperTest` lo demuestra.
- **Resumen:** `montoTotalGastado` suma todas las reservas; `reservasRecientes` devuelve el historial completo, más reciente primero (agrega `.limit(N)` en `reservasOrdenadas` si tu profesor lo prefiere). Se mapea dentro de la transacción y con `@EntityGraph` (sin N+1).

## Limitaciones / qué NO verifiqué
No pude compilar ni ejecutar el proyecto (mi entorno no tiene Maven ni acceso a Maven Central), ni probarlo contra PostgreSQL. Solo revisé la sintaxis. Ejecuta `mvn test` primero. Si el compilador se queja:
- `@SubclassMapping` con la interfaz sellada como tipo de retorno: reemplaza el método por
```java
default HabitacionResponse toResponse(Habitacion h) {
    if (h instanceof SuitePresidencial s) return toSuiteResponse(s);
    if (h instanceof HabitacionEstandar e) return toEstandarResponse(e);
    throw new IllegalArgumentException("Tipo de habitación no soportado: " + h.getClass());
}
```
- Con JDK 25 instalado sigue funcionando `release 21`; no cambies a 25 mientras uses Boot 3.3.x.

## Pendiente (fuera del alcance del taller)
Paquetes duplicados `repositorio`/`repository`, dos `NotificadorService`, enum `modelo.EstadoReserva` sin uso (homónimo de la interfaz de `comportamentales.state`).
