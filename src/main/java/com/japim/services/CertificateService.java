package com.japim.services;

import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.japim.dtos.certificates.CertificateDTO;
import com.japim.exceptions.certificates.DaysDiffOfEventPresenceException;
import com.japim.exceptions.certificates.DontGenerateCertificateException;
import com.japim.exceptions.certificates.UserDontExistsInEventException;
import com.japim.exceptions.defaults.EntityDontFindException;
import com.japim.models.Certificate;
import com.japim.models.Event;
import com.japim.models.User;
import com.japim.repositories.CertificateRepository;
import com.japim.repositories.EventPresenceRepository;
import com.japim.repositories.EventRepository;
import com.japim.security.AuthSecurity;

@Service 
public class CertificateService {

    private final CertificateRepository certificateRepo;
    private final EventRepository eventRepo;
    private final UserService userService;
    private final EventPresenceRepository eventPresenceRepo;

    private final String IN_PROGRESS = "EAM";
    private final String CANCELED = "CAN";
    private final String CLOSED = "ENC";
    private final String APROVATED = "APR";
    private final String CREATED = "CRI";
    
    private CertificateDTO parse(Certificate certificate){
        return new CertificateDTO(
            certificate.getId(),
            certificate.getDate_generated(),
            certificate.getEvent(),
            certificate.getUser()
        );
    }

    public CertificateService(CertificateRepository certificateRepo, EventRepository eventRepo, UserService userService, EventPresenceRepository eventPresenceRepo){
        this.certificateRepo = certificateRepo;
        this.eventRepo = eventRepo;
        this.userService = userService;
        this.eventPresenceRepo = eventPresenceRepo;
    }

    public List<CertificateDTO> certificates(){
        return certificateRepo.findAll().stream().map(this::parse).toList();
    }

    public CertificateDTO certificate(Long id){
        return parse(certificateRepo.findById(id).orElseThrow(()-> new EntityDontFindException(id,"certificates")));

    }
    
    public List<CertificateDTO> myCertificates(Authentication auth){
        AuthSecurity authSecurity = (AuthSecurity) auth.getPrincipal();
        User user = authSecurity.getUserEntity();
        return certificateRepo.findAllByUser_Id(user.getId()).stream().map(this::parse).toList();
    }

    public CertificateDTO getMyCertificate(Authentication auth, Long event_id){
        AuthSecurity authSecurity = (AuthSecurity) auth.getPrincipal();
        User user = authSecurity.getUserEntity();

        Certificate certificate = certificateRepo.findByUser_IdAndEvent_Id(user.getId(), event_id).orElse(null);

        if (certificate != null) {
            return parse(certificate);
        }

        if(!eventRepo.existsByIdAndParticipants_Id(event_id, user.getId()) && !eventRepo.existsByIdAndVolunteers_Id(event_id, user.getId())){
            throw new UserDontExistsInEventException();
        }
        
        Event event = eventRepo.findById(event_id).orElseThrow(()->new EntityDontFindException(event_id, "events"));
        
        long days = ChronoUnit.DAYS.between(event.getDate_start(), event.getDate_end()) + 1;
        long days_in_presence = eventPresenceRepo.countByParticipant_IdAndEvent_Id(user.getId(), event.getId());
        
        if(days != days_in_presence){
            throw new DaysDiffOfEventPresenceException();
        }

        certificate = new Certificate();
        certificate.setEvent(event);
        certificate.setUser(user);
        certificateRepo.save(certificate);

        return parse(certificate);

    }
}
