CREATE TABLE stock_insumos (
    id BIGSERIAL PRIMARY KEY,
    insumo_id BIGINT NOT NULL REFERENCES insumos(id),
    cantidad_actual INTEGER NOT NULL DEFAULT 0,
    UNIQUE(insumo_id)
);
