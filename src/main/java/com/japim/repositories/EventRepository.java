package com.japim.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.japim.models.Event;
import com.japim.models.User;
public interface EventRepository extends JpaRepository<Event, Long> {
    
    @Transactional 
    boolean existsByIdAndParticipants_Id(Long eventId, Long userId);

    @Query("SELECT e FROM Event e ORDER BY e.created_at DESC")
    List<Event> findAllOrderByDesc();

    @Query("SELECT DISTINCT e FROM Event e WHERE :user MEMBER OF e.participants OR :user MEMBER OF e.volunteers OR :user MEMBER OF e.speakers ORDER BY e.created_at DESC")
    List<Event> findEventsByUserInvolvement(@Param("user") User user);
    
    @Modifying
    @Transactional 
    @Query(value = "UPDATE event_participants SET deleted_at = NOW() WHERE event_id = :event_id AND participant_id = :participant_id", nativeQuery = true)
    void removeParticipantInEvent(@Param("event_id") Long eventId, @Param("participant_id") Long userId);


    @Transactional 
    boolean existsByIdAndVolunteers_Id(Long eventId, Long userId);

    @Modifying
    @Transactional 
    @Query(value = "UPDATE event_volunteers SET deleted_at = NOW() WHERE event_id = :event_id AND volunteer_id = :volunteer_id", nativeQuery = true)
    void removeVolunteerInEvent(@Param("event_id") Long eventId, @Param("volunteer_id") Long userId);
}
