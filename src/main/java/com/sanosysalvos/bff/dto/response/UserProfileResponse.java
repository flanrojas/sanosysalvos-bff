package com.sanosysalvos.bff.dto.response;

public record UserProfileResponse(
        String name,
        String email,
        String address,
        String phone,
        String rol
) {}