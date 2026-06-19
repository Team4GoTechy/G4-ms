package com.team4.petstore.entity;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "prescripciones")
public class Prescripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consulta_id", nullable = false, unique = true)
    private Consulta consulta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veterinario_id", nullable = false)
    private Veterinario veterinario;

    @Column(name = "fecha")
    private LocalDate fecha = LocalDate.now();

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @OneToMany(mappedBy = "prescripcion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePrescripcion> detalles = new ArrayList<>();

    public Prescripcion() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Consulta getConsulta() { return consulta; }
    public void setConsulta(Consulta consulta) { this.consulta = consulta; }

    public Veterinario getVeterinario() { return veterinario; }
    public void setVeterinario(Veterinario veterinario) { this.veterinario = veterinario; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public List<DetallePrescripcion> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePrescripcion> detalles) { this.detalles = detalles; }
}