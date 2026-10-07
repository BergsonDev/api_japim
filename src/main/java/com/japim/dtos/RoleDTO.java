package com.japim.dtos;


import java.util.Date;

public record RoleDTO(Long id, String name, String initialism, String description, Date created_by, Date updated_at, Date deleted_at ){}