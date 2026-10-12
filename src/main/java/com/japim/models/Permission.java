package com.japim.models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.annotation.PreDestroy;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "permissions")
@SQLRestriction("deleted_at IS NULL")
@SQLDelete(sql = "UPDATE permissions SET deleted_at = NOW() WHERE id = ?")
public  class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @ManyToMany(mappedBy = "permissions",fetch = FetchType.LAZY)
    private Set<Role> roles = new HashSet<>();
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private LocalDateTime deleted_at;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public String getDescription() {
        return description;
    }

    public Set<Role> getRoles(){
        return this.roles;
    }

    public void setRoles(Set<Role> roles){
        this.roles = roles;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
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