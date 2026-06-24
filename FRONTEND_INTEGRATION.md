# Guía de Integración Frontend - Módulo Veterinario

> **Propósito:** Documentar el contrato actual de la API para que el frontend consuma correctamente el backend (refactor de Servicios ya aplicado en `main`).

---

## Estado Actual del Refactor

El backend consolidó el concepto de "Servicios" en una entidad comercial. **El refactor ya está en `main`** y los endpoints del controller de veterinarios y citas ya devuelven/aceptan el modelo nuevo.

| Antes | Ahora | Estado |
|---|---|---|
| `TipoCita` enum para "qué puede hacer un veterinario" | Eliminado del flujo real | El enum `TipoCita` sigue presente en el código (`entity/enums/TipoCita.java`) pero **no es usado por ninguna entidad**. Es código muerto. |
| `TipoCita` enum en `Cita.tipoCita` | Reemplazado por `Servicio` (ManyToOne) | ✅ Migrado en V29. La columna `tipo_cita` ya no existe. |
| `Set<TipoServicio>` en `Veterinario.serviciosHabilitados` | `Set<Servicio>` (ManyToMany) | ✅ Migrado en V28. La join-table vieja fue dropeada en V27. |
| DTOs separados `VeterinarioCuentaRequest` + `VeterinarioPerfilRequest` | `VeterinarioRequest` único con `servicioIds` | ⚠️ Los DTOs viejos **siguen en el código pero no los usa ningún controller**. El controller usa `VeterinarioRequest`. |

### Lo que el frontend debe hacer

- Los veterinarios ya no tienen "tipos de cita habilitados" como strings enum.
- Ahora tienen una lista de **objetos `Servicio`** (con `id`, `nombre`, `descripcion`, `precio`).
- Al crear un veterinario, se envían **IDs de servicios existentes** (no enums).
- Al crear una cita, se envía el **`servicioId`** (no enum).
- El backend valida automáticamente que el veterinario ofrezca el servicio.

---

## Cambios Breaking por Endpoint

### 🔴 1. `GET /api/v1/veterinarios` - Listar veterinarios

`veterinariosHabilitados` → `servicios`

```typescript
// ❌ ANTES
{
  "id": 1,
  "nombreCompleto": "Juan Perez",
  "matricula": "VET-2024-001",
  "serviciosHabilitados": ["CONSULTA", "VACUNACION", "CIRUGIA"]  // enum array
}

// ✅ AHORA
{
  "id": 1,
  "nombreCompleto": "Juan Perez",
  "matricula": "VET-2024-001",
  "servicios": [
    { "id": 1, "nombre": "Consulta general", "descripcion": "...", "precio": 5000.0 },
    { "id": 2, "nombre": "Vacunación", "descripcion": "...", "precio": 3000.0 }
  ]
}
```

### 🔴 2. `POST /api/v1/veterinarios` - Crear veterinario

`serviciosHabilitados` → `servicioIds`

```typescript
// ❌ ANTES
{
  "nombre": "Juan",
  "apellido": "Perez",
  "email": "juan.perez@petstore.com",
  "password": "securePassword123",
  "matricula": "VET-2024-001",
  "especialidad": "Cirugía General",
  "serviciosHabilitados": ["CONSULTA", "VACUNACION", "CIRUGIA"]  // ❌
}

// ✅ AHORA
{
  "nombre": "Juan",
  "apellido": "Perez",
  "email": "juan.perez@petstore.com",
  "password": "securePassword123",
  "telefono": "351-555-1234",
  "matricula": "VET-2024-001",
  "especialidad": "Cirugía General",
  "bio": "Médico veterinario con más de 10 años de experiencia en cirugía general y emergencia.",
  "servicioIds": [1, 2, 3]  // ✅ IDs de servicios existentes
}
```

> **⚠️ Importante:** los IDs deben existir en la tabla `servicios`. Si envías un ID inexistente, el backend responde `404`.

### 🔴 3. `PUT /api/v1/veterinarios/{id}` - Actualizar veterinario

Mismo cambio que POST: `serviciosHabilitados` → `servicioIds`.

### 🔴 4. `GET /api/v1/citas/{id}` y listados

`tipoCita` → `servicio` (objeto completo)

