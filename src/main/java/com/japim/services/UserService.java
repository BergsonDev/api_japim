package com.japim.services;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.japim.dtos.users.UserDTO;
import com.japim.dtos.users.UserInputDTO;
import com.japim.dtos.users.UserMeDTO;
import com.japim.exceptions.defaults.EntityDontFindException;
import com.japim.models.Role;
import com.japim.models.User;
import com.japim.repositories.RoleRepository;
import com.japim.repositories.UserRepository;
import com.japim.security.AuthSecurity;


@Service 
public class UserService {

    private UserRepository userRepo;
    private RoleRepository roleRepo;
    private PasswordEncoder passwordEncoder;

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

    public UserService(UserRepository userRepo, RoleRepository roleRepo, PasswordEncoder passwordEncoder){
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
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
        Role role = this.roleRepo.findByInitialism("users").orElseThrow(()->new EntityDontFindException(null, "roles"));
        
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
}
