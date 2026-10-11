package com.japim.dtos.permissions;

import java.time.LocalDateTime;

public record PermissionDTO(Long id, String name, String description, LocalDateTime created_at, LocalDateTime updated_at, LocalDateTime deleted_at){};