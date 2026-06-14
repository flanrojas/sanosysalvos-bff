package com.sanosysalvos.bff.dto.response;

import java.util.List;

public record RadiusResponse(
        List<PetDistanceItem> pets
) {}