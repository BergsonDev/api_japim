package com.japim.services;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.japim.dtos.events.EventDTO;
import com.japim.dtos.events.EventInputCreateDTO;
import com.japim.dtos.events.EventInputUpdateDTO;
import com.japim.dtos.events.EventParticipantDTO;
import com.japim.exceptions.FunctionalityIsCommingException;
import com.japim.exceptions.defaults.EntityDontFindException;
import com.japim.exceptions.events.IsPaymentDontValueException;
import com.japim.exceptions.events.MaximizeParticipantsException;
import com.japim.exceptions.events.NullableSpeakersInputException;
import com.japim.models.Event;
import com.japim.models.User;
import com.japim.repositories.EventRepository;
import com.japim.repositories.UserRepository;
import com.japim.security.AuthSecurity;

@Service
public class EventService {
    private final EventRepository eventRepo;
    private final UserRepository userRepo;

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
            event.getSpeakers(),
            event.getParticipants(),
            event.getCreated_at(),
            event.getUpdated_at(),
            event.getDeleted_at()
        );
    }


    private EventParticipantDTO parseEventParticipant(Event event){
        return new EventParticipantDTO(
            event.getId(),
            event.getName(),
            event.getShort_description(),
            event.getDescription(),
            event.getPath_img(),
            event.getIs_payment(),
            event.getValue() > 0 ? (double) event.getValue() / 100 : 0,
            event.getMax_participants(),
            event.getDate_start(),
            event.getHour_start(),
            event.getDate_end(),
            event.getHour_end(),
            event.getWorkload(),
            event.getSpeakers()
        );

    } 

    public EventService(EventRepository eventRepo, UserRepository userRepo){
        this.userRepo = userRepo;   
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

        if (eventInput.speakers_id().size() <= 0 ) {
            throw new NullableSpeakersInputException();
        }

        if (eventInput.is_payment() == true && eventInput.value() <= 0) {
            throw new IsPaymentDontValueException();
        }
        
        List<User> speakers = userRepo.findAllById(eventInput.speakers_id()).stream().toList();

        if(speakers.size() != eventInput.speakers_id().size()){
            throw new EntityDontFindException(null, "users");
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
        event.setSpeakers(speakers);

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

        if(eventInput.speakers_id().size() <= 0){
            throw new NullableSpeakersInputException();
        }
        
        List<User> speakers = userRepo.findAllById(eventInput.speakers_id()).stream().toList();

        if(speakers.size() != eventInput.speakers_id().size()){
            throw new EntityDontFindException(null, "users");
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
        event.setSpeakers(speakers);

        eventRepo.save(event);

        return parse(event);
    }

    @Transactional 
    public EventDTO delete(Long id){
        Event event = eventRepo.findById(id).orElseThrow(()-> new EntityDontFindException(id, "events"));

        eventRepo.delete(event);

        return parse(event);
    }

    @Transactional 
    public EventParticipantDTO subscribeParticipant(Long event_id, Authentication auth){
        AuthSecurity authSecurity = (AuthSecurity) auth.getPrincipal();
        User user = authSecurity.getUserEntity();

        Event event = eventRepo.findById(event_id).orElseThrow(() -> new EntityDontFindException(event_id, "events"));

        if(event.getIs_payment()){
            throw new FunctionalityIsCommingException("Funcionalidade de Pagamento em evolução");
        }

        if(event.getParticipants().size() >= event.getMax_participants()){
            throw new MaximizeParticipantsException();
        }

        if(eventRepo.existsByIdAndParticipants_Id(event.getId(), user.getId())){
            return parseEventParticipant(event);
        }

        event.addParticipant(user);
        eventRepo.save(event);

        return parseEventParticipant(event);
    }

    @Transactional 
    public EventParticipantDTO unsubscribeParticipant(Long event_id, Authentication auth){
        AuthSecurity authSecurity = (AuthSecurity) auth.getPrincipal();
        User user = authSecurity.getUserEntity();  

        Event event = eventRepo.findById(event_id).orElseThrow(() -> new EntityDontFindException(event_id, "events"));
        
        if(eventRepo.existsByIdAndParticipants_Id(event.getId(), user.getId())){
            eventRepo.removeParticipantInEvent(event.getId(), user.getId());
            eventRepo.save(event);
        }
        
        return parseEventParticipant(event);
    
    }
    
}
