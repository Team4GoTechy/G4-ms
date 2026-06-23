package com.team4.petstore.service;

import com.team4.petstore.dto.request.BloqueoFechaRequest;
import com.team4.petstore.dto.request.HorarioRequest;
import com.team4.petstore.dto.request.VeterinarioRequest;
import com.team4.petstore.dto.response.BloqueoFechaResponse;
import com.team4.petstore.dto.response.HorarioResponse;
import com.team4.petstore.dto.response.ServicioResponse;
import com.team4.petstore.dto.response.VeterinarioResponse;
import com.team4.petstore.entity.BloqueoFecha;
import com.team4.petstore.entity.HorarioAtencion;
import com.team4.petstore.entity.Rol;
import com.team4.petstore.entity.Servicio;
import com.team4.petstore.entity.Usuario;
import com.team4.petstore.entity.Veterinario;
import com.team4.petstore.exception.BadRequestException;
import com.team4.petstore.exception.ResourceNotFoundException;
import com.team4.petstore.repository.BloqueoFechaRepository;
import com.team4.petstore.repository.HorarioAtencionRepository;
import com.team4.petstore.repository.RolRepository;
import com.team4.petstore.repository.ServicioRepository;
import com.team4.petstore.repository.UsuarioRepository;
import com.team4.petstore.repository.VeterinarioRepository;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class VeterinarioService {

    private final VeterinarioRepository veterinarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final ServicioRepository servicioRepository;
    private final HorarioAtencionRepository horarioAtencionRepository;
    private final BloqueoFechaRepository bloqueoFechaRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String ROLE_VETERINARIO = "ROLE_VETERINARIO";
    private static final Map<Integer, String> NOMBRES_DIAS = Map.of(
            1, "Lunes",
            2, "Martes",
            3, "Miércoles",
            4, "Jueves",
            5, "Viernes",
            6, "Sábado",
            7, "Domingo"
    );

    public VeterinarioService(
            VeterinarioRepository veterinarioRepository,
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            ServicioRepository servicioRepository,
            HorarioAtencionRepository horarioAtencionRepository,
            BloqueoFechaRepository bloqueoFechaRepository,
            PasswordEncoder passwordEncoder) {
        this.veterinarioRepository = veterinarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.servicioRepository = servicioRepository;
        this.horarioAtencionRepository = horarioAtencionRepository;
        this.bloqueoFechaRepository = bloqueoFechaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<VeterinarioResponse> listar() {
        return veterinarioRepository.findByActivoTrue().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<VeterinarioResponse> listarTodos() {
        return veterinarioRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VeterinarioResponse obtenerPorId(@NonNull Long id) {
        Veterinario veterinario = veterinarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinario no encontrado con id: " + id));
        return toResponse(veterinario);
    }

    @Transactional
    public VeterinarioResponse crear(@NonNull VeterinarioRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("El correo electrónico ya está registrado");
        }

        if (veterinarioRepository.existsByMatricula(request.getMatricula())) {
            throw new BadRequestException("El número de matrícula ya está registrado");
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new BadRequestException("La contraseña es obligatoria al crear un veterinario");
        }

        Set<Servicio> servicios = resolverServicios(request.getServicioIds());

        Rol rolVeterinario = rolRepository.findByNombre(ROLE_VETERINARIO)
                .orElseThrow(() -> new ResourceNotFoundException("Rol VETERINARIO no encontrado"));

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setCelular(request.getTelefono());
        usuario.setActivo(true);
        usuario.addRol(rolVeterinario);
        usuario = usuarioRepository.save(usuario);

        Veterinario veterinario = new Veterinario();
        veterinario.setUsuario(usuario);
        veterinario.setMatricula(request.getMatricula());
        veterinario.setEspecialidad(request.getEspecialidad());
        veterinario.setBio(request.getBio());
        veterinario.setServicios(servicios);
        veterinario.setActivo(true);
        veterinario = veterinarioRepository.save(veterinario);

        inicializarHorarios(veterinario);

        return toResponse(veterinario);
    }

    @Transactional
    public VeterinarioResponse actualizar(@NonNull Long id, @NonNull VeterinarioRequest request) {
        Veterinario veterinario = veterinarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinario no encontrado con id: " + id));

        Usuario usuario = veterinario.getUsuario();

        if (!usuario.getEmail().equals(request.getEmail()) &&
                usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("El correo electrónico ya está registrado");
        }

        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setEmail(request.getEmail());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        usuario.setCelular(request.getTelefono());
        usuarioRepository.save(usuario);

        if (!veterinario.getMatricula().equals(request.getMatricula()) &&
                veterinarioRepository.existsByMatricula(request.getMatricula())) {
            throw new BadRequestException("El número de matrícula ya está registrado");
        }

        Set<Servicio> servicios = resolverServicios(request.getServicioIds());

        veterinario.setMatricula(request.getMatricula());
        veterinario.setEspecialidad(request.getEspecialidad());
        veterinario.setBio(request.getBio());
        veterinario.setServicios(servicios);
        veterinario = veterinarioRepository.save(veterinario);

        return toResponse(veterinario);
    }

    @Transactional
    public void eliminar(@NonNull Long id) {
        Veterinario veterinario = veterinarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinario no encontrado con id: " + id));
        veterinario.setActivo(false);
        veterinarioRepository.save(veterinario);
    }

    @Transactional
    public VeterinarioResponse cambiarEstado(@NonNull Long id, @NonNull Boolean activo) {
        Veterinario veterinario = veterinarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinario no encontrado con id: " + id));
        veterinario.setActivo(activo);
        veterinario = veterinarioRepository.save(veterinario);
        return toResponse(veterinario);
    }

    @Transactional(readOnly = true)
    public List<HorarioResponse> listarHorarios(@NonNull Long veterinarioId) {
        if (!veterinarioRepository.existsById(veterinarioId)) {
            throw new ResourceNotFoundException("Veterinario no encontrado con id: " + veterinarioId);
        }
        return horarioAtencionRepository.findByVeterinarioIdOrderByDiaSemanaAsc(veterinarioId)
                .stream()
                .map(this::toHorarioResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<HorarioResponse> actualizarHorarios(@NonNull Long veterinarioId, @NonNull HorarioRequest request) {
        Veterinario veterinario = veterinarioRepository.findById(veterinarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinario no encontrado con id: " + veterinarioId));

        horarioAtencionRepository.deleteByVeterinarioId(veterinarioId);

        List<HorarioAtencion> horarios = new ArrayList<>();
        for (HorarioRequest.HorarioDiaRequest diaRequest : request.getHorarios()) {
            HorarioAtencion horario = new HorarioAtencion();
            horario.setVeterinario(veterinario);
            horario.setDiaSemana(diaRequest.getDiaSemana());
            horario.setTrabaja(diaRequest.getTrabaja());
            if (Boolean.TRUE.equals(diaRequest.getTrabaja())) {
                if (diaRequest.getHoraInicio() == null || diaRequest.getHoraFin() == null) {
                    throw new BadRequestException("Debe especificar hora de inicio y fin para días que trabaja");
                }
                if (!diaRequest.getHoraInicio().isBefore(diaRequest.getHoraFin())) {
                    throw new BadRequestException("La hora de inicio debe ser anterior a la hora de fin");
                }
                horario.setHoraInicio(diaRequest.getHoraInicio());
                horario.setHoraFin(diaRequest.getHoraFin());
            }
            horarios.add(horario);
        }

        horarioAtencionRepository.saveAll(horarios);

        return horarios.stream()
                .map(this::toHorarioResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BloqueoFechaResponse> listarBloqueos(@NonNull Long veterinarioId) {
        if (!veterinarioRepository.existsById(veterinarioId)) {
            throw new ResourceNotFoundException("Veterinario no encontrado con id: " + veterinarioId);
        }
        return bloqueoFechaRepository.findByVeterinarioIdOrderByFechaInicioAsc(veterinarioId)
                .stream()
                .map(this::toBloqueoFechaResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public BloqueoFechaResponse crearBloqueo(@NonNull Long veterinarioId, @NonNull BloqueoFechaRequest request) {
        Veterinario veterinario = veterinarioRepository.findById(veterinarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinario no encontrado con id: " + veterinarioId));

        if (request.getFechaFin().isBefore(request.getFechaInicio())) {
            throw new BadRequestException("La fecha de fin debe ser posterior a la fecha de inicio");
        }

        boolean existeSolapamiento = bloqueoFechaRepository
                .existsByVeterinarioIdAndFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(
                        veterinarioId, request.getFechaFin(), request.getFechaInicio());

        if (existeSolapamiento) {
            throw new BadRequestException("Ya existe un bloqueo que se superpone con las fechas ingresadas");
        }

        BloqueoFecha bloqueo = new BloqueoFecha();
        bloqueo.setVeterinario(veterinario);
        bloqueo.setFechaInicio(request.getFechaInicio());
        bloqueo.setFechaFin(request.getFechaFin());
        bloqueo.setMotivo(request.getMotivo());
        bloqueo = bloqueoFechaRepository.save(bloqueo);

        return toBloqueoFechaResponse(bloqueo);
    }

    @Transactional
    public void eliminarBloqueo(@NonNull Long veterinarioId, @NonNull Long bloqueoId) {
        if (!veterinarioRepository.existsById(veterinarioId)) {
            throw new ResourceNotFoundException("Veterinario no encontrado con id: " + veterinarioId);
        }

        BloqueoFecha bloqueo = bloqueoFechaRepository.findById(bloqueoId)
                .orElseThrow(() -> new ResourceNotFoundException("Bloqueo no encontrado con id: " + bloqueoId));

        if (!bloqueo.getVeterinario().getId().equals(veterinarioId)) {
            throw new BadRequestException("El bloqueo no pertenece a este veterinario");
        }

        bloqueoFechaRepository.delete(bloqueo);
    }

    private void inicializarHorarios(Veterinario veterinario) {
        List<HorarioAtencion> horarios = new ArrayList<>();
        for (int dia = 1; dia <= 7; dia++) {
            HorarioAtencion horario = new HorarioAtencion();
            horario.setVeterinario(veterinario);
            horario.setDiaSemana(dia);
            horario.setTrabaja(false);
            horarios.add(horario);
        }
        horarioAtencionRepository.saveAll(horarios);
    }

    private VeterinarioResponse toResponse(Veterinario veterinario) {
        VeterinarioResponse response = new VeterinarioResponse();
        response.setId(veterinario.getId());
        response.setUsuarioId(veterinario.getUsuario().getId());
        response.setNombreCompleto(String.format("%s %s",
                veterinario.getUsuario().getNombre(),
                veterinario.getUsuario().getApellido()));
        response.setAvatar(veterinario.getUsuario().getAvatar());
        response.setEmail(veterinario.getUsuario().getEmail());
        response.setTelefono(veterinario.getUsuario().getCelular());
        response.setMatricula(veterinario.getMatricula());
        response.setEspecialidad(veterinario.getEspecialidad());
        response.setBio(veterinario.getBio());
        response.setActivo(veterinario.getActivo());
        response.setServicios(veterinario.getServicios().stream()
                .map(s -> new ServicioResponse(s.getId(), s.getNombre(), s.getDescripcion(), s.getPrecio(), null))
                .collect(Collectors.toSet()));
        response.setFechaCreacion(veterinario.getCreatedAt());
        return response;
    }

    private Set<Servicio> resolverServicios(Set<Long> servicioIds) {
        if (servicioIds == null || servicioIds.isEmpty()) {
            throw new BadRequestException("Debe asignar al menos un servicio");
        }
        Set<Servicio> servicios = new HashSet<>(servicioRepository.findAllById(servicioIds));
        if (servicios.size() != servicioIds.size()) {
            throw new ResourceNotFoundException("Uno o más servicios no existen");
        }
        return servicios;
    }

    private HorarioResponse toHorarioResponse(HorarioAtencion horario) {
        HorarioResponse response = new HorarioResponse();
        response.setId(horario.getId());
        response.setDiaSemana(horario.getDiaSemana());
        response.setNombreDia(NOMBRES_DIAS.get(horario.getDiaSemana()));
        response.setHoraInicio(horario.getHoraInicio());
        response.setHoraFin(horario.getHoraFin());
        response.setTrabaja(horario.getTrabaja());
        return response;
    }

    private BloqueoFechaResponse toBloqueoFechaResponse(BloqueoFecha bloqueo) {
        BloqueoFechaResponse response = new BloqueoFechaResponse();
        response.setId(bloqueo.getId());
        response.setFechaInicio(bloqueo.getFechaInicio());
        response.setFechaFin(bloqueo.getFechaFin());
        response.setMotivo(bloqueo.getMotivo());
        response.setCreatedAt(bloqueo.getCreatedAt());
        return response;
    }
}
