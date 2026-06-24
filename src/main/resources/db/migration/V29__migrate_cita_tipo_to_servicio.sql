-- Cambiar Cita.tipoCita (enum string) por Cita.servicio (FK)
-- Asume que la tabla citas está vacía o los datos de prueba se manejarán manualmente
ALTER TABLE citas DROP COLUMN IF EXISTS tipo_cita;
ALTER TABLE citas ADD COLUMN servicio_id BIGINT;
ALTER TABLE citas ADD CONSTRAINT fk_cita_servicio
    FOREIGN KEY (servicio_id) REFERENCES servicios(id);
CREATE INDEX idx_citas_servicio ON citas(servicio_id);
