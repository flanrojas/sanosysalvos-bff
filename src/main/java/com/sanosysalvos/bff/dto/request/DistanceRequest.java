package com.sanosysalvos.bff.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DistanceRequest(
        @JsonProperty("origin_latitude") double originLatitude,
        @JsonProperty("origin_longitude") double originLongitude,
        @JsonProperty("target_latitude") double targetLatitude,
        @JsonProperty("target_longitude") double targetLongitude
) {}