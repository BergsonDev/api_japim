package com.japim.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.japim.models.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
    
    @Transactional 
    boolean existsByIdAndParticipants_Id(Long eventId, Long userId);

    @Modifying // Informa ao Spring que é uma operação de escrita (DML)
    @Transactional // Garante a transação da deleção
    @Query(value = "UPDATE event_participants SET deleted_at = NOW() WHERE event_id = :event_id AND participant_id = :participant_id", nativeQuery = true)
    void removeParticipantInEvent(@Param("event_id") Long eventId, @Param("participant_id") Long userId);
}
