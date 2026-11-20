package com.japim.services;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.japim.dtos.eventPresence.EventPresenceDTO;
import com.japim.dtos.events.EventDTO;
import com.japim.dtos.events.EventInputCreateDTO;
import com.japim.dtos.events.EventInputUpdateDTO;
import com.japim.dtos.events.EventParticipantDTO;
import com.japim.dtos.events.EventVolunteerDTO;
import com.japim.exceptions.FunctionalityIsCommingException;
import com.japim.exceptions.defaults.EntityDontFindException;
import com.japim.exceptions.eventPresence.PresenceConfirmatedInEventException;
import com.japim.exceptions.eventPresence.TimeDontValueForPresenceException;
import com.japim.exceptions.events.DontDeleteEventException;
import com.japim.exceptions.events.DontFindUserInEventException;
import com.japim.exceptions.events.DontUpdateEventException;
import com.japim.exceptions.events.EventDontModifyStatusException;
import com.japim.exceptions.events.IsPaymentDontValueException;
import com.japim.exceptions.events.MaximizeParticipantsException;
import com.japim.exceptions.events.NullableSpeakersInputException;
import com.japim.models.Event;
import com.japim.models.EventPresence;
import com.japim.models.User;
import com.japim.repositories.EventPresenceRepository;
import com.japim.repositories.EventRepository;
import com.japim.repositories.StatusEventRepository;
import com.japim.repositories.UserRepository;
import com.japim.security.AuthSecurity;

@Service
public class EventService {
    private final EventRepository eventRepo;
    private final UserRepository userRepo;
    private final StatusEventRepository statusRep;
    private final EventPresenceRepository eventPresenceRepo;

    private final String IN_PROGRESS = "EAM";
    private final String CANCELED = "CAN";
    private final String CLOSED = "ENC";
    private final String APROVATED = "APR";
    private final String CREATED = "CRI";
    
    private final int DAY_DONT_PERMISSION = 3;

    private EventDTO parse(Event event, Authentication auth){
        AuthSecurity authSecurity = (AuthSecurity) auth.getPrincipal();
        User user = authSecurity.getUserEntity();
        boolean in_event = false;
    
        if(event.getParticipants() != null && event.getParticipants().size() > 0){
            in_event = event.getParticipants().stream().filter(pa->pa.getId()==user.getId()).toList().size() > 0;
        }
        if(!in_event && event.getVolunteers() != null  && event.getVolunteers().size() > 0){
            in_event = event.getVolunteers().stream().filter(vo->vo.getId()==user.getId()).toList().size() > 0;
        }

        if(!in_event && event.getSpeakers() != null && event.getSpeakers().size() > 0){
            in_event = event.getSpeakers().stream().filter(sp->sp.getId()==user.getId()).toList().size() > 0;

        }


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
            event.getStatusEvent(),
            event.getConfirmed_by(),
            event.getSpeakers(),
            event.getSpeakers() == null ? 0 : event.getSpeakers().size(),
            event.getParticipants(),
            event.getParticipants() == null ? 0 : event.getParticipants().size(),
            event.getVolunteers(),
            event.getVolunteers() == null ? 0 : event.getVolunteers().size(),
            in_event,
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


    private EventVolunteerDTO parseEventVolunteer(Event event){
        return new EventVolunteerDTO(
            event.getId(),
            event.getName(),
            event.getShort_description(),
            event.getDescription(),
            event.getPath_img(),
            event.getIs_payment(),
            event.getValue() > 0 ? (double) event.getValue() / 100 : 0,
            event.getMax_volunteers(),
            event.getMax_participants(),
            event.getDate_start().toString(),
            event.getHour_start().toString(),
            event.getDate_end().toString(),
            event.getHour_end().toString(),
            event.getWorkload(),
            event.getSpeakers(),
            event.getVolunteers()
        );
    }

    private EventPresenceDTO parsePresence(EventPresence eventPresence){
        return new EventPresenceDTO(
            eventPresence.getId(),
            eventPresence.getEvent(),
            eventPresence.getParticipant(), 
            eventPresence.getConfirmed_by(), 
            eventPresence.getDate_presence(), 
            eventPresence.getCreated_at(),
            eventPresence.getUpdated_at(),
            eventPresence.getDeleted_at()
        );
    }

    private boolean confirmPresenceInEvent(Event event, ZonedDateTime date_now){

        if (date_now.getYear() != event.getDate_start().getYear() || date_now.getYear() != event.getDate_end().getYear()) {
            throw new TimeDontValueForPresenceException("Anos diferentes do evento enviado");   
        }

        if(date_now.getMonth().getValue() < event.getDate_start().getMonth().getValue() || date_now.getMonth().getValue() > event.getDate_end().getMonth().getValue()){
            throw new TimeDontValueForPresenceException("Mês da confirmação de presenção não está no intervalo dos dias do evento");
        }

        if(date_now.getDayOfMonth() < event.getDate_start().getDayOfMonth() || date_now.getDayOfMonth() > event.getDate_end().getDayOfMonth() ){
            throw new TimeDontValueForPresenceException("Meses diferentes do evento para checar a presença");   
        }

        if(date_now.getHour() < event.getHour_start().getHour() || date_now.getHour() > event.getHour_end().getHour()){
            throw new TimeDontValueForPresenceException("Hora não permitida para fazer a chamada do evento");
        }

        if((date_now.getHour() == event.getHour_start().getHour() && date_now.getMinute() < event.getHour_start().getMinute()) || (date_now.getHour() == event.getHour_end().getHour() && date_now.getMinute() > event.getHour_end().getMinute())){
            throw new TimeDontValueForPresenceException("Minutos não permitido para fazer a chamada do evento");
        }

        return true;

    }

    private boolean permissionForModifyEvent(Event event, ZonedDateTime date_now){

        if(event.getStatusEvent().getInitialism().compareTo(CREATED) != 0 || event.getStatusEvent().getInitialism().compareTo(APROVATED) != 0) return false;

        if(date_now.getMonth().getValue() > event.getDate_start().getMonth().getValue()) return false;
        
        if(date_now.getDayOfMonth()  >= event.getDate_start().getDayOfMonth() - DAY_DONT_PERMISSION) return false;   

        return true;

    }

    public EventService(EventRepository eventRepo, UserRepository userRepo, StatusEventRepository statusRepo, EventPresenceRepository eventPresenceRepo){
        this.userRepo = userRepo;   
        this.eventRepo = eventRepo;
        this.statusRep = statusRepo;
        this.eventPresenceRepo = eventPresenceRepo;
    }

    @Transactional(readOnly = true)
    public List<EventDTO> events(Authentication auth){
        return eventRepo.findAll().stream().map(event->parse(event, auth)).toList();
    }

    @Transactional(readOnly = true)
    public EventDTO event(Long id, Authentication auth){
        Event event = eventRepo.findById(id).orElseThrow(()-> new EntityDontFindException(id,"events"));
        return parse(event, auth);
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
            event.setStatusEvent(statusRep.findByInitialism("APR").orElseThrow(()->new EntityDontFindException(null, "status events")));
        }else{
            event.setStatusEvent(statusRep.findByInitialism("CRI").orElseThrow(()->new EntityDontFindException(null, "status events")));
        }

        eventRepo.save(event);

        return parse(event,auth);
    }


