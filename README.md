# G4-ms
Backend del proyecto Pet Store para la capacitación impartida por GoTechy

# 📑 Documentación General – Tienda de mascotas
## 1. Introducción
Este documento describe la arquitectura general del sistema de Tienda de mascotas, incluyendo las entidades principales, complementarias y sus relaciones.  
El objetivo es proveer una visión integral que sirva como referencia para el desarrollo, mantenimiento y evolución del sistema.

## 2. Alcance
El sistema abarca:  
**Facturación**: emisión de facturas, detalle de ítems y métodos de pago.  
**Usuarios y Roles**: gestión de perfiles, permisos y autenticación.  
**Productos y Categorías**: catálogo de productos y servicios.  
**Mascotas y Especialistas**: registro de pacientes y profesionales.  
**Turnos**: agenda de citas entre mascotas y especialistas.  

## 3. Entidades Principales
- FACTURAS
- USUARIO
- ROLES
- PRODUCTOS
- CATEGORIA
- MASCOTAS
- ESPECIALISTAS
- ESPECIALIDADES
- TURNOS

## 4. Entidades Complementarias
- DETALLE_FACTURAS
- METODO_DE_PAGO
- ROL_PERMISOS
- PERMISOS

## 5. Relaciones Clave
Una **Factura** contiene múltiples **DetalleFactura**.  
Una **Factura** pertenece a un Usuario y usa un Método de Pago.  
Un **Usuario** tiene un **Rol**, y los roles se vinculan a **Permisos** mediante **RolPermisos**.  
Un **Producto** se clasifica en una **Categoría**.  
Una **Mascota** pertenece a un **Usuario**.  
Un **Especialista** está vinculado a un **Usuario** y a una **Especialidad**.  
Un **Turno** atiende a una **Mascota** y es asignado a un **Especialista**.

## 6. Diagrama de Clases UML

```mermaid
erDiagram
    DETALLE_FACTURAS {
        int id_productos FK
        int id_factura FK
        decimal precio_unitario
        int cantidad
        datetime fecha_de_creacion
    }
    METODO_DE_PAGO {
        int id PK
        string nombre_metodo
        datetime fecha_de_creacion
    }
    FACTURAS {
        int id_factura PK
        int fk_usuario FK
        int fk_metodo_pago FK
        datetime fecha_de_creacion
    }
    USUARIO {
        int id PK
        int fk_rol FK
        string nombre
        string apellido
        string email
        string contrasena
        string direccion
        string telefono
        datetime fecha_de_creacion
    }
    ROLES {
        int id PK
        string nombre
        datetime fecha_de_creacion
    }
    ROL_PERMISOS {
        int fk_rol FK
        int fk_permiso PK
    }
    PERMISOS {
        int id PK
        string nombre
        string descripcion
        datetime fecha_de_creacion
    }
    PRODUCTOS {
        int id_producto PK
        int fk_categoria FK
        string nombre
        string codigo
        string descripcion
        decimal precio
        int stock
        datetime fecha_de_creacion
    }
    CATEGORIA {
        int id PK
        string nombre
        string descripcion
        datetime fecha_de_creacion
    }
    MASCOTAS {
        int id PK
        int fk_usuario FK
        string nombre
        string especie
        string raza
        datetime fecha_de_creacion
    }
    ESPECIALIDADES {
        int id PK
        string nombre
        datetime fecha_de_creacion
    }
    ESPECIALISTAS {
        int id PK
        int fk_usuario FK
        int fk_especialidad FK
        string matricula
        datetime fecha_de_creacion
    }
    TURNOS {
        int id PK
        int fk_mascota FK
        int fk_especialista FK
        datetime fecha_hora
        string motivo
        string estado
        datetime fecha_de_creacion
    }

    FACTURAS ||--o{ DETALLE_FACTURAS : contiene
    FACTURAS }o--|| METODO_DE_PAGO : usa
    FACTURAS }o--|| USUARIO : pertenece
    USUARIO }o--|| ROLES : tiene
    ROLES ||--o{ ROL_PERMISOS : asigna
    PERMISOS ||--o{ ROL_PERMISOS : define
    PRODUCTOS }o--|| CATEGORIA : clasifica
    DETALLE_FACTURAS }o--|| PRODUCTOS : referencia
    MASCOTAS }o--|| USUARIO : dueño
    ESPECIALISTAS }o--|| USUARIO : perfil
    ESPECIALISTAS }o--|| ESPECIALIDADES : especialidad
    TURNOS }o--|| MASCOTAS : atiende
    TURNOS }o--|| ESPECIALISTAS : asignado
```

## 7. Procesos Generales
- Facturación
    - Selección de productos.  
    - Generación de detalle de factura.  
    - Asociación de método de pago.  
    - Emisión de factura.  
- Gestión de Turnos  
    - Solicitud de cita por parte del usuario.  
    - Selección de especialista según especialidad.  
    - Registro del turno con fecha/hora y estado.  
    - Notificación al usuario.  

## 8. Anexos
### Glosario

**Factura**: comprobante de transacción.  
**Turno**: cita veterinaria.  
**Rol**: perfil de acceso.  
**Permiso**: acción habilitada en el sistema.
