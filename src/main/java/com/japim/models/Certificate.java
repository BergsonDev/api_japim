package com.japim.models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity 
@Table (name = "certificates")
public class Certificate  {
    
    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date_generated;

    @OneToOne(fetch = FetchType.LAZY)
    private Event event;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    public Long getId() {
        return id;
    }

    public LocalDateTime getDate_generated() {
        return date_generated;
    }

    public Event getEvent() {
        return event;
    }

    public User getUser() {
        return user;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setUser(User user) {
        this.user = user;
    }


    @PrePersist
    public void onCreate(){
        date_generated = LocalDateTime.now();
    }

    

}
