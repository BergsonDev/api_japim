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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
@SQLRestriction("deleted_at IS NULL")
@SQLDelete(sql = "UPDATE roles SET deleted_at = NOW() WHERE id = ?")
public  class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String initialism;
    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "role_permissions",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions = new HashSet<>();

    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private LocalDateTime deleted_at;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getInitialism() {
        return initialism;
    }

    public String getDescription() {
        return description;
    }

    public void setInitialism(String initialism) {
        this.initialism = initialism;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addPermission(Permission permission){
        this.permissions.add(permission);
        permission.getRoles().add(this);
    }

    public void removePermission(Permission permission){
        this.permissions.remove(permission);
        permission.getRoles().remove(this);
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

    public Set<Permission> getPermissions(){
        return this.permissions;
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