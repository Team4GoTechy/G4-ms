CREATE TABLE compras (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10,2) NOT NULL,
    metodo_pago VARCHAR(50) NOT NULL,
    estado VARCHAR(50) NOT NULL DEFAULT 'PENDIENTE',
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

CREATE INDEX idx_compras_usuario ON compras(usuario_id);
CREATE INDEX idx_compras_fecha ON compras(fecha);
