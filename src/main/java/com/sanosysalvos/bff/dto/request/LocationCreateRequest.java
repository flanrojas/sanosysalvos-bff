package com.sanosysalvos.bff.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public record LocationCreateRequest(
        @JsonProperty("pet_id") UUID petId,
        double latitude,
        double longitude
) {}