```typescript
// ❌ ANTES
{
  "id": 1,
  "mascotaId": 5,
  "veterinarioId": 3,
  "tipoCita": "CONSULTA",  // ❌ string enum
  "fechaHora": "2026-07-15T10:00:00",
  "estado": "PENDIENTE"
}

// ✅ AHORA
{
  "id": 1,
  "mascotaId": 5,
  "mascotaNombre": "Max",
  "veterinarioId": 3,
  "veterinarioNombre": "Juan Perez",
  "veterinarioAvatar": "https://...",
  "clienteId": 7,
  "clienteNombre": "Cliente Demo",
  "servicio": {  // ✅ objeto completo
    "id": 1,
    "nombre": "Consulta general",
    "descripcion": "Revisión general de la mascota",
    "precio": 5000.0,
    "veterinarios": []  // poblado solo en /api/v1/servicios
  },
  "fechaHora": "2026-07-15T10:00:00",
  "duracionMinutos": 30,
  "estado": "PENDIENTE",
  "notas": "Control de rutina",
  "motivoCancelacion": null,
  "fechaCreacion": "2026-06-23T...",
  "pagado": false
}
```

### 🔴 5. `POST /api/v1/citas` - Crear cita

`tipoCita` → `servicioId`

```typescript
// ❌ ANTES
{
  "mascotaId": 1,
  "veterinarioId": 5,
  "tipoCita": "CONSULTA",  // ❌
  "fechaHora": "2026-07-15T10:00:00",
  "duracionMinutos": 30
}

// ✅ AHORA
{
  "mascotaId": 1,
  "veterinarioId": 5,
  "servicioId": 1,  // ✅ ID del servicio
  "fechaHora": "2026-07-15T10:00:00",
  "duracionMinutos": 30,
  "notas": "Control de rutina"
}
```

> **⚠️ Validación del backend:** si el `servicioId` no está en los `servicios` del veterinario, devuelve `400 Bad Request` con mensaje `El veterinario no ofrece este servicio`.

### 🟡 6. `PATCH /api/v1/veterinarios/{id}/activo`

```typescript
// ✅ AHORA
{
  "activo": true  // boolean estricto, NO string
}
```

Si envías `"activo": "true"` (string) será rechazado con `400`.

### 🟡 7. `PATCH /api/v1/citas/{id}/estado`

```typescript
// Body
{
  "estado": "CONFIRMADA",  // o CANCELADA, COMPLETADA, NO_ASISTIO, etc.
  "motivo": "Cliente canceló por lluvia"  // requerido si estado es CANCELADA
}
```

> El campo `motivo` se guarda en `Cita.motivoCancelacion` (columna agregada en V30).

### 🟢 8. Endpoints SIN cambios

- `GET /api/v1/veterinarios/{id}/horarios`
- `PUT /api/v1/veterinarios/{id}/horarios`
- `GET /api/v1/veterinarios/{id}/bloqueos`
- `POST /api/v1/veterinarios/{id}/bloqueos`
- `DELETE /api/v1/veterinarios/{id}/bloqueos/{bloqueoId}`
- `GET /api/v1/veterinarios/todos`
- `DELETE /api/v1/veterinarios/{id}`
- `GET /api/v1/servicios`
- `GET /api/v1/servicios/veterinario/{veterinarioId}`
- `POST /api/v1/servicios`
- `PUT /api/v1/servicios/{id}`
- `DELETE /api/v1/servicios/{id}`

---

## Modelos TypeScript

### Nuevos tipos

```typescript
// types/servicio.ts
export interface Servicio {
  id: number;
  nombre: string;
  descripcion?: string;
  precio: number;
  veterinarios?: Veterinario[];  // poblado solo en GET /api/v1/servicios
}
```

### Tipos actualizados

```typescript
// types/veterinario.ts
export interface Veterinario {
  id: number;
  usuarioId: number;
  nombreCompleto: string;
  avatar?: string;
  email: string;
  telefono?: string;
  matricula: string;
  especialidad: string;
  bio?: string;
  activo: boolean;
  servicios: Servicio[];  // ✅ array de objetos
  fechaCreacion: string;
}

export interface CrearVeterinarioPayload {
  nombre: string;
  apellido: string;
  email: string;
  password: string;        // min 6 chars
  telefono?: string;
  matricula: string;
  especialidad: string;
  bio?: string;
  servicioIds: number[];   // ✅ IDs de servicios existentes
}

export interface CambiarEstadoPayload {
  activo: boolean;  // ✅ boolean estricto
}
```