    @Transactional
    public EventDTO update(EventInputUpdateDTO eventInput, Authentication auth){
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

        if(!permissionForModifyEvent(event, ZonedDateTime.now(ZoneId.of("America/Manaus")))){
            throw new DontUpdateEventException(null);
        }

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

        return parse(event,auth);
    }

    @Transactional 
    public EventDTO delete(Long id, Authentication auth){
        Event event = eventRepo.findById(id).orElseThrow(()-> new EntityDontFindException(id, "events")); 
        
        if(permissionForModifyEvent(event, ZonedDateTime.now(ZoneId.of("America/Manaus")))){
            throw new DontDeleteEventException(null);
        }

        if(eventPresenceRepo.existsByEvent_Id(event.getId())){
            throw new DontDeleteEventException("Evento já tem a presenças registrados, não pode ser deletado");   
        }
        eventRepo.delete(event);

        return parse(event,auth);
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
        
        if(eventPresenceRepo.existsByParticipant_Id(user.getId())){
            throw new PresenceConfirmatedInEventException();
        }

        if(eventRepo.existsByIdAndParticipants_Id(event.getId(), user.getId())){
            eventRepo.removeParticipantInEvent(event.getId(), user.getId());
            eventRepo.save(event);
        }
        
        return parseEventParticipant(event);
    
    }
    


    @Transactional 
    public EventVolunteerDTO subscribeVolunteer(Long event_id, Authentication auth){

        AuthSecurity authSecurity = (AuthSecurity) auth.getPrincipal();
        User user = authSecurity.getUserEntity();

        Event event = eventRepo.findById(event_id).orElseThrow(() -> new EntityDontFindException(event_id, "events"));

        if(event.getVolunteers().size() >= event.getMax_volunteers()){
            throw new MaximizeParticipantsException();
        }

        if(eventRepo.existsByIdAndVolunteers_Id(event.getId(), user.getId())){
            return parseEventVolunteer(event);
        }

        event.addVolunteer(user);
        eventRepo.save(event);

        return parseEventVolunteer(event);
    }

    @Transactional
    public EventVolunteerDTO unsubscribeVolunteer(Long event_id, Authentication auth){
        AuthSecurity authSecurity = (AuthSecurity) auth.getPrincipal();
        User user = authSecurity.getUserEntity();  

        Event event = eventRepo.findById(event_id).orElseThrow(() -> new EntityDontFindException(event_id, "events"));
        if(eventPresenceRepo.existsByParticipant_Id(user.getId())){
            throw new PresenceConfirmatedInEventException();
        }

        if(eventRepo.existsByIdAndVolunteers_Id(event.getId(), user.getId())){
            eventRepo.removeVolunteerInEvent(event.getId(), user.getId());
            eventRepo.save(event);
        }
        
        return parseEventVolunteer(event);
    }

