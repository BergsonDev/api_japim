package com.japim.seeders;

import org.springframework.transaction.annotation.Transactional;

public interface InterfaceSeeder {
    public String nameSeeder();
    @Transactional 
    public void run();
}
