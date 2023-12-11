package com.mogilan.controller;

import com.mogilan.PersistenceJPATestConfig;
import com.mogilan.config.WebConfig;
import com.mogilan.service.ClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {WebConfig.class, PersistenceJPATestConfig.class})
@WebAppConfiguration()
class ClientControllerTest {

    @Mock
    ClientService clientService;

    @Test
    void getAll() {
    }

    @Test
    void getById() {
    }

    @Test
    void save() {
    }

    @Test
    void update() {
    }

    @Test
    void deleteById() {
    }
}