package com.team4.petstore.config;

import com.team4.petstore.entity.Insumo;
import com.team4.petstore.entity.Mascota;
import com.team4.petstore.entity.Proveedor;
import com.team4.petstore.entity.StockInsumo;
import com.team4.petstore.entity.Usuario;
import com.team4.petstore.repository.InsumoRepository;
import com.team4.petstore.repository.MascotaRepository;
import com.team4.petstore.repository.ProveedorRepository;
import com.team4.petstore.repository.RolRepository;
import com.team4.petstore.repository.StockInsumoRepository;
import com.team4.petstore.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

@Configuration
public class DataSeederConfig {

    private static final Logger log = LoggerFactory.getLogger(DataSeederConfig.class);

    @Bean
    @Profile("!test")
    public CommandLineRunner seedData(UsuarioRepository usuarioRepository,
                                      RolRepository rolRepository,
                                      MascotaRepository mascotaRepository,
                                      ProveedorRepository proveedorRepository,
                                      InsumoRepository insumoRepository,
                                      StockInsumoRepository stockInsumoRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            if (usuarioRepository.count() > 0) {
                log.info("Users already exist, skipping user data seeding");
            } else {
                seedUsers(usuarioRepository, rolRepository, mascotaRepository, passwordEncoder);
            }

            if (proveedorRepository.count() == 0) {
                seedProveedores(proveedorRepository);
            } else {
                log.info("Proveedores already exist, skipping");
            }

            if (insumoRepository.count() == 0) {
                seedInsumos(insumoRepository, stockInsumoRepository);
            } else {
                log.info("Insumos already exist, skipping");
            }

            log.info("Data seeding completed!");
        };
    }

    private void seedUsers(UsuarioRepository usuarioRepository,
                          RolRepository rolRepository,
                          MascotaRepository mascotaRepository,
                          PasswordEncoder passwordEncoder) {
        log.info("Seeding default users: ADMIN, DOCTOR, CLIENTE...");

        String encodedPassword = passwordEncoder.encode("12345678");

        Usuario admin = crearUsuario("Admin", "Sistema", "admin@petshop.com", encodedPassword);
        admin.addRol(rolRepository.findByNombre("ROLE_ADMIN")
            .orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found")));
        usuarioRepository.save(admin);
        log.info("Created ADMIN: admin@petshop.com");

        Usuario doctor = crearUsuario("Doctor", "Veterinario", "doctor@petshop.com", encodedPassword);
        doctor.addRol(rolRepository.findByNombre("ROLE_VETERINARIO")
            .orElseThrow(() -> new RuntimeException("ROLE_VETERINARIO not found")));
        usuarioRepository.save(doctor);
        log.info("Created DOCTOR with ROLE_VETERINARIO: doctor@petshop.com");

        Usuario cliente = crearUsuario("Cliente", "Demo", "cliente@petshop.com", encodedPassword);
        cliente.addRol(rolRepository.findByNombre("ROLE_CLIENTE")
            .orElseThrow(() -> new RuntimeException("ROLE_CLIENTE not found")));
        cliente.addMascota(new Mascota("Max", "Macho", "Perro", cliente));
        cliente.addMascota(new Mascota("Nube", "Hembra", "Gato", cliente));
        usuarioRepository.save(cliente);
        log.info("Created CLIENTE: cliente@petshop.com with 2 pets (Max and Nube)");
    }

    private void seedProveedores(ProveedorRepository proveedorRepository) {
        log.info("Seeding default proveedores...");

        Proveedor prov1 = new Proveedor();
        prov1.setNombre("Distribuidora Medica ABC");
        prov1.setEmail("ventas@distmedicaabc.com");
        prov1.setTelefono("351-555-1001");
        prov1.setActivo(true);
        proveedorRepository.save(prov1);

        Proveedor prov2 = new Proveedor();
        prov2.setNombre("Farmacia Veterinaria Norte");
        prov2.setEmail("info@farmavetnorte.com");
        prov2.setTelefono("351-555-2002");
        prov2.setActivo(true);
        proveedorRepository.save(prov2);

        Proveedor prov3 = new Proveedor();
        prov3.setNombre("Insumos Veterinarios del Sur");
        prov3.setEmail("pedidos@ivsur.com");
        prov3.setTelefono("351-555-3003");
        prov3.setActivo(true);
        proveedorRepository.save(prov3);

        log.info("Created 3 proveedores");
    }

    private void seedInsumos(InsumoRepository insumoRepository, StockInsumoRepository stockInsumoRepository) {
        log.info("Seeding default insumos...");

        crearInsumo(insumoRepository, stockInsumoRepository, "Vacuna Antirrabica", "Vacuna para prevencion de rabia canina", "unidades", new BigDecimal("25.50"), 20);
        crearInsumo(insumoRepository, stockInsumoRepository, "Vacuna Sextuple", "Vacuna para perros contra moquillo, hepatitis, leptospirosis, parainfluenza y parvovirus", "unidades", new BigDecimal("35.00"), 15);
        crearInsumo(insumoRepository, stockInsumoRepository, "Vacuna Triple Felina", "Vacuna para gatos contra rinotraqueitis, calcivirus y panleucopenia", "unidades", new BigDecimal("30.00"), 15);
        crearInsumo(insumoRepository, stockInsumoRepository, "Antibiotico Amoxicilina", "Antibiotico de amplio espectro para infecciones bacterianas", "unidades", new BigDecimal("18.75"), 50);
        crearInsumo(insumoRepository, stockInsumoRepository, "Antiparasitario Ivermectina", "Tratamiento contra parasitos internos y externos", "ml", new BigDecimal("12.00"), 30);
        crearInsumo(insumoRepository, stockInsumoRepository, "Suero Fisilogico", "Solucion salina para limpieza de heridas", "ml", new BigDecimal("5.00"), 100);
        crearInsumo(insumoRepository, stockInsumoRepository, "Gasas Esteriles", "Gasas para curaciones", "unidades", new BigDecimal("8.50"), 40);
        crearInsumo(insumoRepository, stockInsumoRepository, "Jeringas 5ml", "Jeringas descartables de 5ml", "unidades", new BigDecimal("3.25"), 60);
        crearInsumo(insumoRepository, stockInsumoRepository, "Anestesico Ketamina", "Anestesico general para procedimientos", "ml", new BigDecimal("45.00"), 10);
        crearInsumo(insumoRepository, stockInsumoRepository, "Antiinflamatorio Meloxicam", "Antiinflamatorio no esteroideo", "unidades", new BigDecimal("22.00"), 25);

        log.info("Created 10 insumos with stock");
    }

    private void crearInsumo(InsumoRepository insumoRepository, StockInsumoRepository stockInsumoRepository,
                             String nombre, String descripcion, String unidad, BigDecimal precio, Integer stockMinimo) {
        Insumo insumo = new Insumo();
        insumo.setNombre(nombre);
        insumo.setDescripcion(descripcion);
        insumo.setUnidadMedida(unidad);
        insumo.setPrecioUnitario(precio);
        insumo.setStockMinimo(stockMinimo);
        insumo.setActivo(true);
        insumo = insumoRepository.save(insumo);

        StockInsumo stock = new StockInsumo(insumo);
        stockInsumoRepository.save(stock);
    }

    private Usuario crearUsuario(String nombre, String apellido, String email, String password) {
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setEmail(email);
        usuario.setPassword(password);
        usuario.setActivo(true);
        return usuario;
    }
}
