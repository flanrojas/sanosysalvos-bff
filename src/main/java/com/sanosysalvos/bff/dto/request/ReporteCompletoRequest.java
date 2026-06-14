package com.sanosysalvos.bff.dto.request;

public record ReporteCompletoRequest(
        String titulo,
        String nombre,
        String tipo,
        String color,
        Double tamaño,
        String estado,
        String ubicacion,
        String fecha,
        String descripcion,
        String nombreContacto,
        String telefonoContacto,
        String usuarioId,
        Double latitud,
        Double longitud
) {}