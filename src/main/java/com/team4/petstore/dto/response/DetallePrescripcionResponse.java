package com.team4.petstore.dto.response;

public class DetallePrescripcionResponse {

    private Long id;
    private Long productoId;
    private String productoNombre;
    private String dosis;
    private String frecuencia;
    private String duracion;
    private String viaAdministracion;
    private String instrucciones;

    public DetallePrescripcionResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }

    public String getProductoNombre() { return productoNombre; }
    public void setProductoNombre(String productoNombre) { this.productoNombre = productoNombre; }

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