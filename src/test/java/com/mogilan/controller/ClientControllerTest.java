package com.mogilan.controller;

import com.mogilan.PersistenceJPATestConfig;
import com.mogilan.TestData;
import com.mogilan.config.WebConfig;
import com.mogilan.dto.ClientDto;
import com.mogilan.exception.handler.GlobalExceptionHandler;
import com.mogilan.service.ClientService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {WebConfig.class, PersistenceJPATestConfig.class})
@WebAppConfiguration()
class ClientControllerTest {

    @Mock
    ClientService clientService;
    @InjectMocks
    private ClientController clientController;
    private MockMvc mockMvc;

    @BeforeEach
    void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(clientController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getAllSuccess() throws Exception {
        doReturn(getDtoList()).when(clientService).readAll();
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/clients/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getAllShouldThrowExceptionIfUrlIncorrect() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/clients/"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getByIdSuccess() throws Exception {
        Long id = getDtoWithId().getId();
        doReturn(getDto()).when(clientService).readById(id);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/clients/{1}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(getDto().getName()));
    }

    @Test
    void getByIdShouldThrowExceptionIfIdIncorrect() throws Exception {
        doThrow(new EntityNotFoundException()).when(clientService).readById(any());
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/clients/{1}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void saveSuccess() throws Exception {
        doReturn(getDto()).when(clientService).create(any());
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/clients/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Apple\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(getDto().getName()));
    }

    @Test
    void saveShouldThrowExceptionIfDtoIncorrect() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/clients/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"ANY_CONTENT\""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateSuccess() throws Exception {
        Long id = getDtoWithId().getId();
        doNothing().when(clientService).update(any(),any());
        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/clients/{1}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Apple\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(ClientController.UPDATED_MESSAGE));
        verify(clientService, times(1)).update(any(), any());
    }

    @Test
    void updateShouldThrowExceptionIfIdIncorrect() throws Exception {
        Long id = getDtoWithId().getId();
        doThrow(new EntityNotFoundException()).when(clientService).update(any(), any());
        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/clients/{1}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Apple\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateShouldThrowExceptionIfDtoIncorrect() throws Exception {
        Long id = getDtoWithId().getId();
        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/clients/{1}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"ANY_CONTENT\""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteByIdSuccess() throws Exception {
        Long id = getDtoWithId().getId();
        doNothing().when(clientService).deleteById(id);
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/clients/{1}", id))
                .andExpect(status().isOk())
                .andExpect(content().string(ClientController.DELETED_MESSAGE));
        verify(clientService, times(1)).deleteById(id);
    }

    @Test
    void deleteByIdShouldThrowExceptionIfIdIncorrect() throws Exception {
        Long id = getDtoWithId().getId();
        doThrow(new EntityNotFoundException()).when(clientService).deleteById(id);
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/clients/{1}", id))
                .andExpect(status().isNotFound());
    }

    private ClientDto getDto() {
        var dto = TestData.getClientDto1();

        var simpleTask1 = TestData.getSimpleTask1();
        simpleTask1.setId(1L);
        var simpleTask2 = TestData.getSimpleTask2();
        simpleTask2.setId(2L);
        var simpleTask3 = TestData.getSimpleTask3();
        simpleTask3.setId(3L);
        dto.setTasks(List.of(simpleTask1, simpleTask2, simpleTask3));

        return dto;
    }

    private ClientDto getDtoWithId() {
        var dto = getDto();
        dto.setId(1L);
        return dto;
    }

    private List<ClientDto> getDtoList() {
        return List.of(getDtoWithId(), getDtoWithId(), getDtoWithId(), getDtoWithId(), getDtoWithId());
    }
}