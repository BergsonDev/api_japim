package com.japim.dtos.events;

import java.util.List;

import com.japim.models.User;

public record EventVolunteerDTO(
    Long id,
    String name,
    String short_description,
    String description,
    String path_img,
    boolean is_payment,
    double value,
    int max_volunteers,
    int max_participants,
    String date_start,
    String hour_start,
    String date_end,
    String hour_end,
    int workload,
    List<User> speakers,
    List<User> volunteers
) {
    
}
