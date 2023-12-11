package com.mogilan.controller;

import com.mogilan.PersistenceJPATestConfig;
import com.mogilan.TestData;
import com.mogilan.config.WebConfig;
import com.mogilan.dto.LawyerDto;
import com.mogilan.exception.handler.GlobalExceptionHandler;
import com.mogilan.service.LawyerService;
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
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {WebConfig.class, PersistenceJPATestConfig.class})
@WebAppConfiguration()
class LawyerControllerTest {

    @Mock
    LawyerService lawyerService;
    @InjectMocks
    private LawyerController lawyerController;
    private MockMvc mockMvc;

    @BeforeEach
    void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(lawyerController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getAllSuccess() throws Exception {
        doReturn(getDtoList()).when(lawyerService).readAll();
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/lawyers/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getAllShouldThrowExceptionIfUrlIncorrect() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/lawyers/"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getByIdSuccess() throws Exception {
        Long id = getDtoWithId().getId();
        doReturn(getDto()).when(lawyerService).readById(id);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/lawyers/{1}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value(getDto().getFirstName()));
    }

    @Test
    void getByIdShouldThrowExceptionIfIdIncorrect() throws Exception {
        doThrow(new EntityNotFoundException()).when(lawyerService).readById(any());
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/lawyers/{1}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void saveSuccess() throws Exception {
        doReturn(getDto()).when(lawyerService).create(any());
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/lawyers/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Apple\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value(getDto().getFirstName()));
    }

    @Test
    void saveShouldThrowExceptionIfDtoIncorrect() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/lawyers/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"ANY_CONTENT\""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateSuccess() throws Exception {
        Long id = getDtoWithId().getId();
        doNothing().when(lawyerService).update(any(), any());
        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/lawyers/{1}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Apple\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(LawyerController.UPDATED_MESSAGE));
        verify(lawyerService, times(1)).update(any(), any());
    }

    @Test
    void updateShouldThrowExceptionIfIdIncorrect() throws Exception {
        Long id = getDtoWithId().getId();
        doThrow(new EntityNotFoundException()).when(lawyerService).update(any(), any());
        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/lawyers/{1}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Apple\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateShouldThrowExceptionIfDtoIncorrect() throws Exception {
        Long id = getDtoWithId().getId();
        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/lawyers/{1}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"ANY_CONTENT\""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteByIdSuccess() throws Exception {
        Long id = getDtoWithId().getId();
        doNothing().when(lawyerService).deleteById(id);
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/lawyers/{1}", id))
                .andExpect(status().isOk())
                .andExpect(content().string(LawyerController.DELETED_MESSAGE));
        verify(lawyerService, times(1)).deleteById(id);
    }

    @Test
    void deleteByIdShouldThrowExceptionIfIdIncorrect() throws Exception {
        Long id = getDtoWithId().getId();
        doThrow(new EntityNotFoundException()).when(lawyerService).deleteById(id);
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/lawyers/{1}", id))
                .andExpect(status().isNotFound());
    }

    private LawyerDto getDto() {
        var dto = TestData.getLawyerDto1();

        var simpleTask1 = TestData.getSimpleTask1();
        simpleTask1.setId(1L);
        var simpleTask2 = TestData.getSimpleTask2();
        var simpleTask3 = TestData.getSimpleTask3();

        dto.setTasks(List.of(simpleTask1, simpleTask2, simpleTask3));

        return dto;
    }

    private LawyerDto getDtoWithId() {
        var dto = getDto();
        dto.setId(1L);
        return dto;
    }

    private List<LawyerDto> getDtoList() {
        return List.of(getDtoWithId(), getDtoWithId(), getDtoWithId(), getDtoWithId(), getDtoWithId());
    }

}