CREATE TABLE ordenes_compra (
    id BIGSERIAL PRIMARY KEY,
    proveedor_id BIGINT NOT NULL REFERENCES proveedores(id),
    estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE detalles_orden_compra (
    id BIGSERIAL PRIMARY KEY,
    insumo_id BIGINT NOT NULL REFERENCES insumos(id),
    cantidad INTEGER NOT NULL,
    precio_unitario DECIMAL(10, 2),
    orden_compra_id BIGINT NOT NULL REFERENCES ordenes_compra(id)
);
