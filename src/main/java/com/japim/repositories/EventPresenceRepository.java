package com.japim.repositories;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.japim.models.EventPresence;

public interface EventPresenceRepository extends JpaRepository<EventPresence, Long>{

    
    @Query(value = """
            SELECT *
            FROM event_presences
            WHERE DATE(date_presence) = :date_presence AND event_id = :event_id AND deleted_at IS NULL
        """, nativeQuery = true)
    Optional<EventPresence> findByDatePresence(@Param("date_presence") LocalDate date_presence, @Param("event_id") Long event_id);

    List<EventPresence> findByParticipant_Id(Long participant_id);

    List<EventPresence> findByParticipant_IdAndEvent_Id(Long participant_id,Long event_id );

    @Query(value = """
            SELECT *
            FROM event_presences
            WHERE DATE(date_presence) = DATE(:date_presence) AND event_id = :event_id AND participant_id = :participant_id AND deleted_at IS NULL
        """, nativeQuery = true)
    Optional<EventPresence> findByDatePresenceEvendIdAndParticipantId(@Param("date_presence") LocalDateTime date_presence, @Param("event_id") Long event_id, @Param("participant_id") Long participant_id);

    boolean existsByParticipant_Id(Long participant_id);

    boolean existsByEvent_Id(Long event_id);


    long countByParticipant_IdAndEvent_Id(Long participant_id,Long event_id);
}