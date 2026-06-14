package com.sanosysalvos.bff.dto.request;
import java.util.UUID;

public record PublicacionRequest(
        String tipoPublicacion,
        String titulo,
        String descripcion,
        String fechaExtravioOEncuentro,
        String estado,
        Double latitud,
        Double longitud,
        String direccionReferencia,
        String urlFoto,
        String nombreContacto,
        String telefonoContacto,
        String emailContacto,
        UUID mascotaId,
        UUID usuarioId
) {}