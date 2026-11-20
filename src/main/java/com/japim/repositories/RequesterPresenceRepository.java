package com.japim.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.japim.models.RequesterPresence;

public interface RequesterPresenceRepository extends JpaRepository<RequesterPresence, String> {
    Optional<RequesterPresence> findByUser_IdAndEvent_Id(Long user_id, Long event_id);

}
