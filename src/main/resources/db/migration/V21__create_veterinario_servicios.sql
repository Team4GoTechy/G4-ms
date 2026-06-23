CREATE TABLE veterinario_servicios (
    veterinario_id BIGINT NOT NULL REFERENCES veterinarios(id) ON DELETE CASCADE,
    servicio VARCHAR(20) NOT NULL,
    PRIMARY KEY (veterinario_id, servicio),
    CONSTRAINT chk_servicio CHECK (servicio IN ('CONSULTA', 'VACUNACION', 'CIRUGIA', 'GROOMING', 'CONTROL'))
);

CREATE INDEX idx_vet_servicios_veterinario ON veterinario_servicios(veterinario_id);
