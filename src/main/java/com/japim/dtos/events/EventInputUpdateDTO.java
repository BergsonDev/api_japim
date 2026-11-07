package com.japim.dtos.events;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record EventInputUpdateDTO(Long id, String name, String short_description, String description, boolean is_payment, float value, int max_participants, int max_volunteers, LocalDate date_start, LocalTime hour_start, LocalDate date_end, LocalTime hour_end, int workload, List<Long> speakers_id) {
    
}
