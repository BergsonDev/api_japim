package com.japim.models;



import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;


@Entity
@Table(name="events")
@SQLRestriction("deleted_at IS NULL")
@SQLDelete(sql = "UPDATE events SET deleted_at = NOW() WHERE id = ?")
public class Event {
    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String short_description;
    private String description;
    private String path_img;
    private boolean is_payment;
    private int value;
    private int max_participants;
    private int max_volunteers;
    private LocalDate date_start;
    private LocalTime hour_start;
    private LocalDate date_end;
    private LocalTime hour_end;
    private int workload;

    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(
        name = "event_speakers",
        joinColumns = @JoinColumn(name = "event_id"),
        inverseJoinColumns = @JoinColumn(name = "speaker_id")
    )
    private List<User> speakers;

    @OneToOne(fetch=FetchType.LAZY)
    private User created_by;

    @OneToOne(fetch=FetchType.LAZY)
    private User confirmed_by;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private LocalDateTime deleted_at;


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath_img() {
        return path_img;
    }

    public void setPath_img(String path_img) {
        this.path_img = path_img;
    }

    public boolean getIs_payment() {
        return  is_payment;
    }

    public void setIs_payment(boolean is_payment) {
        this.is_payment = is_payment;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getMax_participants() {
        return max_participants;
    }

    public void setMax_participants(int max_participants) {
        this.max_participants = max_participants;
    }

    public int getMax_volunteers() {
        return max_volunteers;
    }

    public void setMax_volunteers(int max_volunteers) {
        this.max_volunteers = max_volunteers;
    }

    public LocalDate getDate_start() {
        return date_start;
    }

    public void setDate_start(LocalDate date_start) {
        this.date_start = date_start;
    }

    public LocalTime getHour_start() {
        return hour_start;
    }

    public void setHour_start(LocalTime hour_start) {
        this.hour_start = hour_start;
    }

    public LocalDate getDate_end() {
        return date_end;
    }

    public void setDate_end(LocalDate date_end) {
        this.date_end = date_end;
    }

    public LocalTime getHour_end() {
        return hour_end;
    }

    public void setHour_end(LocalTime hour_end) {
        this.hour_end = hour_end;
    }

    public int getWorkload() {
        return workload;
    }

    public void setWorkload(int workload) {
        this.workload = workload;
    }

    public User getCreated_by() {
        return created_by;
    }

    public void setCreated_by(User created_by) {
        this.created_by = created_by;
    }

    public User getConfirmed_by() {
        return confirmed_by;
    }

    public void setConfirmed_by(User confirmed_by) {
        this.confirmed_by = confirmed_by;
    }

    public List<User> getSpeakers() {
        return speakers;
    }

    public void addSpeakers(User speaker) {
        this.speakers.add(speaker);
    }

    public void addSpeakers(List<User> speakers) {
        this.speakers.addAll(speakers);
    }

    public void removeSpeakers(List<User> speakers) {
        this.speakers.removeAll(speakers);
    }

    public void removeSpeaker(User speaker) {
        this.speakers.remove(speaker);
    }

    public void setSpeakers(List<User> speakers) {
        this.speakers = speakers;
    }

    public LocalDateTime getCreated_at() {
        return this.created_at;
    }

    public LocalDateTime getUpdated_at() {
        return this.updated_at;
    }

    public LocalDateTime getDeleted_at() {
        return this.deleted_at;
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
