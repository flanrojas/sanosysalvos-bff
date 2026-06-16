package com.sanosysalvos.bff.dto.request;

public record PasswordChangeRequest(
        String currentPassword,
        String newPassword
) {}