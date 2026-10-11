package com.japim.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.japim.models.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long>{}
