package com.team4.petstore.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "veterinarios")
public class Veterinario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(nullable = false, unique = true, length = 50)
    private String matricula;

    @Column(length = 100)
    private String especialidad;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(nullable = false)
    private Boolean activo = true;

    @ManyToMany(mappedBy = "veterinarios", fetch = FetchType.LAZY)
    private Set<Servicio> servicios = new HashSet<>();

    @OneToMany(mappedBy = "veterinario", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("diaSemana ASC")
    private List<HorarioAtencion> horarios = new ArrayList<>();

    @OneToMany(mappedBy = "veterinario", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("fechaInicio ASC")
    private List<BloqueoFecha> bloqueos = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Veterinario() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public Set<Servicio> getServicios() { return servicios; }
    public void setServicios(Set<Servicio> servicios) { this.servicios = servicios; }

    public List<HorarioAtencion> getHorarios() { return horarios; }
    public void setHorarios(List<HorarioAtencion> horarios) { this.horarios = horarios; }

    public List<BloqueoFecha> getBloqueos() { return bloqueos; }
    public void setBloqueos(List<BloqueoFecha> bloqueos) { this.bloqueos = bloqueos; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public void addHorario(HorarioAtencion horario) {
        horarios.add(horario);
        horario.setVeterinario(this);
    }

    public void removeHorario(HorarioAtencion horario) {
        horarios.remove(horario);
        horario.setVeterinario(null);
    }

    public void addBloqueo(BloqueoFecha bloqueo) {
        bloqueos.add(bloqueo);
        bloqueo.setVeterinario(this);
    }

    public void removeBloqueo(BloqueoFecha bloqueo) {
        bloqueos.remove(bloqueo);
        bloqueo.setVeterinario(null);
    }
}
