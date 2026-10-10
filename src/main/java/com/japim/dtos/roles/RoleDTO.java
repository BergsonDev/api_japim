package com.japim.dtos.roles;


import java.time.LocalDateTime;

public record RoleDTO(Long id, String name, String initialism, String description, LocalDateTime created_at, LocalDateTime updated_at, LocalDateTime deleted_at ){}