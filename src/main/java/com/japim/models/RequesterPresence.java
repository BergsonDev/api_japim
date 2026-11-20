package com.japim.models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity 
@Table(name="requester_presences")
public class RequesterPresence {
    
    @Id 
    private String code;

    private LocalDateTime date;

    @OneToOne 
    private User user;

    @OneToOne 
    private Event event;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @PrePersist 
    public void onCreate(){
        date = LocalDateTime.now();
    }
}
