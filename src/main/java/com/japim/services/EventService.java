package com.japim.services;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.japim.dtos.events.EventDTO;
import com.japim.dtos.events.EventInputCreateDTO;
import com.japim.dtos.events.EventInputUpdateDTO;
import com.japim.exceptions.defaults.EntityDontFindException;
import com.japim.exceptions.events.IsPaymentDontValueException;
import com.japim.models.Event;
import com.japim.repositories.EventRepository;
import com.japim.security.AuthSecurity;
@Service
public class EventService {
    private final EventRepository eventRepo;

    private EventDTO parse(Event event){
        return new EventDTO(
            event.getId(),
            event.getName(),
            event.getShort_description(),
            event.getDescription(),
            event.getPath_img(),
            event.getIs_payment(),
            event.getValue() > 0 ? (double) event.getValue() / 100 : 0,
            event.getMax_participants(),
            event.getMax_volunteers(),
            event.getDate_start(),
            event.getHour_start(),
            event.getDate_end(),
            event.getHour_end(),
            event.getWorkload(),
            event.getCreated_by(),
            event.getConfirmed_by(),
            event.getCreated_at(),
            event.getUpdated_at(),
            event.getDeleted_at()
        );
    }

    public EventService(EventRepository eventRepo) {
        this.eventRepo = eventRepo;
    }

    @Transactional(readOnly = true)
    public List<EventDTO> events(){
        return eventRepo.findAll().stream().map(this::parse).toList();
    }

    @Transactional(readOnly = true)
    public EventDTO event(Long id){
        Event event = eventRepo.findById(id).orElseThrow(()-> new EntityDontFindException(id,"events"));
        return parse(event);
    }
    
    @Transactional
    public EventDTO create(EventInputCreateDTO eventInput, Authentication auth){
        AuthSecurity authSecurity = (AuthSecurity) auth.getPrincipal();
        boolean is_confirmed = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(authName -> authName.equals("events-confirm"));

        if (eventInput.is_payment() == true && eventInput.value() <= 0) {
            throw new IsPaymentDontValueException();
        }
        
        Event event = new Event();
        event.setName(eventInput.name());
        event.setShort_description(eventInput.short_description());
        event.setDescription(eventInput.description());
        event.setIs_payment(eventInput.is_payment());
        event.setValue((int) eventInput.value() * 100);
        event.setMax_participants(eventInput.max_participants());
        event.setMax_volunteers(eventInput.max_volunteers());
        event.setDate_start(eventInput.date_start());
        event.setHour_start(eventInput.hour_start());
        event.setDate_end(eventInput.date_end());
        event.setHour_end(eventInput.hour_end());
        event.setWorkload(eventInput.workload());
        event.setCreated_by(authSecurity.getUserEntity());

        if(is_confirmed){
            event.setConfirmed_by(authSecurity.getUserEntity());
        }

        eventRepo.save(event);

        return parse(event);
    }


    @Transactional
    public EventDTO update(EventInputUpdateDTO eventInput){

        if (eventInput.is_payment() && eventInput.value() <= 0) {
            throw new IsPaymentDontValueException();
        }
        
        Event event = eventRepo.findById(eventInput.id()).orElseThrow(() -> new EntityDontFindException(eventInput.id(), "events"));
        event.setName(eventInput.name());
        event.setShort_description(eventInput.short_description());
        event.setDescription(eventInput.short_description());

        event.setIs_payment(eventInput.is_payment());
        event.setValue((int) eventInput.value() * 100);
        event.setMax_participants(eventInput.max_participants());
        event.setMax_volunteers(eventInput.max_volunteers());
        event.setDate_start(eventInput.date_start());
        event.setHour_start(eventInput.hour_start());
        event.setDate_end(eventInput.date_end());
        event.setHour_end(eventInput.hour_end());
        event.setWorkload(eventInput.workload());

        eventRepo.save(event);

        return parse(event);
    }

    @Transactional 
    public EventDTO delete(Long id){
        Event event = eventRepo.findById(id).orElseThrow(()-> new EntityDontFindException(id, "events"));

        eventRepo.delete(event);

        return parse(event);
    }
    
}
