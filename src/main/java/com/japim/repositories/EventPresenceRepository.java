package com.japim.repositories;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.japim.models.EventPresence;

public interface EventPresenceRepository extends JpaRepository<EventPresence, Long>{

    @Query(value = "SELECT * FROM event_presences where date_presence = :date_presence",nativeQuery = true)
    Optional<EventPresence> findByDatePresence(@Param("date_presence") LocalDateTime date_presence);

}