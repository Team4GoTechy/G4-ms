package com.team4.petstore.service;

import com.team4.petstore.dto.request.DetallePrescripcionRequest;
import com.team4.petstore.dto.request.PrescripcionRequest;
import com.team4.petstore.dto.response.DetallePrescripcionResponse;
import com.team4.petstore.dto.response.PrescripcionResponse;
import com.team4.petstore.entity.Consulta;
import com.team4.petstore.entity.DetallePrescripcion;
import com.team4.petstore.entity.Prescripcion;
import com.team4.petstore.entity.Producto;
import com.team4.petstore.entity.Veterinario;
import com.team4.petstore.exception.ResourceNotFoundException;
import com.team4.petstore.repository.ConsultaRepository;
import com.team4.petstore.repository.PrescripcionRepository;
import com.team4.petstore.repository.ProductoRepository;
import com.team4.petstore.repository.UsuarioRepository;
import com.team4.petstore.repository.VeterinarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PrescripcionService {

    private final PrescripcionRepository prescripcionRepository;
    private final ConsultaRepository consultaRepository;
    private final VeterinarioRepository veterinarioRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final StockService stockService;

    public PrescripcionService(PrescripcionRepository prescripcionRepository,
                               ConsultaRepository consultaRepository,
                               VeterinarioRepository veterinarioRepository,
                               ProductoRepository productoRepository,
                               UsuarioRepository usuarioRepository,
                               StockService stockService) {
        this.prescripcionRepository = prescripcionRepository;
        this.consultaRepository = consultaRepository;
        this.veterinarioRepository = veterinarioRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
        this.stockService = stockService;
    }

    // email viene del token JWT via @AuthenticationPrincipal en el controller
    @Transactional
    public PrescripcionResponse crear(PrescripcionRequest dto, String emailVeterinario) {
        Consulta consulta = consultaRepository.findById(dto.getConsultaId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Consulta no encontrada con id: " + dto.getConsultaId()));

        // Buscamos el Veterinario a través del Usuario cuyo email conocemos
        Veterinario veterinario = usuarioRepository.findByEmail(emailVeterinario)
            .flatMap(u -> veterinarioRepository.findByUsuarioId(u.getId()))
            .orElseThrow(() -> new ResourceNotFoundException(
                "Veterinario no encontrado para el usuario: " + emailVeterinario));

        Prescripcion prescripcion = new Prescripcion();
        prescripcion.setConsulta(consulta);
        prescripcion.setVeterinario(veterinario);
        prescripcion.setObservaciones(dto.getObservaciones());

        List<DetallePrescripcion> detalles = new ArrayList<>();
        for (DetallePrescripcionRequest detalleDto : dto.getDetalles()) {
            Producto producto = productoRepository.findById(detalleDto.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Producto no encontrado con id: " + detalleDto.getProductoId()));

            // Si no hay stock lanza StockInsuficienteException
            // @Transactional revierte prescripcion + todos los descuentos anteriores
            stockService.descontar(producto.getId(), 1);

            DetallePrescripcion detalle = new DetallePrescripcion();
            detalle.setPrescripcion(prescripcion);
            detalle.setProducto(producto);
            detalle.setDosis(detalleDto.getDosis());
            detalle.setFrecuencia(detalleDto.getFrecuencia());
            detalle.setDuracion(detalleDto.getDuracion());
            detalle.setViaAdministracion(detalleDto.getViaAdministracion());
            detalle.setInstrucciones(detalleDto.getInstrucciones());
            detalles.add(detalle);
        }

        prescripcion.setDetalles(detalles);
        return mapToResponse(prescripcionRepository.save(prescripcion));
    }

    @Transactional(readOnly = true)
    public PrescripcionResponse buscarPorConsulta(Long consultaId) {
        Prescripcion prescripcion = prescripcionRepository.findByConsultaId(consultaId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Prescripción no encontrada para la consulta: " + consultaId));
        return mapToResponse(prescripcion);
    }

    private PrescripcionResponse mapToResponse(Prescripcion p) {
        PrescripcionResponse dto = new PrescripcionResponse();
        dto.setId(p.getId());
        dto.setConsultaId(p.getConsulta().getId());
        dto.setVeterinarioId(p.getVeterinario().getId());
        dto.setVeterinarioNombre(p.getVeterinario().getUsuario().getNombre());
        dto.setFecha(p.getFecha());
        dto.setObservaciones(p.getObservaciones());

        List<DetallePrescripcionResponse> detallesDto = new ArrayList<>();
        for (DetallePrescripcion d : p.getDetalles()) {
            DetallePrescripcionResponse detalleDto = new DetallePrescripcionResponse();
            detalleDto.setId(d.getId());
            detalleDto.setProductoId(d.getProducto().getId());
            detalleDto.setProductoNombre(d.getProducto().getNombre());
            detalleDto.setDosis(d.getDosis());
            detalleDto.setFrecuencia(d.getFrecuencia());
            detalleDto.setDuracion(d.getDuracion());
            detalleDto.setViaAdministracion(d.getViaAdministracion());
            detalleDto.setInstrucciones(d.getInstrucciones());
            detallesDto.add(detalleDto);
        }
        dto.setDetalles(detallesDto);
        return dto;
    }
}