package com.sanosysalvos.bff.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record MascotaResponse(
        UUID id,
        String name,
        String status,
        String species,
        String color,
        Double size,
        String foundLocation,
        String lostLocation,
        String description,
        UUID ownerId,
        LocalDateTime createdAt
) {}