```typescript
// types/cita.ts
export type EstadoCita =
  | 'PENDIENTE'
  | 'CONFIRMADA'
  | 'EN_PROGRESO'
  | 'COMPLETADA'
  | 'CANCELADA'
  | 'CANCELADA_POR_MEDICO'
  | 'NO_ASISTIO';

export interface Cita {
  id: number;
  mascotaId: number;
  mascotaNombre: string;
  veterinarioId: number;
  veterinarioNombre: string;
  veterinarioAvatar?: string;
  clienteId: number;
  clienteNombre: string;
  servicio: Servicio;  // ✅ objeto en vez de enum
  fechaHora: string;
  duracionMinutos: number;
  estado: EstadoCita;
  notas?: string;
  motivoCancelacion?: string;
  fechaCreacion: string;
  pagado: boolean;
}

export interface CrearCitaPayload {
  mascotaId: number;
  veterinarioId: number;
  servicioId: number;     // ✅ ID en vez de enum
  fechaHora: string;      // ISO 8601
  duracionMinutos?: number;
  notas?: string;
}

export interface CambiarEstadoCitaPayload {
  estado: EstadoCita;
  motivo?: string;  // recomendado para CANCELADA
}
```

---

## Pantallas Afectadas

### Tabla de Veterinarios

```typescript
// ❌ ANTES
<td>{veterinario.serviciosHabilitados.join(', ')}</td>

// ✅ AHORA
<td>{veterinario.servicios.map(s => s.nombre).join(', ')}</td>
```

### Modal Crear/Editar Veterinario

```typescript
const [serviciosDisponibles, setServiciosDisponibles] = useState<Servicio[]>([]);
const [servicioIdsSeleccionados, setServicioIdsSeleccionados] = useState<number[]>([]);

useEffect(() => {
  fetch('/api/v1/servicios', { headers: { Authorization: `Bearer ${token}` } })
    .then(res => res.json())
    .then(data => setServiciosDisponibles(data));
}, []);

return (
  <div>
    {serviciosDisponibles.map(servicio => (
      <label key={servicio.id}>
        <input
          type="checkbox"
          checked={servicioIdsSeleccionados.includes(servicio.id)}
          onChange={(e) => {
            if (e.target.checked) {
              setServicioIdsSeleccionados([...servicioIdsSeleccionados, servicio.id]);
            } else {
              setServicioIdsSeleccionados(
                servicioIdsSeleccionados.filter(id => id !== servicio.id)
              );
            }
          }}
        />
        {servicio.nombre} - ${servicio.precio}
      </label>
    ))}
  </div>
);

const payload: CrearVeterinarioPayload = {
  // ...
  servicioIds: servicioIdsSeleccionados
};
```

### Modal Agendar Cita

```typescript
// ❌ ANTES
<select value={tipoCita} onChange={...}>
  <option value="CONSULTA">Consulta</option>
  // ...
</select>

// ✅ AHORA
<select value={servicioId} onChange={...}>
  {serviciosDisponibles.map(servicio => (
    <option key={servicio.id} value={servicio.id}>
      {servicio.nombre} - ${servicio.precio}
    </option>
  ))}
</select>
```

### Detalle de Cita

```typescript
// ❌ ANTES
<div>Tipo: {cita.tipoCita}</div>

// ✅ AHORA
<div>
  Servicio: {cita.servicio.nombre}
  <small>Precio: ${cita.servicio.precio}</small>
</div>
```

### Filtros de Veterinarios por Servicio

```typescript
// Filtrar en frontend
const filtrados = veterinarios.filter(v =>
  v.servicios.some(s => s.id === servicioIdFiltro)
);
```

---

## Manejo de Errores

### "El veterinario no ofrece este servicio"

```typescript
try {
  await citaService.crear(payload);
} catch (error) {
  if (error.response?.status === 400 &&
      error.response?.data?.message?.includes('no ofrece este servicio')) {
    toast.error('Este veterinario no ofrece el servicio seleccionado. Elegí otro.');
  }
}
```

