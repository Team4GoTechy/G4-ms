package com.team4.petstore.controller;

import com.team4.petstore.service.CloudinaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/upload")
@Tag(name = "Upload", description = "Endpoints para subir archivos e imagenes usando Cloudinary")
public class ImageController {

    private final CloudinaryService cloudinaryService;

    public ImageController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @Operation(summary = "Subir imagen de producto", description = "Sube una imagen de producto a Cloudinary y devuelve la URL (solo ADMIN)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Imagen subida exitosamente"),
        @ApiResponse(responseCode = "400", description = "Archivo no valido o no es una imagen"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - solo ADMIN")
    })
    @PostMapping("/producto")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> subirImagenProducto(@RequestParam("file") MultipartFile file) {
        try {
            String url = cloudinaryService.uploadProductImage(file);
            Map<String, String> response = new HashMap<>();
            response.put("url", url);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al subir la imagen: " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Subir avatar de usuario", description = "Sube una imagen de avatar a Cloudinary y devuelve la URL (cualquier usuario autenticado)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Avatar subido exitosamente"),
        @ApiResponse(responseCode = "400", description = "Archivo no valido o no es una imagen"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @PostMapping("/avatar")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, String>> subirAvatar(@RequestParam("file") MultipartFile file) {
        try {
            String url = cloudinaryService.uploadAvatarImage(file);
            Map<String, String> response = new HashMap<>();
            response.put("url", url);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al subir la imagen: " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Subir imagen de mascota", description = "Sube una imagen de mascota a Cloudinary y devuelve la URL (cualquier usuario autenticado)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Imagen subida exitosamente"),
        @ApiResponse(responseCode = "400", description = "Archivo no valido o no es una imagen"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @PostMapping("/mascota")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, String>> subirImagenMascota(@RequestParam("file") MultipartFile file) {
        try {
            String url = cloudinaryService.uploadMascotaImage(file);
            Map<String, String> response = new HashMap<>();
            response.put("url", url);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al subir la imagen: " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
