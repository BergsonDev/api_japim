package com.japim.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.japim.models.Certificate;

public interface CertificateRepository extends JpaRepository<Certificate, Long>{
    List<Certificate> findAllByUser_Id(Long user_id);

    Optional<Certificate> findByUser_IdAndEvent_Id(Long user_id, Long event_id);
}
