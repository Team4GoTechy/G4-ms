CREATE TABLE veterinarios (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL UNIQUE REFERENCES usuarios(id),
    matricula VARCHAR(50) NOT NULL UNIQUE,
    especialidad VARCHAR(100),
    bio TEXT,
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_veterinario_usuario ON veterinarios(usuario_id);
CREATE INDEX idx_veterinario_matricula ON veterinarios(matricula);
CREATE INDEX idx_veterinario_activo ON veterinarios(activo);
