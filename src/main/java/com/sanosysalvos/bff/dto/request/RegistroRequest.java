package com.sanosysalvos.bff.dto.request;

public record RegistroRequest(
        String name,
        String password,
        String email,
        String address,
        String phone
) {}