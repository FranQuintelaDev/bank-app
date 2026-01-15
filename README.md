# Bank App - API de Solicitudes de Préstamos 

Una API REST para gestionar solicitudes de préstamos. Permite crear solicitudes, consultarlas y modificar su estado siguiendo un flujo definido.

## Cómo ejecutar el proyecto

### Requisitos
- Java 17 o superior
- Maven 3.8+ (o usar el wrapper incluido `mvnw`)

### Ejecutar la aplicación

```bash
# Clonar y entrar al directorio
cd bank-app

# Compilar (genera el código de la API desde el OpenAPI spec)
./mvnw clean compile

# Ejecutar
./mvnw spring-boot:run
```

La aplicación arrancará en `http://localhost:8080`

### Ejecutar tests

```bash
./mvnw test
```

### Ejecutar la App Con Docker

```bash
docker-compose up --build
```

### Endpoints disponibles

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/loans` | Lista paginada de solicitudes |
| POST | `/loans` | Crear nueva solicitud |
| GET | `/loans/{id}` | Obtener solicitud por ID |
| PATCH | `/loans/{id}` | Actualizar estado de solicitud |

También puedes acceder a:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- H2 Console: `http://localhost:8080/h2-console` (user: `sa`, sin password)

## Arquitectura

El proyecto sigue **arquitectura hexagonal** (ports & adapters). La idea es que el dominio sea el centro y no dependa de frameworks ni infraestructura.

```
src/main/java/com/demo/bank_app/
├── domain/                    # El núcleo - reglas de negocio
│   ├── model/                 # Entidades (LoanRequest, LoanRequestStatus)
│   ├── port/                  # Interfaces (contratos)
│   └── exception/             # Excepciones de dominio
│
├── application/               # Casos de uso
│   └── useCase/               # Implementación de la lógica de aplicación
│
└── infrastructure/            # Adaptadores 
    ├── controller/            # REST API 
    ├── persistence/           # JPA/H2 
    ├── mapper/                # Conversión entre DTOs y dominio
    └── exception/             # Manejo global de excepciones
```

### Decisiones técnicas

**API First con OpenAPI Generator**
- El contrato está en `loan-api.yaml` y genera las interfaces del controller automáticamente
- Así nos aseguramos de que la implementación siempre coincide con la especificación y podrian trabajar en paralelo FE y BE (en un proyecto real)

**Flujo de estados con validación**
- El cambio de estado está validado en el dominio (`LoanRequest.updateStatus()`)
- PENDING → APPROVED/REJECTED → CANCELLED
- Si intentas una transición inválida, salta `InvalidStateTransitionException`

**Excepciones centralizadas**
- `GlobalExceptionHandler` captura todas las excepciones y devuelve `ProblemDetail` 
- El controller se queda limpio, sin try-catch por todos lados

**Tests**
- Unitarios con JUnit + Mockito (lógica de dominio y casos de uso)
- Integración con MockMvc (flujo completo HTTP → DB)
- Los JSON de respuesta esperada están en archivos separados (`src/test/resources/json/`)

## Mejoras pendientes

Con más tiempo añadiría:

- **Logs estructurados** - Meter logs de entrada/salida en cada capa para trazabilidad. Tipo: "LoanRequestUseCase.save() - input: {...}, output: {...}". Muy útil para debuggear cuando algo falla entre capas y no sabes dónde se perdió o transformó mal el dato.

- **Caché** - Si las consultas crecen, cachear las búsquedas por ID con redis.

---

*Desarrollado como prueba técnica - Enero 2026*

