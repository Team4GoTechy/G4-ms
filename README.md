# G4-ms
Backend del proyecto Pet Store para la capacitación impartida por GoTechy

```plantuml
@startuml
title FACTURACIÓN Y TURNOS

class FACTURAS {
  id_factura : PK
  fk_usuario : FK -> USUARIO
  fk_metodo_pago : FK -> METODO_DE_PAGO
  fecha_de_creacion : datetime
}

class DETALLE_FACTURAS {
  id_productos : FK -> PRODUCTOS
  id_factura : FK -> FACTURAS
  precio_unitario : decimal
  cantidad : int
  fecha_de_creacion : datetime
}

class METODO_DE_PAGO {
  id : PK
  nombre_metodo : string
  fecha_de_creacion : datetime
}

class USUARIO {
  id : PK
  fk_rol : FK -> ROLES
  nombre : string
  apellido : string
  email : string
  contrasena : string
  direccion : string
  telefono : string
  fecha_de_creacion : datetime
}

class ROLES {
  id : PK
  nombre : string
  fecha_de_creacion : datetime
}

class ROL_PERMISOS {
  fk_rol : FK -> ROLES
  fk_permiso : PK FK -> PERMISOS
}

class PERMISOS {
  id : PK
  nombre : string
  descripcion : string
  fecha_de_creacion : datetime
}

class PRODUCTOS {
  id_producto : PK
  fk_categoria : FK -> CATEGORIA
  nombre : string
  codigo : string
  descripcion : string
  precio : decimal
  stock : int
  fecha_de_creacion : datetime
}

class CATEGORIA {
  id : PK
  nombre : string
  descripcion : string
  fecha_de_creacion : datetime
}

class MASCOTAS {
  id : PK
  fk_usuario : FK -> USUARIO
  nombre : string
  especie : string
  raza : string
  fecha_de_creacion : datetime
}

class ESPECIALIDADES {
  id : PK
  nombre : string
  fecha_de_creacion : datetime
}

class ESPECIALISTAS {
  id : PK
  fk_usuario : FK -> USUARIO
  fk_especialidad : FK -> ESPECIALIDADES
  matricula : string
  fecha_de_creacion : datetime
}

class TURNOS {
  id : PK
  fk_mascota : FK -> MASCOTAS
  fk_especialista : FK -> ESPECIALISTAS
  fecha_hora : datetime
  motivo : string
  estado : string
  fecha_de_creacion : datetime
}

'=== RELACIONES ===
FACTURAS "1" -- "N" DETALLE_FACTURAS : contiene
FACTURAS "1" -- "1" METODO_DE_PAGO : usa
FACTURAS "1" -- "1" USUARIO : pertenece
USUARIO "1" -- "1" ROLES : tiene
ROLES "1" -- "N" ROL_PERMISOS : asigna
PERMISOS "1" -- "N" ROL_PERMISOS : define
PRODUCTOS "1" -- "1" CATEGORIA : clasifica
DETALLE_FACTURAS "N" -- "1" PRODUCTOS : referencia
MASCOTAS "N" -- "1" USUARIO : dueño
ESPECIALISTAS "1" -- "1" USUARIO : perfil
ESPECIALISTAS "1" -- "1" ESPECIALIDADES : especialidad
TURNOS "N" -- "1" MASCOTAS : atiende
TURNOS "N" -- "1" ESPECIALISTAS : asignado
@enduml
```
