package com.team4.petstore.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    /**
     * Sube una imagen a Cloudinary y devuelve la URL segura.
     *
     * @param file       Archivo a subir
     * @param folder     Carpeta en Cloudinary (ej: "petshop/products", "petshop/avatars")
     * @param width      Ancho de imagen (opcional, 0 para ignorar)
     * @param height     Alto de imagen (opcional, 0 para ignorar)
     * @return URL segura de la imagen subida
     */
    public String uploadImage(MultipartFile file, String folder, int width, int height) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("El archivo no puede estar vacio");
        }

        // Validar tipo de archivo
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("El archivo debe ser una imagen");
        }

        // Validar tamaño (máximo 5MB)
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("El archivo debe ser menor a 5MB");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("folder", folder);
        params.put("resource_type", "image");
        params.put("overwrite", true);
        params.put("transformation", buildTransformation(width, height));

        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), params);

        return (String) uploadResult.get("secure_url");
    }

    /**
     * Sube una imagen de producto con transformación para tamaño estándar.
     */
    public String uploadProductImage(MultipartFile file) throws IOException {
        return uploadImage(file, "petshop/products", 800, 600);
    }

    /**
     * Sube una imagen de avatar con transformación circular.
     */
    public String uploadAvatarImage(MultipartFile file) throws IOException {
        return uploadImage(file, "petshop/avatars", 200, 200);
    }

    /**
     * Sube una imagen de mascota con transformación para tamaño estándar.
     */
    public String uploadMascotaImage(MultipartFile file) throws IOException {
        return uploadImage(file, "petshop/mascotas", 400, 400);
    }

    /**
     * Elimina una imagen de Cloudinary dado su public_id.
     */
    public void deleteImage(String publicId) throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("resource_type", "image");
        cloudinary.uploader().destroy(publicId, params);
    }

    /**
     * Construye la transformación de imagen para Cloudinary.
     */
    private Transformation buildTransformation(int width, int height) {
        Transformation transformation = new Transformation();

        if (width > 0 && height > 0) {
            transformation = transformation.width(width).height(height).crop("fill");
        } else if (width > 0) {
            transformation = transformation.width(width).crop("scale");
        } else if (height > 0) {
            transformation = transformation.height(height).crop("scale");
        }

        return transformation.quality("auto").fetchFormat("auto");
    }
}
