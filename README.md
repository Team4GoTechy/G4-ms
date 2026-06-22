# G4-ms - PetStore E-Commerce Backend

Backend del proyecto Pet Store para la capacitación impartida por GoTechy.

## 📋 Descripción del Proyecto

Este proyecto implementa un sistema completo de **e-commerce** y **gestión veterinaria** para una tienda de mascotas, con tres módulos principales:

| Módulo | Descripción |
|--------|-------------|
| **E-Commerce** | Gestión de productos, compras y usuarios (CLIENTE, ADMIN) |
| **Gestión Veterinaria** | Gestión de historial clínico, consultas, citas e internaciones (VETERINARIO, ADMIN) |
| **Vet-Stock** | Gestión de insumos médicos, stock, solicitudes y órdenes de compra (VETERINARIO, ADMIN) |

---

## 🏥 Módulo de Gestión Veterinaria (Sv-Veterinaria)

### Descripción

Este módulo implementa la gestión de historial clínico de mascotas, consultas médicas, citas e internaciones dentro del sistema, con control de acceso por roles.

### Funcionalidades Principales

| Funcionalidad | Descripción |
|---------------|-------------|
| **Mascotas** | Gestión de mascotas con datos básicos y vínculo con su dueño |
| **Historial Clínico** | Listado paginado de consultas asociadas a cada mascota |
| **Consultas** | Registro de consultas médicas con diagnóstico, tratamiento y fecha |
| **Citas** | Gestión de citas con validación de horarios no superpuestos |
| **Internaciones** | Control de internaciones con evoluciones registradas |
| **Prescripciones** | Gestión de prescripciones médicas vinculadas a consultas |

### Modelo de Datos (Veterinaria)

```mermaid
erDiagram
    MASCOTA {
        Long id PK
        string nombre
        string especie
        string sexo
        Long fk_dueno FK
    }
    CONSULTA {
        Long id PK
        Long fk_mascota FK
        Long fk_veterinario FK
        date fecha
        string diagnostico
        string tratamiento
    }
    USUARIO ||--o{ MASCOTA : tiene
    MASCOTA ||--o{ CONSULTA : tiene
    CONSULTA }o--|| USUARIO : realizada_por
```

### Endpoints Principales (Sv-Veterinaria)

| Método | Ruta | Descripción | Auth |
|--------|------|-------------|------|
| GET | `/api/v1/mascotas/{id}/historial-clinico` | Listar historial clínico | ADMIN, VETERINARIO |
| POST | `/api/v1/consultas` | Registrar nueva consulta | ADMIN, VETERINARIO |
| PUT | `/api/v1/consultas/{id}` | Actualizar consulta | ADMIN, VETERINARIO |
| DELETE | `/api/v1/consultas/{id}` | Eliminar consulta | ADMIN |
| POST | `/api/v1/citas` | Crear cita | ADMIN, VETERINARIO |
| GET | `/api/v1/citas/veterinario/{id}/mes?anio=&mes=` | Agenda del mes | ADMIN, VETERINARIO |
| POST | `/api/v1/internaciones` | Crear internación | ADMIN, VETERINARIO |
| PUT | `/api/v1/internaciones/{id}/evolucion` | Agregar evolución | ADMIN, VETERINARIO |

---

## 📦 Módulo E-Commerce

### Funcionalidades Principales

| Funcionalidad | Descripción |
|---------------|-------------|
| **Productos** | Gestión de productos con código único, precios, stock y categorías |
| **Categorías** | Organización de productos por categorías |
| **Carrito de Compras** | Agregar, modificar y eliminar items del carrito |
| **Compras** | Proceso completo de compra con validación de stock |
| **Usuarios** | Sistema de usuarios con roles (CLIENTE, ADMIN) |
| **Autenticación** | JWT-based authentication |
| **Estado de Compras** | ADMIN puede cambiar estado (PENDIENTE → CONFIRMADO → ENTREGADO, o CANCELADO) |
| **Stock Automático** | Al cancelar compra, el stock se restaura automáticamente |

---

## 🏥 Módulo de Gestión Veterinaria (Vet-Stock)

### Descripción

