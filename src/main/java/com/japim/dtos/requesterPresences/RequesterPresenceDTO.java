package com.japim.dtos.requesterPresences;

import java.time.LocalDateTime;
import com.japim.models.Event;
import com.japim.models.User;

public record RequesterPresenceDTO(String code, LocalDateTime date, User user, Event event) {
    
}
