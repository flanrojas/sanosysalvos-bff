package com.sanosysalvos.bff.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RadiusRequest(
        double latitude,
        double longitude,
        @JsonProperty("radius_meters") double radiusMeters
) {}