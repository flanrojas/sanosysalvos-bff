package com.sanosysalvos.bff.dto.response;

public record ReporteCompletoResponse(
        String mensaje,
        MascotaResponse mascota,
        PublicacionResponse publicacion
) {}