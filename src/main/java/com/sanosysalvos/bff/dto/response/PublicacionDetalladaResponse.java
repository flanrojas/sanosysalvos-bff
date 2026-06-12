package com.sanosysalvos.bff.dto.response;

public record PublicacionDetalladaResponse(
        PublicacionResponse publicacion,
        MascotaResponse mascota
) {}