package com.japim.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.japim.models.StatusEvent;


public interface StatusEventRepository extends JpaRepository<StatusEvent, Object>{

    Optional<StatusEvent> findByInitialism(String initialism);
    
}
