package com.team4.petstore.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Schema(description = "Solicitud para crear o actualizar un producto")
public class ProductoRequest {

    @Schema(description = "Nombre del producto", example = "Alimento Premium para Perros")
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Schema(description = "Codigo unico del producto", example = "ALIM-PERRO-001")
    @NotBlank(message = "El codigo es obligatorio")
    private String codigo;

    @Schema(description = "Descripcion del producto", example = "Alimento premium para perros adultos")
    private String descripcion;

    @Schema(description = "Precio del producto", example = "45.99")
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private BigDecimal precio;

    @Schema(description = "Cantidad en stock", example = "100")
    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    @Schema(description = "Unidad de medida del producto", example = "kg", allowableValues = {"g", "kg", "ml", "L", "unidad"})
    private String unidadMedida;

    @Schema(description = "ID de la categoria", example = "1")
    private Long categoriaId;

    @Schema(description = "URL de la imagen del producto", example = "https://example.com/images/alimento-perro.jpg")
    private String imagenUrl;

    public ProductoRequest() {}

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
    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }
    public Long getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Long categoriaId) { this.categoriaId = categoriaId; }
    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
}
