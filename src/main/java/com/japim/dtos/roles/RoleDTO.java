package com.japim.dtos.roles;


import java.time.LocalDateTime;
import java.util.Set;

import com.japim.models.Permission;

public record RoleDTO(Long id, String name, String initialism, String description, Set<Permission> permissions, LocalDateTime created_at, LocalDateTime updated_at, LocalDateTime deleted_at ){}