Este módulo permite a los **veterinarios** gestionar los insumos médicos de la clínica, solicitando reposición cuando el stock está bajo. Los **administradores** aprobam las solicitudes y gestionan las órdenes de compra a proveedores.

### Roles y Permisos

| Rol | Permisos |
|-----|----------|
| **VETERINARIO** | Ver stock, crear solicitudes de reposición, registrar consumo de insumos |
| **ADMIN** | Todas las anteriores + crear/editar insumos, aprobar/cancelar solicitudes, gestionar proveedores, completar/cancelar órdenes |

### Flujo de Trabajo

```
┌─────────────┐     Solicita      ┌─────────────┐     Aprueba      ┌─────────────┐
│  VETERINARIO │ ───────────────► │  SOLICITUD   │ ───────────────► │   ORDEN     │
│              │                  │  (PENDIENTE)  │                  │  COMPRA     │
└─────────────┘                  └─────────────┘                  │ (PENDIENTE)  │
                                                                    └──────┬──────┘
                                                                           │
                                                                   ┌───────▼───────┐
                                                                   │  ADMIN recibe  │
                                                                   │  mercadería   │
                                                                   └───────┬───────┘
                                                                           │
                                                            ┌──────────────▼──────────────┐
                                                            │ COMPLETAR orden             │
                                                            │ - Actualiza precios reales  │
                                                            │ - Aumenta stock             │
                                                            │ - Registra movimientos     │
                                                            └────────────────────────────┘

┌─────────────┐     Registra      ┌─────────────┐
│  VETERINARIO │ ───────────────► │   STOCK      │
│              │     consumo       │  (disminuye) │
└─────────────┘                  └─────────────┘
```

### Entidades Principales

| Entidad | Descripción |
|---------|-------------|
| **Insumo** | Catálogo de productos médicos (nombre, descripción, unidad, precio, stock mínimo) |
| **StockInsumo** | Stock actual de cada insumo |
| **MovimientoInsumo** | Historial de entradas y salidas (con cantidad, precio, fecha, descripción) |
| **SolicitudReposicion** | Solicitud del veterinario para reponer insumos |
| **DetalleSolicitud** | Ítems solicitados (insumo + cantidad) |
| **OrdenCompra** | Orden de compra al proveedor |
| **DetalleOrdenCompra** | Ítems de la orden (con precio real acordado) |
| **Proveedor** | Datos del proveedor (nombre, email, teléfono) |

### Estados

| Entidad | Estados |
|---------|---------|
| **SolicitudReposicion** | `PENDIENTE` → `APROBADA` o `CANCELADA` |
| **OrdenCompra** | `PENDIENTE` → `COMPLETADA` o `CANCELADA` |
| **MovimientoInsumo** | `ENTRADA` (compra) o `SALIDA` (consumo) |

### Lógica de Negocio

1. **Crear Insumo (ADMIN):** Al crear un insumo, automáticamente se crea un registro de stock en 0.

2. **Solicitar Reposición (VET):** El veterinario crea una solicitud con los insumos y cantidades necesitadas (sin especificar precio).

3. **Aprobar Solicitud (ADMIN):** El admin selecciona un proveedor y se genera automáticamente una `OrdenCompra` en estado `PENDIENTE`.

4. **Completar Orden (ADMIN):** Cuando llega la mercadería:
   - El admin puede ajustar los precios reales (que pueden diferir de los estimados)
   - El precio del insumo se actualiza con el precio real
   - El stock aumenta según las cantidades recibidas
   - Se registran los movimientos de entrada

5. **Registrar Consumo (VET):** El veterinario registra cuando usa insumos en una consulta:
   - El stock disminuye
   - Se registra el movimiento de salida

### Alertas de Stock

El endpoint de stock incluye un campo `alertaStock` que indica `true` cuando `cantidadActual <= stockMinimo`, permitiendo mostrar alertas en el frontend.

---

## 🚀 Inicio Rápido

### Requisitos

- Java 21+
- Maven 3.8+
- PostgreSQL 15+
- Docker (opcional)

### Configuración

