package com.japim.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.annotation.PreDestroy;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;


@Entity
@Table(name="event_presences")
@SQLRestriction("deleted_at IS NULL")
@SQLDelete(sql = "UPDATE events SET deleted_at = NOW() WHERE id = ?")
public class EventPresence {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch=FetchType.LAZY)
    private Event event;

    @OneToOne(fetch=FetchType.LAZY)
    private User participant;

    @OneToOne(fetch=FetchType.LAZY)
    private User confirmed_by;

    private LocalDateTime date_presence;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private LocalDateTime deleted_at;

    public Long getId() {
        return id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setParticipant(User participant) {
        this.participant = participant;
    }

    public User getParticipant() {
        return participant;
    }


    public User getConfirmed_by() {
        return confirmed_by;
    }

    public void setConfirmed_by(User confirmed_by) {
        this.confirmed_by = confirmed_by;
    }

    public void setDate_presence(LocalDateTime date_presence) {
        this.date_presence = date_presence;
    }

    public LocalDateTime getDate_presence() {
        return date_presence;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public LocalDateTime getDeleted_at() {
        return deleted_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
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