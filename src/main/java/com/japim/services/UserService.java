package com.japim.services;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.japim.dtos.events.EventDTO;
import com.japim.dtos.users.UserDTO;
import com.japim.dtos.users.UserInputDTO;
import com.japim.dtos.users.UserMeDTO;
import com.japim.exceptions.defaults.EntityDontFindException;
import com.japim.models.Event;
import com.japim.models.Role;
import com.japim.models.User;
import com.japim.repositories.EventRepository;
import com.japim.repositories.RoleRepository;
import com.japim.repositories.UserRepository;
import com.japim.security.AuthSecurity;


@Service 
public class UserService {

    private UserRepository userRepo;
    private RoleRepository roleRepo;
    private PasswordEncoder passwordEncoder;
    private EventRepository eventRepository;

     private EventDTO parseEvent(Event event, Authentication auth){
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

    private UserDTO parse(User user){
        return new UserDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPassword(),
            user.getRa(),
            user.getCpf(),
            user.getIs_active(),
            user.getPath_img(),
            user.getRole(),
            user.getCreated_at(),
            user.getUpdated_at(),
            user.getDeleted_at()
        );

    }

    private UserMeDTO parseAuth(User user){
        return new UserMeDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPassword(),
            user.getRa(),
            user.getCpf(),
            user.getIs_active(),
            user.getPath_img(),
            user.getRole(),
            user.getEvents_participant(),
            user.getEvents_volunteer(),
            user.getEvents_speaker(),
            user.getCreated_at(),
            user.getUpdated_at(),
            user.getDeleted_at()
        );
    }

    public UserService(UserRepository userRepo, RoleRepository roleRepo, PasswordEncoder passwordEncoder,EventRepository eventRepository){
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
        this.eventRepository = eventRepository;
    }

    @Transactional(readOnly=true)
    public List<UserDTO> findAll(){
        return userRepo.findAll().stream().map(this::parse).toList();
    }

    @Transactional(readOnly=true)
    public UserDTO findByID(Long id){
        User user = this.userRepo.findById(id).orElseThrow(()->new EntityDontFindException(id, "users"));
        return this.parse(user);
    }

    public List<UserDTO> findAllById(Long[] ids){
        return this.userRepo.findAllById(List.of(ids)).stream().map(this::parse).toList();
    }


    @Transactional
    public UserDTO create(UserInputDTO userInput){
        User user = new User();
        Role role = this.roleRepo.findByInitialism("USER").orElseThrow(()->new EntityDontFindException(null, "roles"));
        
        user.setName(userInput.name());
        user.setEmail(userInput.email());
        user.setPassword(passwordEncoder.encode(userInput.password()));
        user.setCpf(userInput.cpf());
        user.setRa(userInput.ra());
        user.setPath_img(userInput.path_img());
        user.setRole(role);
        user.setIs_active(true);
        
        this.userRepo.save(user);
        return this.parse(user);
    }
    
    @Transactional
    public UserDTO update(UserInputDTO userInput){
        User user = this.userRepo.findById(userInput.id()).orElseThrow(()->new EntityDontFindException(userInput.id(), "users"));
        Role role = this.roleRepo.findByInitialism("users").orElseThrow(()->new EntityDontFindException(null, "roles"));
        
        user.setName(userInput.name());
        user.setEmail(userInput.email());
        user.setCpf(userInput.cpf());
        user.setRa(userInput.ra());
        user.setPath_img(userInput.path_img());
        user.setRole(role);
        user.setIs_active(true);
        
        this.userRepo.save(user);
        return this.parse(user);
    }
    
    @Transactional 
    public UserDTO delete(long id){
        User user = this.userRepo.findById(id).orElseThrow(()->new EntityDontFindException(id, "users"));
        this.userRepo.delete(user);
        return this.parse(user);
    }


    @Transactional 
    public UserDTO desative(long id){
        User user = this.userRepo.findById(id).orElseThrow(()->new EntityDontFindException(id, "users"));
        user.setIs_active(false);
        this.userRepo.save(user);
        return this.parse(user);
    }

    @Transactional(readOnly=true) 
    public UserMeDTO getAuth(Authentication auth){
        AuthSecurity authSecurity = (AuthSecurity) auth.getPrincipal();
        return parseAuth(authSecurity.getUserEntity());
    }

    @Transactional 
    public List<EventDTO> eventsSubscribe(Authentication auth){
        AuthSecurity authSecurity = (AuthSecurity) auth.getPrincipal();
        User user = authSecurity.getUserEntity();

        return eventRepository.findEventsByUserInvolvement(user).stream().map(event->parseEvent(event,auth)).toList();
    }
}
