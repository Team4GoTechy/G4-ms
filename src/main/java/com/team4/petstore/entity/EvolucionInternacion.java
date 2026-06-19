package com.team4.petstore.entity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "evolucion_internacion")
public class EvolucionInternacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "internacion_id", nullable = false)
    private Internacion internacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora = LocalDateTime.now();

    @Column(columnDefinition = "TEXT")
    private String observacion;

    @Column(precision = 6, scale = 2)
    private BigDecimal peso;

    @Column(precision = 4, scale = 1)
    private BigDecimal temperatura;

    public EvolucionInternacion() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Internacion getInternacion() { return internacion; }
    public void setInternacion(Internacion internacion) { this.internacion = internacion; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    public BigDecimal getPeso() { return peso; }
    public void setPeso(BigDecimal peso) { this.peso = peso; }

    public BigDecimal getTemperatura() { return temperatura; }
    public void setTemperatura(BigDecimal temperatura) { this.temperatura = temperatura; }
}