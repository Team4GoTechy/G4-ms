CREATE TABLE horarios_atencion (
    id BIGSERIAL PRIMARY KEY,
    veterinario_id BIGINT NOT NULL REFERENCES veterinarios(id) ON DELETE CASCADE,
    dia_semana INT NOT NULL CHECK (dia_semana BETWEEN 1 AND 7),
    hora_inicio TIME,
    hora_fin TIME,
    trabaja BOOLEAN DEFAULT FALSE,
    UNIQUE(veterinario_id, dia_semana),
    CONSTRAINT chk_horario CHECK (trabaja = FALSE OR (hora_inicio IS NOT NULL AND hora_fin IS NOT NULL AND hora_inicio < hora_fin))
);

CREATE INDEX idx_horarios_veterinario ON horarios_atencion(veterinario_id);
