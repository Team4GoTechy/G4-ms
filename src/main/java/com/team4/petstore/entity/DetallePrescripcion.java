package com.team4.petstore.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "detalle_prescripcion")
public class DetallePrescripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescripcion_id", nullable = false)
    private Prescripcion prescripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(length = 100)
    private String dosis;

    @Column(length = 100)
    private String frecuencia;

    @Column(length = 100)
    private String duracion;

    @Column(name = "via_administracion", length = 50)
    private String viaAdministracion;

    @Column(columnDefinition = "TEXT")
    private String instrucciones;

    public DetallePrescripcion() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Prescripcion getPrescripcion() { return prescripcion; }
    public void setPrescripcion(Prescripcion prescripcion) { this.prescripcion = prescripcion; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public String getDosis() { return dosis; }
    public void setDosis(String dosis) { this.dosis = dosis; }

    public String getFrecuencia() { return frecuencia; }
    public void setFrecuencia(String frecuencia) { this.frecuencia = frecuencia; }

    public String getDuracion() { return duracion; }
    public void setDuracion(String duracion) { this.duracion = duracion; }

    public String getViaAdministracion() { return viaAdministracion; }
    public void setViaAdministracion(String viaAdministracion) { this.viaAdministracion = viaAdministracion; }

    public String getInstrucciones() { return instrucciones; }
    public void setInstrucciones(String instrucciones) { this.instrucciones = instrucciones; }
}