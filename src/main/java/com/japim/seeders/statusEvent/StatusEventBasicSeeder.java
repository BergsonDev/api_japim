package com.japim.seeders.statusEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.japim.models.StatusEvent;
import com.japim.repositories.StatusEventRepository;
import com.japim.seeders.InterfaceSeeder;


@Component
@Order(6)
public class StatusEventBasicSeeder implements InterfaceSeeder{

    private StatusEventRepository statusRepo;

    public StatusEventBasicSeeder(StatusEventRepository statusRepo){
        this.statusRepo = statusRepo;
    }

    @Override
    public String nameSeeder() {
        return "Status de Eventos";
    }

    @Override 
    public void run() {
        Map<String, String> status_events = Map.of(
            "Criado","CRI",
            "Aprovado","APR",
            "Cancelado","CAN",
            "Encerrado","ENC"
        );

        List<StatusEvent> list_status_events = new ArrayList<StatusEvent>();

        for (Map.Entry<String, String> statusEvent : status_events.entrySet()) {
            StatusEvent status_event = new StatusEvent();
            status_event.setName(statusEvent.getKey());
            status_event.setInitialism(statusEvent.getValue());
            list_status_events.add(status_event);
        }

        statusRepo.saveAll(list_status_events);
        
    }
}
