package com.japim.dtos.eventPresence;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.japim.models.Event;
import com.japim.models.User;

public record EventPresenceDTO(
    Long id, 
    Event event, 
    User participant, 
    User confirmed_by, 
    LocalDateTime date_presence, 
    LocalDateTime created_at, 
    LocalDateTime updated_at, 
    LocalDateTime deleted_at
){}