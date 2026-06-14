package com.sanosysalvos.bff.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DistanceResponse(
        @JsonProperty("distance_meters") double distanceMeters
) {}