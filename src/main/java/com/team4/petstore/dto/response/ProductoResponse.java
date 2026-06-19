package com.team4.petstore.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Respuesta con datos del producto")
public class ProductoResponse {
    @Schema(description = "ID del producto", example = "1")
    private Long id;

    @Schema(description = "Nombre del producto", example = "Alimento Premium para Perros")
    private String nombre;

    @Schema(description = "Codigo unico del producto", example = "ALIM-PERRO-001")
    private String codigo;

    @Schema(description = "Descripcion del producto", example = "Alimento premium para perros adultos")
    private String descripcion;

    @Schema(description = "Precio del producto", example = "45.99")
    private BigDecimal precio;

    @Schema(description = "Cantidad en stock", example = "100")
    private Integer stock;

    @Schema(description = "Nombre de la categoria", example = "Alimentos")
    private String categoria;

    @Schema(description = "Indica si el producto esta activo", example = "true")
    private Boolean activo;

    @Schema(description = "URL de la imagen del producto", example = "https://example.com/images/alimento-perro.jpg")
    private String imagenUrl;

    public ProductoResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
}
