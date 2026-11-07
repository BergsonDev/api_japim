package com.japim.repositories;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.japim.models.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long>{

    List<Permission> findByNameIn(List<String>name);
}
