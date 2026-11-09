package com.japim.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table (name="status_events")
@SQLRestriction ("deleted_at IS NULL")
@SQLDelete (sql = "UPDATE status_events SET deleted_at = NOW() WHERE id = ?")
public class StatusEvent {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String initialism;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private LocalDateTime deleted_at;

    public StatusEvent() {
    }

    public StatusEvent(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInitialism() {
        return initialism;
    }

    public void setInitialism(String initialism) {
        this.initialism = initialism;
    }

    public void onCreate() {
        this.created_at = LocalDateTime.now();   
    }

    public void onUpdate(){
        this.updated_at = LocalDateTime.now();
    }
}
