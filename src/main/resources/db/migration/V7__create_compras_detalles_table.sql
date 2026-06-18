CREATE TABLE compras_detalles (
    id BIGSERIAL PRIMARY KEY,
    compra_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    cantidad INTEGER NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (compra_id) REFERENCES compras(id),
    FOREIGN KEY (producto_id) REFERENCES productos(id)
);

CREATE INDEX idx_compras_detalles_compra ON compras_detalles(compra_id);