1. **Variables de entorno:**
```bash
export JWT_SECRET=PETSHOP_SECRET_KEY_256_BITS_MIN_FOR_HS256_ALGORITHM_2026
export CLOUDINARY_CLOUD_NAME=your_cloud_name
export CLOUDINARY_API_KEY=your_api_key
export CLOUDINARY_API_SECRET=your_api_secret
```

2. **Base de datos PostgreSQL:**
```bash
# Usando Docker
docker run -d \
  --name petshop_db \
  -e POSTGRES_DB=petshop_ecommerce \
  -e POSTGRES_USER=petshop_admin \
  -e POSTGRES_PASSWORD=petshop_secure_pass \
  -p 5432:5432 \
  postgres:15
```

3. **Ejecutar la aplicación:**
```bash
./mvnw spring-boot:run
```

---

## 👤 Usuarios de Prueba (Seeds)

Al iniciar la aplicación, se crean automáticamente los siguientes usuarios:

| Email | Password | Rol |
|-------|----------|-----|
| `admin@petshop.com` | `12345678` | ADMIN |
| `doctor@petshop.com` | `12345678` | VETERINARIO |
| `cliente@petshop.com` | `12345678` | CLIENTE |

> **Nota:** El usuario CLIENTE tiene 2 mascotas de prueba (Max - Perro, Nube - Gato)

---

## 📚 Endpoints API

### Autenticación

| Método | Ruta | Descripción | Auth |
|--------|------|-------------|------|
| POST | `/auth/register` | Registro de usuario | No |
| POST | `/auth/login` | Inicio de sesión | No |

### Productos (E-Commerce)

| Método | Ruta | Descripción | Auth |
|--------|------|-------------|------|
| GET | `/productos` | Listar productos | No |
| GET | `/productos/{id}` | Obtener producto | No |
| POST | `/productos` | Crear producto | ADMIN |
| PUT | `/productos/{id}` | Actualizar producto | ADMIN |
| DELETE | `/productos/{id}` | Eliminar producto | ADMIN |

### Categorías

| Método | Ruta | Descripción | Auth |
|--------|------|-------------|------|
| GET | `/categorias` | Listar categorías | No |

### Compras

| Método | Ruta | Descripción | Auth |
|--------|------|-------------|------|
| GET | `/compras` | Historial de compras del usuario autenticado | CLIENTE |
| GET | `/compras/usuario/{usuarioId}` | Historial de compras de un usuario específico | ADMIN |
| POST | `/compras` | Crear compra | CLIENTE |
| PUT | `/compras/{id}/estado?estado=` | Cambiar estado | ADMIN |

### Gestión de Insumos (Vet-Stock)

#### Insumos (ADMIN)

| Método | Ruta | Descripción | Auth |
|--------|------|-------------|------|
| GET | `/insumos` | Listar insumos | ADMIN |
| GET | `/insumos/{id}` | Obtener insumo | ADMIN |
| POST | `/insumos` | Crear insumo | ADMIN |
| PUT | `/insumos/{id}` | Actualizar insumo | ADMIN |
| DELETE | `/insumos/{id}` | Eliminar insumo | ADMIN |

#### Stock (VET, ADMIN)

| Método | Ruta | Descripción | Auth |
|--------|------|-------------|------|
| GET | `/insumos/stock` | Ver stock de todos los insumos | VET, ADMIN |
| GET | `/insumos/stock/{id}` | Ver stock de un insumo | VET, ADMIN |
| GET | `/insumos/stock/{id}/historial` | Ver historial de movimientos | VET, ADMIN |
| GET | `/insumos/stock/disponibles` | Stock para mostrar en frontend | No |

#### Solicitudes de Reposición

| Método | Ruta | Descripción | Auth |
|--------|------|-------------|------|
| GET | `/solicitudes` | Mis solicitudes | VET |
| GET | `/solicitudes/todas` | Todas las solicitudes | ADMIN |
| GET | `/solicitudes/{id}` | Ver solicitud | VET, ADMIN |
| GET | `/solicitudes/veterinario/{id}` | Solicitudes de un veterinario | ADMIN |
| POST | `/solicitudes` | Crear solicitud | VET |
| PUT | `/solicitudes/{id}/aprobar?proveedorId=` | Aprobar y generar orden | ADMIN |
| PUT | `/solicitudes/{id}/cancelar` | Cancelar solicitud | ADMIN |