### Servicio inexistente

```typescript
if (error.response?.status === 404) {
  toast.error('Uno o más servicios seleccionados no existen. Recargá la lista.');
}
```

### Email o matrícula duplicada

```typescript
if (error.response?.status === 400 &&
    error.response?.data?.message?.toLowerCase().includes('duplicate')) {
  toast.error('Email o matrícula ya registrados.');
}
```

---

## Checklist de Migración

### Backend (ya hecho en `main`)
- [x] `Veterinario.servicios` ahora es `Set<Servicio>` (V28)
- [x] `Cita.servicio` ahora es `ManyToOne Servicio` (V29)
- [x] Drop de la join-table vieja `veterinario_servicios` (V27)
- [x] `motivo_cancelacion` en citas (V30)
- [x] Validación `veterinario.tieneServicio()` en `CitaService.crear()`
- [x] DTOs de controller usan `servicioIds` y `servicioId`

### Backend (código muerto pendiente de limpieza)
- [ ] Eliminar `entity/enums/TipoCita.java` (no se usa en ninguna entidad)
- [ ] Eliminar `dto/request/VeterinarioPerfilRequest.java` (no se usa en ningún controller)
- [ ] Eliminar `dto/request/VeterinarioCuentaRequest.java` (no se usa en ningún controller)
- [ ] Evaluar eliminar `event/*.java` (3 clases sin listeners ni publishers)

### Frontend - Tareas a realizar

- [ ] `types/veterinario.ts`: `serviciosHabilitados` → `servicios`, `servicioIds`
- [ ] `types/cita.ts`: `tipoCita` → `servicio`/`servicioId`; agregar `motivoCancelacion`
- [ ] `services/veterinarioService.ts`: ajustar tipos
- [ ] `services/citaService.ts`: ajustar tipos
- [ ] Cargar lista de servicios al iniciar la app
- [ ] `components/TablaVeterinarios.tsx`: renderizar `servicios[]`
- [ ] `components/ModalVeterinario.tsx`: checkboxes de servicios
- [ ] `components/ModalCita.tsx`: selector de servicios
- [ ] `pages/DetalleCita.tsx`: mostrar `servicio.nombre` y `servicio.precio`
- [ ] `pages/DetalleVeterinario.tsx`: mostrar `servicios[]`
- [ ] Manejar `motivo` al cancelar cita
- [ ] Validar en frontend que el servicio seleccionado esté en la lista del veterinario antes de enviar la cita

### Testing
- [ ] Crear veterinario con `servicioIds` válidos
- [ ] Intentar agendar cita con servicio no disponible
- [ ] Verificar que la respuesta muestra objeto `servicio` en vez de string
- [ ] Probar `PATCH` de estado con booleano estricto
- [ ] Probar cancelación de cita con `motivo`

---

## Casos de Prueba

### Test 1: Crear Veterinario Exitoso
```typescript
const servicios = await fetch('/api/v1/servicios', {
  headers: { Authorization: `Bearer ${token}` }
}).then(r => r.json());

const nuevoVet = await fetch('/api/v1/veterinarios', {
  method: 'POST',
  headers: {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    nombre: 'Juan',
    apellido: 'Perez',
    email: 'juan@test.com',
    password: 'pass1234',
    matricula: 'VET-001',
    especialidad: 'Cirugía',
    servicioIds: [1, 3]
  })
});

expect(nuevoVet.status).toBe(201);
expect(nuevoVet.data.servicios).toHaveLength(2);
expect(nuevoVet.data.servicios[0]).toHaveProperty('nombre');
```

### Test 2: Crear Veterinario con Servicio Inexistente
```typescript
const response = await fetch('/api/v1/veterinarios', {
  method: 'POST',
  body: JSON.stringify({
    // ...
    servicioIds: [999]  // ID inexistente
  })
});
expect(response.status).toBe(404);
```

### Test 3: Agendar Cita con Servicio No Ofrecido
```typescript
// Veterinario solo tiene servicios [1, 2]
const cita = await fetch('/api/v1/citas', {
  method: 'POST',
  body: JSON.stringify({
    mascotaId: 1,
    veterinarioId: 5,
    servicioId: 99,  // servicio 99 no está en [1, 2]
    fechaHora: '2026-07-15T10:00:00',
    duracionMinutos: 30
  })
});
expect(cita.status).toBe(400);
expect(cita.data.message).toBe('El veterinario no ofrece este servicio');
```

