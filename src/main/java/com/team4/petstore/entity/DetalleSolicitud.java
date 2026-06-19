package com.team4.petstore.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "detalles_solicitud")
public class DetalleSolicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insumo_id", nullable = false)
    private Insumo insumo;

    @Column(name = "cantidad_solicitada", nullable = false)
    private Integer cantidadSolicitada;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitud_id", nullable = false)
    private SolicitudReposicion solicitud;

    public DetalleSolicitud() {}

    public DetalleSolicitud(Insumo insumo, Integer cantidadSolicitada) {
        this.insumo = insumo;
        this.cantidadSolicitada = cantidadSolicitada;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Insumo getInsumo() { return insumo; }
    public void setInsumo(Insumo insumo) { this.insumo = insumo; }
    public Integer getCantidadSolicitada() { return cantidadSolicitada; }
    public void setCantidadSolicitada(Integer cantidadSolicitada) { this.cantidadSolicitada = cantidadSolicitada; }
    public SolicitudReposicion getSolicitud() { return solicitud; }
    public void setSolicitud(SolicitudReposicion solicitud) { this.solicitud = solicitud; }
}
