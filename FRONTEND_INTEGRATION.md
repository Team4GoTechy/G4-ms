# Guía de Integración Frontend - Módulo Veterinario

> **Propósito:** Documentar los cambios necesarios en el frontend para consumir correctamente la nueva versión del backend (rama `feature/vet-admin` con refactor de Servicios a Opción A).

---

## 📋 Tabla de Contenidos

1. [Resumen de Cambios](#resumen-de-cambios)
2. [Cambios Breaking por Endpoint](#cambios-breaking-por-endpoint)
3. [Modelos TypeScript](#modelos-typescript)
4. [Pantallas Afectadas](#pantallas-afectadas)
5. [Manejo de Errores](#manejo-de-errores)
6. [Checklist de Migración](#checklist-de-migración)
7. [Casos de Prueba](#casos-de-prueba)
8. [FAQ](#faq)

---

## Resumen de Cambios

El backend implementó un refactor arquitectónico que consolida el concepto de "Servicios":

| Antes | Ahora | Impacto |
|-------|-------|---------|
| `TipoCita` enum usado para "qué puede hacer un veterinario" | Eliminado | Frontend no debe enviar/recibir este concepto |
| `TipoCita` enum usado en `Cita.tipoCita` | Reemplazado por `Servicio` (entidad) | Cambio breaking en citas |
| `Set<TipoServicio>` en `Veterinario.serviciosHabilitados` | `Set<Servicio>` (relación Many-to-Many) | Cambio breaking en veterinarios |

### Lo que esto significa para el frontend

- Los veterinarios ya no tienen "tipos de cita habilitados" como strings enum
- Ahora tienen una lista de **objetos Servicio** (con id, nombre, descripción, precio)
- Al crear un veterinario, se envían **IDs de servicios existentes**
- Al crear una cita, se envía el **ID del servicio** (no más enum)
- El backend valida automáticamente que el veterinario ofrezca el servicio

---

## Cambios Breaking por Endpoint

### 🔴 1. GET `/api/v1/veterinarios` - Listar veterinarios

**Response - Campo `serviciosHabilitados` → `servicios`**

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

### 🔴 2. POST `/api/v1/veterinarios` - Crear veterinario

**Request - Campo `serviciosHabilitados` → `servicioIds`**

```typescript
// ❌ ANTES
{
  "nombre": "Juan",
  "apellido": "Perez",
  "email": "juan.perez@petstore.com",
  "password": "securePassword123",
  "matricula": "VET-2024-001",
  "especialidad": "Cirugía General",
  "serviciosHabilitados": ["CONSULTA", "VACUNACION", "CIRUGIA"]  // ❌ ya no funciona
}

// ✅ AHORA
{
  "nombre": "Juan",
  "apellido": "Perez",
  "email": "juan.perez@petstore.com",
  "password": "securePassword123",
  "matricula": "VET-2024-001",
  "especialidad": "Cirugía General",
  "servicioIds": [1, 2, 3]  // ✅ IDs de servicios existentes
}
```

> **⚠️ Importante:** Los IDs deben existir en la tabla `servicios`. Si envías un ID inexistente, recibirás error 404.

### 🔴 3. PUT `/api/v1/veterinarios/{id}` - Actualizar veterinario

Mismo cambio que POST: `serviciosHabilitados` → `servicioIds`

### 🔴 4. GET `/api/v1/citas` - Listar citas

**Response - Campo `tipoCita` → `servicio`**

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
  "veterinarioId": 3,
  "servicio": {  // ✅ objeto completo
    "id": 1,
    "nombre": "Consulta general",
    "descripcion": "Revisión general de la mascota",
    "precio": 5000.0
  },
  "fechaHora": "2026-07-15T10:00:00",
  "estado": "PENDIENTE"
}
```

### 🔴 5. POST `/api/v1/citas` - Crear cita

**Request - Campo `tipoCita` → `servicioId`**

```typescript
// ❌ ANTES
{
  "mascotaId": 1,
  "veterinarioId": 5,
  "tipoCita": "CONSULTA",  // ❌ ya no funciona
  "fechaHora": "2026-07-15T10:00:00",
  "duracionMinutos": 30
}

// ✅ AHORA
{
  "mascotaId": 1,
  "veterinarioId": 5,
  "servicioId": 1,  // ✅ ID del servicio
  "fechaHora": "2026-07-15T10:00:00",
  "duracionMinutos": 30
}
```

> **⚠️ Validación nueva del backend:** Si el servicio no está en la lista del veterinario, la cita será rechazada con error 400.

### 🟡 6. PATCH `/api/v1/veterinarios/{id}/activo` - Cambiar estado

**Request - DTO estricto**

```typescript
// ✅ AHORA (sin cambios funcionales, pero estricto)
{
  "activo": true  // boolean, NO string
}
```

Si envías `"activo": "true"` (string) será rechazado con error 400.

### 🟢 7. Endpoints SIN cambios

Los siguientes endpoints no requieren cambios:

- `GET /api/v1/veterinarios/{id}/horarios`
- `PUT /api/v1/veterinarios/{id}/horarios`
- `GET /api/v1/veterinarios/{id}/bloqueos`
- `POST /api/v1/veterinarios/{id}/bloqueos`
- `DELETE /api/v1/veterinarios/{id}/bloqueos/{bloqueoId}`
- `GET /api/v1/veterinarios/todos`
- `DELETE /api/v1/veterinarios/{id}`

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
}

// types/tipo-cita.ts (solo para retrocompatibilidad si se mantiene)
export enum TipoCita {
  CONSULTA = 'CONSULTA',
  VACUNACION = 'VACUNACION',
  CIRUGIA = 'CIRUGIA',
  GROOMING = 'GROOMING',
  CONTROL = 'CONTROL'
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
  servicios: Servicio[];  // ✅ CAMBIO: array de objetos
  fechaCreacion: string;
}

export interface CrearVeterinarioPayload {
  nombre: string;
  apellido: string;
  email: string;
  password: string;
  telefono?: string;
  matricula: string;
  especialidad: string;
  bio?: string;
  servicioIds: number[];  // ✅ CAMBIO: array de IDs
}

export interface CambiarEstadoPayload {
  activo: boolean;  // ✅ estricto boolean
}
```

```typescript
// types/cita.ts
export interface Cita {
  id: number;
  mascotaId: number;
  mascotaNombre: string;
  veterinarioId: number;
  veterinarioNombre: string;
  veterinarioAvatar?: string;
  clienteId: number;
  clienteNombre: string;
  servicio: Servicio;  // ✅ CAMBIO: objeto en vez de enum
  fechaHora: string;
  duracionMinutos: number;
  estado: EstadoCita;
  notas?: string;
  fechaCreacion: string;
  pagado: boolean;
}

export interface CrearCitaPayload {
  mascotaId: number;
  veterinarioId: number;
  servicioId: number;  // ✅ CAMBIO: ID en vez de enum
  fechaHora: string;
  duracionMinutos?: number;
  notas?: string;
}
```

---

## Pantallas Afectadas

### 📺 Pantalla: Tabla de Veterinarios

**Cambio:** La columna "Servicios" ahora debe mostrar nombres de servicios (no strings enum).

```typescript
// ❌ ANTES
<td>{veterinario.serviciosHabilitados.join(', ')}</td>

// ✅ AHORA
<td>
  {veterinario.servicios.map(s => s.nombre).join(', ')}
</td>
```

### 📺 Pantalla: Modal Crear/Editar Veterinario

**Cambio:** Reemplazar checkboxes de enums por checkboxes de servicios dinámicos.

```typescript
// Estado del componente
const [serviciosDisponibles, setServiciosDisponibles] = useState<Servicio[]>([]);
const [servicioIdsSeleccionados, setServicioIdsSeleccionados] = useState<number[]>([]);

// Cargar servicios disponibles al montar
useEffect(() => {
  fetch('/api/v1/servicios')
    .then(res => res.json())
    .then(data => setServiciosDisponibles(data));
}, []);

// Renderizar checkboxes
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

// Enviar al backend
const payload: CrearVeterinarioPayload = {
  // ... otros campos
  servicioIds: servicioIdsSeleccionados
};
```

### 📺 Pantalla: Modal Agendar Cita

**Cambio:** Reemplazar selector de "Tipo de Cita" por selector de "Servicio".

```typescript
// ❌ ANTES
<select value={tipoCita} onChange={...}>
  <option value="CONSULTA">Consulta</option>
  <option value="VACUNACION">Vacunación</option>
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

### 📺 Pantalla: Detalle de Cita

**Cambio:** Mostrar `servicio.nombre` y `servicio.precio` en vez de `tipoCita`.

```typescript
// ❌ ANTES
<div>Tipo: {cita.tipoCita}</div>

// ✅ AHORA
<div>
  Servicio: {cita.servicio.nombre}
  <small>Precio: ${cita.servicio.precio}</small>
</div>
```

### 📺 Pantalla: Filtros de Veterinarios por Servicio

**Cambio:** Filtrar por `servicio.id` en vez de `tipoCita`.

```typescript
// ❌ ANTES
fetch(`/api/v1/veterinarios?tipo=${tipoCitaFiltro}`)

// ✅ AHORA (cargar todos y filtrar en frontend, o agregar query param en backend)
const veterinariosFiltrados = veterinarios.filter(v =>
  v.servicios.some(s => s.id === servicioIdFiltro)
);
```

---

## Manejo de Errores

### Error: "El veterinario no ofrece este servicio"

**Cuándo ocurre:** Al crear una cita con un `servicioId` que no está en los `servicios` del veterinario.

```typescript
try {
  await citaService.crear(payload);
} catch (error) {
  if (error.response?.status === 400 &&
      error.response?.data?.message?.includes('no ofrece este servicio')) {
    toast.error('Este veterinario no ofrece el servicio seleccionado. Por favor, elegí otro servicio.');
  }
}
```

### Error: Servicio inexistente

**Cuándo ocurre:** Al crear/actualizar veterinario con `servicioIds` que no existen en BD.

```typescript
if (error.response?.status === 404) {
  toast.error('Uno o más servicios seleccionados no existen. Recargá la lista e intentá de nuevo.');
}
```

### Error: Email o matrícula duplicada

**Sin cambios**, pero recordar manejar 400 con mensajes apropiados.

---

## Checklist de Migración

### Backend
- [x] Cambiar `Set<TipoServicio>` por `Set<Servicio>` en `Veterinario`
- [x] Crear `TipoServicio` y luego eliminarlo (hecho en la rama)
- [x] Refactor `Cita` para usar `ManyToOne Servicio`
- [x] Validar `veterinario.getServicios().contains(servicio)` en `CitaService.crear()`
- [x] Crear migraciones V24, V25, V26

### Frontend - Tareas a realizar

#### Archivos a modificar
- [ ] `types/veterinario.ts` - cambiar `serviciosHabilitados` → `servicios`, `servicioIds`
- [ ] `types/cita.ts` - cambiar `tipoCita` → `servicio`/`servicioId`
- [ ] `services/veterinarioService.ts` - ajustar tipos
- [ ] `services/citaService.ts` - ajustar tipos
- [ ] `components/TablaVeterinarios.tsx` - renderizar nuevos campos
- [ ] `components/ModalVeterinario.tsx` - usar checkboxes de servicios
- [ ] `components/ModalCita.tsx` - usar selector de servicios
- [ ] `pages/DetalleCita.tsx` - mostrar `servicio.nombre`
- [ ] `pages/DetalleVeterinario.tsx` - mostrar `servicios[]`

#### Tareas generales
- [ ] Cargar lista de servicios al iniciar la app (cache o context)
- [ ] Validar que el servicio seleccionado esté en la lista del veterinario antes de enviar cita
- [ ] Actualizar mensajes de error para incluir "no ofrece este servicio"
- [ ] Reemplazar todos los `tipoCita` enum references en el código

#### Testing
- [ ] Crear veterinario con servicios seleccionados
- [ ] Intentar agendar cita con servicio no disponible
- [ ] Verificar que la respuesta muestra objeto servicio en vez de string
- [ ] Probar PATCH de estado con booleano

---

## Casos de Prueba

### Test 1: Crear Veterinario Exitoso
```typescript
// Setup
const servicios = await fetch('/api/v1/servicios').then(r => r.json());
// servicios = [{id: 1, nombre: 'Consulta', precio: 5000}, ...]

// Action
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
    servicioIds: [1, 3]  // IDs válidos
  })
});

// Expected
expect(nuevoVet.status).toBe(201);
expect(nuevoVet.data.servicios).toHaveLength(2);
expect(nuevoVet.data.servicios[0]).toHaveProperty('nombre');
```

### Test 2: Crear Veterinario con Servicio Inexistente
```typescript
// Action
const response = await fetch('/api/v1/veterinarios', {
  method: 'POST',
  body: JSON.stringify({
    // ...
    servicioIds: [999]  // ID inexistente
  })
});

// Expected
expect(response.status).toBe(404);
expect(response.data.message).toContain('servicios no existen');
```

### Test 3: Agendar Cita con Servicio No Ofrecido
```typescript
// Setup: Veterinario solo tiene servicios [1, 2]

// Action
const cita = await fetch('/api/v1/citas', {
  method: 'POST',
  body: JSON.stringify({
    mascotaId: 1,
    veterinarioId: 5,
    servicioId: 99,  // Servicio 99 no está en [1, 2]
    fechaHora: '2026-07-15T10:00:00',
    duracionMinutos: 30
  })
});

// Expected
expect(cita.status).toBe(400);
expect(cita.data.message).toBe('El veterinario no ofrece este servicio');
```

### Test 4: PATCH Estado con Tipo Incorrecto
```typescript
// Action
const response = await fetch('/api/v1/veterinarios/1/activo', {
  method: 'PATCH',
  body: JSON.stringify({ activo: 'true' })  // string en vez de boolean
});

// Expected
expect(response.status).toBe(400);
```

---

## FAQ

### ¿Qué pasa con las citas existentes que tenían `tipoCita`?
Las citas existentes en la base de datos deben migrarse. El backend incluye la migración V26 que convierte la columna `tipo_cita` (VARCHAR) en `servicio_id` (FK). Si tenés datos de prueba, asegurate de hacer backup antes de aplicar la migración.

### ¿Cómo obtengo el ID de un servicio para crear un veterinario?
Primero listá los servicios disponibles:
```typescript
const servicios = await fetch('/api/v1/servicios').then(r => r.json());
// servicios[0].id = 1, servicios[0].nombre = 'Consulta general', etc.
```

### ¿El frontend necesita cachear la lista de servicios?
Sí, es recomendable. Los servicios no cambian frecuentemente. Podés cargarlos al inicio de la app y mantenerlos en estado global (Context, Redux, etc.).

### ¿Qué pasa si un servicio se elimina pero un veterinario lo tenía?
El backend tiene `ON DELETE CASCADE` en la tabla `servicio_veterinario`. Si eliminás un servicio, se elimina automáticamente de todos los veterinarios que lo ofrecían. Las citas existentes con ese servicio NO se eliminan (FK constraint).

### ¿Cómo muestro los servicios en la tabla de veterinarios?
```typescript
{veterinario.servicios.map(s => s.nombre).join(', ')}
// Output: "Consulta general, Vacunación, Cirugía"
```

### ¿Cómo manejo el error "no ofrece este servicio" en el formulario de cita?
Antes de enviar la cita, validar en el frontend:
```typescript
const serviciosDelVeterinario = veterinario.servicios.map(s => s.id);
if (!serviciosDelVeterinario.includes(servicioIdSeleccionado)) {
  toast.error('Este veterinario no ofrece el servicio seleccionado');
  return;
}
```

### ¿Necesito migrar citas de prueba manualmente?
Sí. Si tenés citas con `tipo_cita` viejo, después de la migración V26 la columna se borra. Si tenés datos importantes, exportalos antes de mergear esta rama.

---

## Resumen Rápido de Cambios

| Endpoint | Campo viejo | Campo nuevo | Tipo |
|----------|-------------|-------------|------|
| `GET /veterinarios` | `serviciosHabilitados: string[]` | `servicios: Servicio[]` | Response |
| `POST /veterinarios` | `serviciosHabilitados: string[]` | `servicioIds: number[]` | Request |
| `PUT /veterinarios/{id}` | `serviciosHabilitados: string[]` | `servicioIds: number[]` | Request |
| `GET /citas` | `tipoCita: string` | `servicio: Servicio` | Response |
| `POST /citas` | `tipoCita: string` | `servicioId: number` | Request |
| `PATCH /veterinarios/{id}/activo` | (sin cambio) | `activo: boolean` | Request (estricto) |

---

## Contacto

Cualquier duda sobre estos cambios, consultar al equipo de backend antes de mergear.

**Versión del documento:** 1.0  
**Última actualización:** 2026-06-23  
**Rama backend:** `feature/vet-admin`