### Test 4: PATCH Activo con Tipo Incorrecto
```typescript
const response = await fetch('/api/v1/veterinarios/1/activo', {
  method: 'PATCH',
  body: JSON.stringify({ activo: 'true' })  // string en vez de boolean
});
expect(response.status).toBe(400);
```

### Test 5: Cancelar Cita con Motivo
```typescript
const response = await fetch('/api/v1/citas/1/estado', {
  method: 'PATCH',
  body: JSON.stringify({
    estado: 'CANCELADA',
    motivo: 'Cliente no puede asistir'
  })
});
expect(response.status).toBe(200);
expect(response.data.motivoCancelacion).toBe('Cliente no puede asistir');
```

---

## FAQ

### ¿Qué pasa con las citas existentes que tenían `tipoCita`?
La migración V29 convierte la columna `tipo_cita` (VARCHAR) en `servicio_id` (FK). Si tenés datos de prueba, asegurate de hacer backup antes de levantar la app después del merge.

### ¿Cómo obtengo el ID de un servicio?
```typescript
const servicios = await fetch('/api/v1/servicios', {
  headers: { Authorization: `Bearer ${token}` }
}).then(r => r.json());
// servicios[0].id = 1, servicios[0].nombre = 'Consulta general', etc.
```

### ¿Debo cachear la lista de servicios en el frontend?
Sí, es recomendable. Los servicios no cambian frecuentemente. Cargalos al inicio de la app y mantenelos en estado global (Context, Redux, etc.).

### ¿Qué pasa si un servicio se elimina pero un veterinario lo tenía?
El backend tiene `ON DELETE CASCADE` en la tabla `servicio_veterinario`. Si eliminás un servicio, se elimina automáticamente de todos los veterinarios que lo ofrecían. **Las citas existentes con ese servicio NO se eliminan** (FK constraint).

### ¿Cómo muestro los servicios en la tabla de veterinarios?
```typescript
{veterinario.servicios.map(s => s.nombre).join(', ')}
// "Consulta general, Vacunación, Cirugía"
```

### ¿Cómo manejo el error "no ofrece este servicio" en el formulario de cita?
Validá en el frontend antes de enviar:
```typescript
const ids = veterinario.servicios.map(s => s.id);
if (!ids.includes(servicioIdSeleccionado)) {
  toast.error('Este veterinario no ofrece el servicio seleccionado');
  return;
}
```

### ¿Por qué sigo viendo el enum `TipoCita` en el código?
Es código muerto que quedó después del refactor. No se usa en ninguna entidad ni en ningún controller. Se recomienda eliminarlo en una limpieza posterior.

### ¿Qué pasa con los DTOs `VeterinarioPerfilRequest` y `VeterinarioCuentaRequest`?
También son código muerto. El controller `VeterinarioController` usa exclusivamente `VeterinarioRequest` (que ya incluye `servicioIds`).

---

## Resumen Rápido de Cambios

| Endpoint | Campo viejo | Campo nuevo | Tipo |
|---|---|---|---|
| `GET /api/v1/veterinarios` | `serviciosHabilitados: string[]` | `servicios: Servicio[]` | Response |
| `POST /api/v1/veterinarios` | `serviciosHabilitados: string[]` | `servicioIds: number[]` | Request |
| `PUT /api/v1/veterinarios/{id}` | `serviciosHabilitados: string[]` | `servicioIds: number[]` | Request |
| `GET /api/v1/citas` | `tipoCita: string` | `servicio: Servicio` | Response |
| `POST /api/v1/citas` | `tipoCita: string` | `servicioId: number` | Request |
| `PATCH /api/v1/veterinarios/{id}/activo` | (sin cambio) | `activo: boolean` (estricto) | Request |
| `PATCH /api/v1/citas/{id}/estado` | (nuevo) | `estado: EstadoCita`, `motivo?: string` | Request |

---

**Versión del documento:** 2.0
**Última actualización:** 2026-06-24
**Rama backend:** `main`
