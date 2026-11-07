package com.japim.dtos.events;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.japim.models.User;

public record  EventParticipantDTO (
    Long id,
    String name,
    String short_description,
    String description,
    String path_img,
    boolean is_payment,
    double value,
    int max_participants,
    LocalDate date_start,
    LocalTime hour_start,
    LocalDate date_end,
    LocalTime hour_end,
    int workload,
    List<User> speakers
){};
