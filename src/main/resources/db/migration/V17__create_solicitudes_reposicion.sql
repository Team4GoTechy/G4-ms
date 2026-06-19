CREATE TABLE solicitudes_reposicion (
    id BIGSERIAL PRIMARY KEY,
    veterinario_id BIGINT NOT NULL REFERENCES usuarios(id),
    estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE detalles_solicitud (
    id BIGSERIAL PRIMARY KEY,
    insumo_id BIGINT NOT NULL REFERENCES insumos(id),
    cantidad_solicitada INTEGER NOT NULL,
    solicitud_id BIGINT NOT NULL REFERENCES solicitudes_reposicion(id)
);
