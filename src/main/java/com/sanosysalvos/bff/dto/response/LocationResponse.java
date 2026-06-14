package com.sanosysalvos.bff.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public record LocationResponse(
        @JsonProperty("pet_id") UUID petId,
        double latitude,
        double longitude
) {}