#### Órdenes de Compra (ADMIN)

| Método | Ruta | Descripción | Auth |
|--------|------|-------------|------|
| GET | `/ordenes-compra` | Listar órdenes | ADMIN |
| GET | `/ordenes-compra/{id}` | Ver orden | ADMIN |
| **POST** | **`/ordenes-compra`** | **Crear orden de compra** | **ADMIN** |
| PUT | `/ordenes-compra/{id}/completar` | Completar orden (recibe mercadería) | ADMIN |
| PUT | `/ordenes-compra/{id}/cancelar` | Cancelar orden | ADMIN |

#### Proveedores (ADMIN)

| Método | Ruta | Descripción | Auth |
|--------|------|-------------|------|
| GET | `/proveedores` | Listar proveedores | ADMIN |
| GET | `/proveedores/{id}` | Ver proveedor | ADMIN |
| POST | `/proveedores` | Crear proveedor | ADMIN |
| PUT | `/proveedores/{id}` | Actualizar proveedor | ADMIN |
| DELETE | `/proveedores/{id}` | Eliminar proveedor | ADMIN |

#### Consumo de Insumos (VET)

| Método | Ruta | Descripción | Auth |
|--------|------|-------------|------|
| POST | `/insumos/consumo` | Registrar consumo de insumos | VET |

### Ejemplo: Crear Solicitud de Reposición

```json
POST /solicitudes
{
  "detalles": [
    { "insumoId": 1, "cantidadSolicitada": 50 },
    { "insumoId": 3, "cantidadSolicitada": 20 }
  ]
}
```

### Ejemplo: Aprobar Solicitud

```
PUT /solicitudes/1/aprobar?proveedorId=1
```

### Ejemplo: Crear Orden de Compra directamente

```json
POST /ordenes-compra
{
  "proveedorId": 1,
  "items": [
    { "insumoId": 1, "cantidad": 50 },
    { "insumoId": 3, "cantidad": 30 }
  ]
}
```

> **Nota:** La orden se crea en estado `PENDIENTE` con precios en cero. Al completarla se actualizan precios reales y stock.

### Ejemplo: Completar Orden (con precios reales)

```json
PUT /ordenes-compra/1/completar
{
  "items": [
    { "insumoId": 1, "cantidad": 50, "precioUnitario": 22.00 },
    { "insumoId": 3, "cantidad": 20, "precioUnitario": 28.50 }
  ]
}
```

### Ejemplo: Registrar Consumo

```json
POST /insumos/consumo
{
  "items": [
    { "insumoId": 1, "cantidad": 2 }
  ],
  "descripcion": "Consulta de revisión general - Luna"
}
```

---

## 📊 Modelo de Datos (E-Commerce)

```mermaid
erDiagram
    USUARIO {
        Long id PK
        string nombre
        string email
        string contrasena
        string direccion
        string telefono
        string avatar
    }
    ROL {
        Long id PK
        string nombre
    }
    PRODUCTO {
        Long id PK
        Long fk_categoria FK
        string nombre
        string codigo
        string descripcion
        decimal precio
        int stock
        string unidad_medida
        boolean activo
    }
    CATEGORIA {
        Long id PK
        string nombre
    }
    COMPRA {
        Long id PK
        Long fk_usuario FK
        datetime fecha
        decimal total
        string estado
    }
    DETALLE_COMPRA {
        Long id PK
        Long fk_compra FK
        Long fk_producto FK
        int cantidad
        decimal precioUnitario
    }

    USUARIO ||--o{ COMPRA : realiza
    USUARIO }o--|| ROL : tiene
    PRODUCTO }o--|| CATEGORIA : clasifica
    COMPRA ||--o{ DETALLE_COMPRA : contiene
    DETALLE_COMPRA }o--|| PRODUCTO : referencia
```

## 📊 Modelo de Datos (Gestión Veterinaria)

