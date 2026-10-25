package com.japim.dtos.users;

import java.time.LocalDateTime;

import com.japim.models.Role;

public record UserDTO(long id, String name, String email, String password, String ra, String cpf, boolean is_active, String path_img, Role role, LocalDateTime created_at, LocalDateTime updated_at, LocalDateTime deleted_at){
}
