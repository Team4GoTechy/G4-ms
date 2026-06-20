package com.team4.petstore.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "stock_insumos")
public class StockInsumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insumo_id", nullable = false)
    private Insumo insumo;

    @Column(name = "cantidad_actual", nullable = false)
    private Integer cantidadActual = 0;

    public StockInsumo() {}

    public StockInsumo(Insumo insumo) {
        this.insumo = insumo;
        this.cantidadActual = 0;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Insumo getInsumo() { return insumo; }
    public void setInsumo(Insumo insumo) { this.insumo = insumo; }
    public Integer getCantidadActual() { return cantidadActual; }
    public void setCantidadActual(Integer cantidadActual) { this.cantidadActual = cantidadActual; }
}
