package com.team4.petstore.service;

import com.team4.petstore.dto.request.SolicitudReposicionRequest;
import com.team4.petstore.dto.response.SolicitudReposicionResponse;
import com.team4.petstore.entity.*;
import com.team4.petstore.exception.BadRequestException;
import com.team4.petstore.exception.ResourceNotFoundException;
import com.team4.petstore.repository.*;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SolicitudReposicionService {

    private final SolicitudReposicionRepository solicitudRepository;
    private final InsumoRepository insumoRepository;
    private final UsuarioRepository usuarioRepository;
    private final OrdenCompraRepository ordenRepository;
    private final ProveedorRepository proveedorRepository;
    private final StockInsumoRepository stockRepository;
    private final MovimientoInsumoService movimientoService;

    public SolicitudReposicionService(SolicitudReposicionRepository solicitudRepository,
                                    InsumoRepository insumoRepository,
                                    UsuarioRepository usuarioRepository,
                                    OrdenCompraRepository ordenRepository,
                                    ProveedorRepository proveedorRepository,
                                    StockInsumoRepository stockRepository,
                                    MovimientoInsumoService movimientoService) {
        this.solicitudRepository = solicitudRepository;
        this.insumoRepository = insumoRepository;
        this.usuarioRepository = usuarioRepository;
        this.ordenRepository = ordenRepository;
        this.proveedorRepository = proveedorRepository;
        this.stockRepository = stockRepository;
        this.movimientoService = movimientoService;
    }

    public List<SolicitudReposicionResponse> listarPorVeterinario(Long veterinarioId) {
        return solicitudRepository.findByVeterinarioIdWithDetalles(veterinarioId).stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    public List<SolicitudReposicionResponse> listarTodas() {
        return solicitudRepository.findAllWithDetalles().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    public SolicitudReposicionResponse obtenerPorId(@NonNull Long id) {
        SolicitudReposicion solicitud = solicitudRepository.findByIdWithVeterinario(id)
            .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada con id: " + id));
        return toResponse(solicitud);
    }

    public List<SolicitudReposicionResponse> listarPorVeterinarioId(Long veterinarioId) {
        return solicitudRepository.findByVeterinarioIdOrderByFechaCreacionDesc(veterinarioId).stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public SolicitudReposicionResponse crear(@NonNull Long veterinarioId, @NonNull SolicitudReposicionRequest request) {
        Usuario veterinario = usuarioRepository.findById(veterinarioId)
            .orElseThrow(() -> new ResourceNotFoundException("Veterinario no encontrado con id: " + veterinarioId));

        SolicitudReposicion solicitud = new SolicitudReposicion();
        solicitud.setVeterinario(veterinario);
        solicitud.setEstado(EstadoSolicitud.PENDIENTE);

        for (SolicitudReposicionRequest.DetalleSolicitudItem item : request.getDetalles()) {
            Insumo insumo = insumoRepository.findByIdAndActivoTrue(item.getInsumoId())
                .orElseThrow(() -> new ResourceNotFoundException("Insumo no encontrado con id: " + item.getInsumoId()));
            DetalleSolicitud detalle = new DetalleSolicitud(insumo, item.getCantidadSolicitada());
            solicitud.addDetalle(detalle);
        }

        solicitud = solicitudRepository.save(solicitud);
        return toResponse(solicitud);
    }

    @Transactional
    public SolicitudReposicionResponse aprobar(@NonNull Long solicitudId, @NonNull Long proveedorId) {
        SolicitudReposicion solicitud = solicitudRepository.findByIdWithDetalles(solicitudId)
            .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada con id: " + solicitudId));

        if (solicitud.getEstado() != EstadoSolicitud.PENDIENTE) {
            throw new BadRequestException("La solicitud ya no esta en estado PENDIENTE");
        }

        Proveedor proveedor = proveedorRepository.findByIdAndActivoTrue(proveedorId)
            .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con id: " + proveedorId));

        OrdenCompra orden = new OrdenCompra();
        orden.setProveedor(proveedor);
        orden.setEstado(EstadoOrdenCompra.PENDIENTE);

        for (DetalleSolicitud detalle : solicitud.getDetalles()) {
            DetalleOrdenCompra detalleOrden = new DetalleOrdenCompra(
                detalle.getInsumo(),
                detalle.getCantidadSolicitada(),
                detalle.getInsumo().getPrecioUnitario()
            );
            orden.addDetalle(detalleOrden);
        }

        orden = ordenRepository.save(orden);

        solicitud.setEstado(EstadoSolicitud.APROBADA);
        solicitud = solicitudRepository.save(solicitud);

        return toResponse(solicitud);
    }

    @Transactional
    public SolicitudReposicionResponse cancelar(@NonNull Long solicitudId) {
        SolicitudReposicion solicitud = solicitudRepository.findByIdWithVeterinario(solicitudId)
            .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada con id: " + solicitudId));

        if (solicitud.getEstado() != EstadoSolicitud.PENDIENTE) {
            throw new BadRequestException("La solicitud ya no esta en estado PENDIENTE");
        }

        solicitud.setEstado(EstadoSolicitud.CANCELADA);
        solicitud = solicitudRepository.save(solicitud);
        return toResponse(solicitud);
    }

    private SolicitudReposicionResponse toResponse(SolicitudReposicion solicitud) {
        SolicitudReposicionResponse response = new SolicitudReposicionResponse();
        response.setId(solicitud.getId());
        response.setVeterinarioId(solicitud.getVeterinario().getId());
        response.setNombreVeterinario(solicitud.getVeterinario().getNombre() + " " + solicitud.getVeterinario().getApellido());
        response.setEstado(solicitud.getEstado().name());
        response.setFechaCreacion(solicitud.getFechaCreacion());

        List<SolicitudReposicionResponse.DetalleResponse> detalles = solicitud.getDetalles().stream()
            .map(d -> new SolicitudReposicionResponse.DetalleResponse(
                d.getInsumo().getId(),
                d.getInsumo().getNombre(),
                d.getCantidadSolicitada()
            ))
            .collect(Collectors.toList());
        response.setDetalles(detalles);

        return response;
    }
}
