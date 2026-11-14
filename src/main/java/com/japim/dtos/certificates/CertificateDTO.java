package com.japim.dtos.certificates;

import java.time.LocalDateTime;

import com.japim.models.Event;
import com.japim.models.User;

public record CertificateDTO(Long id, LocalDateTime date_generated, Event event, User user) {
    
}
