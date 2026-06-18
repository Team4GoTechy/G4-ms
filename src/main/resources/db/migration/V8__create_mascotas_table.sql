-- Agregar columnas opcionales a la tabla usuarios
ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS edad INTEGER;
ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS avatar VARCHAR(255);

-- Crear tabla de mascotas
CREATE TABLE mascotas (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    sexo VARCHAR(10) NOT NULL CHECK (sexo IN ('Macho', 'Hembra')),
    tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('Gato', 'Perro')),
    usuario_id BIGINT NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_mascotas_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE INDEX idx_mascotas_usuario_id ON mascotas(usuario_id);
