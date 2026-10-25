package com.japim.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.japim.models.User;

public interface UserRepository extends  JpaRepository<User, Long>{}