    @Transactional
    public EventDTO confirmedEvent(Long id, Authentication auth){
        AuthSecurity authSecurity = (AuthSecurity) auth.getPrincipal();
        User user = authSecurity.getUserEntity();
        Event event = eventRepo.findById(id).orElseThrow(() -> new EntityDontFindException(id, "events"));
        event.setStatusEvent(statusRep.findByInitialism("APR").orElseThrow(()->new EntityDontFindException(null, "status events")));
        event.setConfirmed_by(user);
        eventRepo.save(event);
        return parse(event,auth);
    }


    @Transactional
    public EventDTO confirmedPresenceByVolunteer(Long id, Long participant_id, Authentication auth){
        ZonedDateTime date_now = ZonedDateTime.now(ZoneId.of("America/Manaus"));
        AuthSecurity authSecurity = (AuthSecurity) auth.getPrincipal();
        User user = authSecurity.getUserEntity();

        if (!eventRepo.existsByIdAndParticipants_Id(id, participant_id)) {
            throw new DontFindUserInEventException();
        }

        User participant = userRepo.findById(participant_id).orElseThrow(()-> new EntityDontFindException(null,"users"));
        Event event = eventRepo.findById(id).orElseThrow(()-> new EntityDontFindException(null,"events"));

        EventPresence eventPresence = eventPresenceRepo.findByDatePresence(date_now.toLocalDateTime().toLocalDate(), event.getId()).orElse(null);

        if(eventPresence != null){
            throw new PresenceConfirmatedInEventException();
        }

        if(!this.confirmPresenceInEvent(event, date_now)) return null;

        eventPresence = new EventPresence();
        eventPresence.setConfirmed_by(user);
        eventPresence.setEvent(event);
        eventPresence.setParticipant(participant);
        eventPresence.setDate_presence(date_now.toLocalDateTime());

        eventPresenceRepo.save(eventPresence);
        return parse(event,auth);
    }

    @Transactional 
    public EventPresenceDTO participantPresenceDay(Long id, Long participant_id){
        if (!eventRepo.existsByIdAndParticipants_Id(id, participant_id)) {
            throw new DontFindUserInEventException();
        }

        return parsePresence(eventPresenceRepo.findByParticipant_IdAndEvent_Id(participant_id,id).orElse(null));

    }

    @Transactional 
    public EventDTO confirmedPresenceByCoordinator(Long id, Long volunteer_id, Authentication auth){
        ZonedDateTime date_now = ZonedDateTime.now(ZoneId.of("America/Manaus"));
        AuthSecurity authSecurity = (AuthSecurity) auth.getPrincipal();
        User user = authSecurity.getUserEntity();

        if (!eventRepo.existsByIdAndVolunteers_Id(id, volunteer_id)) {
            throw new DontFindUserInEventException();
        }

        User participant = userRepo.findById(volunteer_id).orElseThrow(()-> new EntityDontFindException(null,"users"));
        Event event = eventRepo.findById(id).orElseThrow(()-> new EntityDontFindException(null,"events"));

        EventPresence eventPresence = eventPresenceRepo.findByDatePresence(date_now.toLocalDateTime().toLocalDate(),event.getId()).orElse(null);

        if(eventPresence != null){
            throw new PresenceConfirmatedInEventException();
        }

        if(!this.confirmPresenceInEvent(event, date_now)) return null;

        eventPresence = new EventPresence();
        eventPresence.setConfirmed_by(user);
        eventPresence.setEvent(event);
        eventPresence.setParticipant(participant);
        eventPresence.setDate_presence(date_now.toLocalDateTime());

        eventPresenceRepo.save(eventPresence);
        return parse(event,auth);
    }


    public EventDTO finishEvent(Long id, Authentication auth){
        AuthSecurity authSecurity = (AuthSecurity) auth.getPrincipal();
        User user = authSecurity.getUserEntity();
        Event event = eventRepo.findById(id).orElseThrow(()-> new EntityDontFindException(null,"events"));

        if (event.getStatusEvent().getInitialism().compareTo(CANCELED) == 0) {
            throw new EventDontModifyStatusException("Não pode mudar o status para encerrado, pois o evento foi cancelado");
        }

        event.setConfirmed_by(user);
        event.setStatusEvent(statusRep.findByInitialism(CLOSED).orElseThrow(()->new EntityDontFindException(null, "events")));
        eventRepo.save(event);
        return parse(event,auth);
    }

    public EventDTO canceledEvent(Long id, Authentication auth){
        AuthSecurity authSecurity = (AuthSecurity) auth.getPrincipal();
        User user = authSecurity.getUserEntity();
        Event event = eventRepo.findById(id).orElseThrow(()-> new EntityDontFindException(null,"events"));
        if (event.getStatusEvent().getInitialism().compareTo(CLOSED) == 0) {
            throw new EventDontModifyStatusException("Não pode mudar o status para cancelado, pois o evento foi encerrado");
        }
        event.setConfirmed_by(user);
        event.setStatusEvent(statusRep.findByInitialism(CANCELED).orElseThrow(()->new EntityDontFindException(null, "events")));
        eventRepo.save(event);
        return parse(event,auth);
    }

}
