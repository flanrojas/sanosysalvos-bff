package com.sanosysalvos.bff.service;

import com.sanosysalvos.bff.client.MascotasClient;
import com.sanosysalvos.bff.client.PublicacionClient;
import com.sanosysalvos.bff.dto.ReporteCompletoDTO;
import com.sanosysalvos.bff.dto.request.MascotaRequest;
import com.sanosysalvos.bff.dto.request.PublicacionRequest;
import com.sanosysalvos.bff.dto.request.ReporteCompletoRequest;
import com.sanosysalvos.bff.dto.response.MascotaResponse;
import com.sanosysalvos.bff.dto.response.PublicacionDetalladaResponse;
import com.sanosysalvos.bff.dto.response.PublicacionResponse;
import com.sanosysalvos.bff.dto.response.ReporteCompletoResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrquestadorService {

    private final MascotasClient mascotasClient;
    private final PublicacionClient publicacionClient;

    public OrquestadorService(MascotasClient mascotasClient, PublicacionClient publicacionClient) {
        this.mascotasClient = mascotasClient;
        this.publicacionClient = publicacionClient;
    }

    public PublicacionDetalladaResponse getPublicacionDetallada(UUID idPublicacion) {
        PublicacionResponse pub = publicacionClient.getById(idPublicacion);
        MascotaResponse mascota = mascotasClient.getById(pub.mascotaId());
        return new PublicacionDetalladaResponse(pub, mascota);
    }

    public ReporteCompletoResponse crearReporteCompleto(ReporteCompletoRequest dto) {
        MascotaRequest mascotaReq = new MascotaRequest(
                dto.nombre(),
                "perdido".equalsIgnoreCase(dto.estado()) ? "LOST" : "FOUND",
                dto.tipo(),
                dto.color(),
                dto.tamaño() != null ? dto.tamaño() : null,
                null, null, null,
                UUID.fromString(dto.usuarioId())
        );
        MascotaResponse mascotaRes = mascotasClient.create(mascotaReq);

        PublicacionRequest pubReq = new PublicacionRequest(
                "perdido".equalsIgnoreCase(dto.estado()) ? "PERDIDA" : "ENCONTRADA",
                dto.titulo(),
                dto.descripcion(),
                dto.fecha(),
                "ACTIVA",
                dto.latitud() != null ? dto.latitud() : -33.68,
                dto.longitud() != null ? dto.longitud() : -71.21,
                dto.ubicacion(),
                null,
                dto.nombreContacto(),
                dto.telefonoContacto(),
                null,
                mascotaRes.id(),
                UUID.fromString(dto.usuarioId())
        );
        PublicacionResponse pubRes = publicacionClient.create(pubReq);

        return new ReporteCompletoResponse("Reporte creado y orquestado con éxito", mascotaRes, pubRes);
    }
}