package com.japim.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.japim.models.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
    
}
