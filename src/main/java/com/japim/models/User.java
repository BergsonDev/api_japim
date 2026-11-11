package com.japim.models;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.annotation.PreDestroy;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name="users")
@SQLRestriction("deleted_at IS NULL")
@SQLDelete(sql="UPDATE users SET deleted_at = NOW() WHERE id = ?")
public class User {
 
    
    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="role_id")
    private Role role;

    private String name;
    private String email;
    private String password;
    private String cpf;
    private String ra;
    private boolean is_active;
    private String path_img;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private LocalDateTime deleted_at;



    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
        name="event_participants",
        joinColumns=@JoinColumn(name = "participant_id"),
        inverseJoinColumns=@JoinColumn(name = "event_id")
    )
    private List<Event> events_participant = new ArrayList<>();

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
        name="event_volunteers",
        joinColumns=@JoinColumn(name = "volunteer_id"),
        inverseJoinColumns=@JoinColumn(name = "event_id")
    )
    private List<Event> events_volunteer = new ArrayList<>();

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
        name="event_speakers",
        joinColumns=@JoinColumn(name = "speaker_id"),
        inverseJoinColumns=@JoinColumn(name = "event_id")
    )
    private List<Event> events_speaker = new ArrayList<Event>();


    public long getId() {
        return id;
    }


    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }    

    public void setName(String name) {
        this.name = name;
    }


    public boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPath_img() {
        return path_img;
    }

    public void setPath_img(String path_img) {
        this.path_img = path_img;
    }

    public String getRa() {
        return ra;
    }
    
    public void setRa(String ra) {
        this.ra = ra;
    }
    
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }


    // control of creations
    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public LocalDateTime getDeleted_at() {
        return deleted_at;
    }


    public List<Event> getEvents_participant(){
        return events_participant;
    }

    public List<Event> getEvents_speaker(){
        return events_speaker;
    }

    public List<Event> getEvents_volunteer(){
        return events_volunteer;
    }

    // lifecicle
    @PrePersist
    protected void onCreate(){
        this.created_at = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        this.updated_at = LocalDateTime.now();
    }

    @PreDestroy
    protected  void onDelete(){
        this.deleted_at = LocalDateTime.now();
    }
}
