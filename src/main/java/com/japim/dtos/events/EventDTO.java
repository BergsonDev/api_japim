package com.japim.dtos.events;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.japim.models.User;
public record EventDTO(Long id, String name, String short_description, String description, String path_img, boolean is_payment, double value, int max_participants, int max_volunteers, LocalDate date_start, LocalTime hour_start, LocalDate date_end, LocalTime hour_end, int workload, User created_by, User confirmed_by, LocalDateTime created_at, LocalDateTime updated_at, LocalDateTime deleted_at) {
    
}
