package com.sanosysalvos.bff.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public record PetDistanceItem(
        @JsonProperty("pet_id") UUID petId,
        @JsonProperty("distance_meters") double distanceMeters
) {}