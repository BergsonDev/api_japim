package com.japim;

import com.japim.repositories.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RoleCRUDTest {

    @Autowired
    private RoleRepository roleRepo;

    @Test
    public void teste() {
        var roles = roleRepo.findAll();
        assertThat(roles).isNotNull();
    }
}