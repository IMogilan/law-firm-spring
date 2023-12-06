package com.mogilan.repository;

import com.mogilan.config.PersistenceJPAConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WebAppConfiguration.class, PersistenceJPAConfig.class})
@WebAppConfiguration()
class ClientRepositoryTest {

    @Autowired
    ClientRepository clientRepository;

    @Test
    void findAllSuccess() {
        var actualResult = clientRepository.findAll();
        assertThat(actualResult).isNotEmpty();
        assertThat(actualResult).hasSize(3);
    }

}