package com.japim.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import com.japim.models.Role;


public interface RoleRepository extends JpaRepository<Role, Long>{
    
    Optional<Role> findByNameOrInitialism(String name,String initialism);
    Optional<Role> findByName(String name);
    Optional<Role> findByInitialism(String initialism);
    
}