```mermaid
erDiagram
    USUARIO {
        Long id PK
        string nombre
        string email
    }
    ROL {
        Long id PK
        string nombre
    }
    PROVEEDOR {
        Long id PK
        string nombre
        string email
        string telefono
    }
    INSUMO {
        Long id PK
        string nombre
        string descripcion
        string unidad_medida
        decimal precio_unitario
        int stock_minimo
    }
    STOCK_INSUMO {
        Long id PK
        Long fk_insumo FK
        int cantidad_actual
    }
    SOLICITUD_REPOSICION {
        Long id PK
        Long fk_veterinario FK
        string estado
        datetime fecha
    }
    DETALLE_SOLICITUD {
        Long id PK
        Long fk_insumo FK
        Long fk_solicitud FK
        int cantidad_solicitada
    }
    ORDEN_COMPRA {
        Long id PK
        Long fk_proveedor FK
        string estado
        datetime fecha
    }
    DETALLE_ORDEN_COMPRA {
        Long id PK
        Long fk_insumo FK
        Long fk_orden FK
        int cantidad
        decimal precio_unitario
    }
    MOVIMIENTO_INSUMO {
        Long id PK
        Long fk_insumo FK
        string tipo
        int cantidad
        decimal precio_unitario
        datetime fecha
        string descripcion
        Long referencia_id
    }

    USUARIO }o--|| ROL : tiene
    USUARIO ||--o{ SOLICITUD_REPOSICION : solicita
    PROVEEDOR ||--o{ ORDEN_COMPRA : surte
    INSUMO ||--|| STOCK_INSUMO : tiene
    INSUMO ||--o{ DETALLE_SOLICITUD : referenced_by
    SOLICITUD_REPOSICION ||--o{ DETALLE_SOLICITUD : contiene
    ORDEN_COMPRA ||--o{ DETALLE_ORDEN_COMPRA : contiene
    DETALLE_ORDEN_COMPRA }o--|| INSUMO : referencia
    INSUMO ||--o{ MOVIMIENTO_INSUMO : tiene
```

---

## 📁 Estructura del Proyecto

```
src/main/java/com/team4/petstore/
├── config/              # Configuraciones (Security, OpenAPI, Cloudinary, DataSeeder)
├── controller/          # Controladores REST
│   ├── AuthController.java
│   ├── CategoriaController.java
│   ├── CompraController.java
│   ├── ImageController.java
│   ├── ProductoController.java
│   ├── UsuarioController.java
│   ├── MascotaController.java
│   ├── MascotaHistorialController.java
│   ├── ConsultaController.java
│   ├── CitaController.java
│   ├── InternacionController.java
│   ├── InsumoController.java
│   ├── StockInsumoController.java
│   ├── SolicitudReposicionController.java
│   ├── OrdenCompraController.java
│   └── ProveedorController.java
├── dto/
│   ├── request/        # DTOs de entrada
│   └── response/      # DTOs de salida
├── entity/             # Entidades JPA
│   ├── Usuario.java, Rol.java, Producto.java
│   ├── Categoria.java, Compra.java, DetalleCompra.java
│   ├── Mascota.java, Consulta.java, Cita.java
│   ├── Insumo.java, StockInsumo.java, MovimientoInsumo.java
│   ├── SolicitudReposicion.java, DetalleSolicitud.java
│   ├── OrdenCompra.java, DetalleOrdenCompra.java
│   ├── Proveedor.java, Internacion.java, Prescripcion.java
│   └── enums: EstadoCompra, TipoMovimiento, EstadoSolicitud, EstadoCita, etc.
├── exception/         # Excepciones personalizadas
├── repository/         # Repositorios JPA
├── security/           # Filtros JWT y configuración de seguridad
├── service/           # Lógica de negocio
└── event/             # Eventos de dominio (citas, evoluciones)
```

---

## 🛠️ Tecnologías

| Tecnología | Versión |
|------------|---------|
| Spring Boot | 3.4.13 |
| Java | 21 (LTS) |
| Spring Security | JWT |
| Spring Data JPA | - |
| PostgreSQL | 15 |
| Flyway | - |
| Swagger/OpenAPI | 2.8.4 |
| Cloudinary | cloudinary-http45 (HTTP API) |

---

## 📖 Documentación API (Swagger)

Una vez iniciada la aplicación:
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8080/api-docs
