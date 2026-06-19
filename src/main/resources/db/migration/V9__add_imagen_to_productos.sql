-- Agregar columna de URL de imagen a la tabla productos
ALTER TABLE productos ADD COLUMN IF NOT EXISTS imagen_url VARCHAR(500);
