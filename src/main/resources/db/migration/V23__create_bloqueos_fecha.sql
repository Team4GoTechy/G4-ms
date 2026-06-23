CREATE TABLE bloqueos_fecha (
    id BIGSERIAL PRIMARY KEY,
    veterinario_id BIGINT NOT NULL REFERENCES veterinarios(id) ON DELETE CASCADE,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    motivo VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_bloqueo_fechas CHECK (fecha_fin >= fecha_inicio)
);

CREATE INDEX idx_bloqueos_veterinario ON bloqueos_fecha(veterinario_id);
CREATE INDEX idx_bloqueos_fechas ON bloqueos_fecha(fecha_inicio, fecha_fin);
