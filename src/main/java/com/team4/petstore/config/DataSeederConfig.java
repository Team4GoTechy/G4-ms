package com.team4.petstore.config;

import com.team4.petstore.entity.Mascota;
import com.team4.petstore.entity.Usuario;
import com.team4.petstore.repository.MascotaRepository;
import com.team4.petstore.repository.RolRepository;
import com.team4.petstore.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeederConfig {

    private static final Logger log = LoggerFactory.getLogger(DataSeederConfig.class);

    @Bean
    @Profile("!test")
    public CommandLineRunner seedData(UsuarioRepository usuarioRepository,
                                        RolRepository rolRepository,
                                        MascotaRepository mascotaRepository,
                                        PasswordEncoder passwordEncoder) {
        return args -> {
            if (usuarioRepository.count() > 0) {
                log.info("Users already exist, skipping data seeding");
                return;
            }

            log.info("Seeding default users: ADMIN, DOCTOR, CLIENTE...");

            String encodedPassword = passwordEncoder.encode("12345678");

            Usuario admin = crearUsuario("Admin", "Sistema", "admin@petshop.com", encodedPassword);
            admin.addRol(rolRepository.findByNombre("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found")));
            usuarioRepository.save(admin);
            log.info("Created ADMIN: admin@petshop.com");

            Usuario doctor = crearUsuario("Doctor", "Veterinario", "doctor@petshop.com", encodedPassword);
            doctor.addRol(rolRepository.findByNombre("ROLE_DOCTOR")
                .orElseThrow(() -> new RuntimeException("ROLE_DOCTOR not found")));
            usuarioRepository.save(doctor);
            log.info("Created DOCTOR: doctor@petshop.com");

            Usuario cliente = crearUsuario("Cliente", "Demo", "cliente@petshop.com", encodedPassword);
            cliente.addRol(rolRepository.findByNombre("ROLE_CLIENTE")
                .orElseThrow(() -> new RuntimeException("ROLE_CLIENTE not found")));
            cliente.addMascota(new Mascota("Max", "Macho", "Perro", cliente));
            cliente.addMascota(new Mascota("Nube", "Hembra", "Gato", cliente));
            usuarioRepository.save(cliente);
            log.info("Created CLIENTE: cliente@petshop.com with 2 pets (Max and Nube)");

            log.info("Data seeding completed successfully!");
        };
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
