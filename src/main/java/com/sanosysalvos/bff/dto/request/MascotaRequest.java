package com.sanosysalvos.bff.dto.request;

import java.util.UUID;

public record MascotaRequest(
        String name,
        String status,
        String species,
        String color,
        Double size,
        String foundLocation,
        String lostLocation,
        String description,
        UUID ownerId
) {}
