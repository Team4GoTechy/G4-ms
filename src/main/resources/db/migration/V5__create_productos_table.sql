CREATE TABLE productos (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    descripcion TEXT,
    precio DECIMAL(10,2) NOT NULL,
    stock INTEGER NOT NULL DEFAULT 0 CHECK (stock >= 0),
    categoria_id BIGINT,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (categoria_id) REFERENCES categorias(id)
);

CREATE INDEX idx_productos_codigo ON productos(codigo);
CREATE INDEX idx_productos_activo ON productos(activo);
