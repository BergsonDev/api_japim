package com.japim.dtos.users;

import java.time.LocalDateTime;
import java.util.List;

import com.japim.models.Event;
import com.japim.models.Role;

public record UserMeDTO(
    Long id, 
    String name, 
    String email, 
    String password, 
    String ra, 
    String cpf, 
    boolean is_active, 
    String path_img, 
    Role role,
    List<Event> events_participant,
    List<Event> events_volunteer,
    List<Event> events_speaker,
    LocalDateTime created_at, 
    LocalDateTime updated_at, 
    LocalDateTime deleted_at
) {
    
}
