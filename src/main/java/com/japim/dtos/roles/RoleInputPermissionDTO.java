package com.japim.dtos.roles;

import java.util.Set;

public record  RoleInputPermissionDTO(Long id, Set<Long> permissions_id){}