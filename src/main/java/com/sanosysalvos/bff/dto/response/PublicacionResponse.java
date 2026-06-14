package com.sanosysalvos.bff.dto.response;
import java.util.UUID;
import java.time.LocalDateTime;

public record PublicacionResponse(
        UUID idPublicacion,
        String tipoPublicacion,
        String titulo,
        String descripcion,
        LocalDateTime fechaPublicacion,
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