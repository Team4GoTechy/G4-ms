CREATE TABLE IF NOT EXISTS servicio_veterinario (
    servicio_id BIGINT NOT NULL REFERENCES servicios(id) ON DELETE CASCADE,
    veterinario_id BIGINT NOT NULL REFERENCES veterinarios(id) ON DELETE CASCADE,
    PRIMARY KEY (servicio_id, veterinario_id)
);

CREATE INDEX IF NOT EXISTS idx_servicio_veterinario_servicio
    ON servicio_veterinario(servicio_id);
CREATE INDEX IF NOT EXISTS idx_servicio_veterinario_veterinario
    ON servicio_veterinario(veterinario_id);
