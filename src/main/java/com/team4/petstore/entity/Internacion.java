package com.team4.petstore.entity;

import com.team4.petstore.entity.enums.EstadoInternacion;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "internaciones")
public class Internacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mascota_id", nullable = false)
    private Mascota mascota;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veterinario_id", nullable = false)
    private Veterinario veterinario;

    @Column(length = 200)
    private String motivo;

    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDateTime fechaIngreso = LocalDateTime.now();

    @Column(name = "fecha_alta")
    private LocalDateTime fechaAlta;

    @Column(name = "jaula_id", length = 20)
    private String jaulaId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoInternacion estado = EstadoInternacion.ACTIVA;

    @Column(columnDefinition = "TEXT")
    private String notas;

    @OneToMany(mappedBy = "internacion", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("fechaHora ASC")
    private List<EvolucionInternacion> evoluciones = new ArrayList<>();

    public Internacion() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Mascota getMascota() { return mascota; }
    public void setMascota(Mascota mascota) { this.mascota = mascota; }

    public Veterinario getVeterinario() { return veterinario; }
    public void setVeterinario(Veterinario veterinario) { this.veterinario = veterinario; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public LocalDateTime getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDateTime fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    public LocalDateTime getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(LocalDateTime fechaAlta) { this.fechaAlta = fechaAlta; }

    public String getJaulaId() { return jaulaId; }
    public void setJaulaId(String jaulaId) { this.jaulaId = jaulaId; }

    public EstadoInternacion getEstado() { return estado; }
    public void setEstado(EstadoInternacion estado) { this.estado = estado; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public List<EvolucionInternacion> getEvoluciones() { return evoluciones; }
    public void setEvoluciones(List<EvolucionInternacion> evoluciones) { this.evoluciones = evoluciones; }
}