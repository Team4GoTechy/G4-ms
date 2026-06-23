CREATE TABLE veterinarios (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL UNIQUE REFERENCES usuarios(id),
    matricula VARCHAR(50) NOT NULL UNIQUE,
    especialidad VARCHAR(100),
    bio TEXT,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE citas (
    id BIGSERIAL PRIMARY KEY,
    mascota_id BIGINT NOT NULL REFERENCES mascotas(id),
    veterinario_id BIGINT NOT NULL REFERENCES veterinarios(id),
    tipo_cita VARCHAR(30) NOT NULL,
    fecha_hora TIMESTAMP NOT NULL,
    duracion_min INTEGER NOT NULL DEFAULT 30,
    estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',
    notas TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
