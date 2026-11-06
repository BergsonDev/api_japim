package com.japim.controllers;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import com.japim.dtos.events.EventDTO;
import com.japim.dtos.events.EventInputCreateDTO;
import com.japim.dtos.events.EventInputUpdateDTO;
import com.japim.dtos.events.EventParticipantDTO;
import com.japim.services.EventService;


@Controller 
public class EventController {

    private final EventService eventService;


    public EventController(EventService eventService){
        this.eventService = eventService;
    }


    @QueryMapping
    @PreAuthorize("hasAuthority('events-read')")
    public List<EventDTO> events(){
        return eventService.events();
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('events-read')")
    public EventDTO event(@Argument Long id){
        return eventService.event(id);
    }

    @MutationMapping
    @PreAuthorize("hasAuthority('events-create')") 
    public EventDTO createEvent(@Argument("input") EventInputCreateDTO eventInput, Authentication auth){
        return eventService.create(eventInput, auth);
    }

    @MutationMapping
    @PreAuthorize("hasAuthority('events-update')") 
    public EventDTO updateEvent(@Argument("input") EventInputUpdateDTO eventInput){
        return eventService.update(eventInput);
    }



    @MutationMapping
    @PreAuthorize("hasAuthority('events-delete')") 
    public EventDTO deleteEvent(@Argument long id){
        return eventService.delete(id);
    }


    @PreAuthorize ("hasAuthority('events-subscribe-participant')")
    @MutationMapping
    public EventParticipantDTO subscribeParticipant(@Argument Long event_id, Authentication auth){
        return eventService.subscribeParticipant(event_id, auth);  
    }

    @PreAuthorize ("hasAuthority('events-unsubscribe-participant')")
    @MutationMapping
    public EventParticipantDTO unsubscribeParticipant(@Argument Long event_id, Authentication auth){
        return eventService.unsubscribeParticipant(event_id, auth);
    }

}
