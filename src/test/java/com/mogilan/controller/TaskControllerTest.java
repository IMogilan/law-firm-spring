package com.mogilan.controller;

import com.mogilan.PersistenceJPATestConfig;
import com.mogilan.TestData;
import com.mogilan.config.WebConfig;
import com.mogilan.dto.TaskDto;
import com.mogilan.exception.handler.GlobalExceptionHandler;
import com.mogilan.service.TaskService;
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
class TaskControllerTest {

    @Mock
    TaskService taskService;
    @InjectMocks
    private TaskController taskController;
    private MockMvc mockMvc;

    @BeforeEach
    void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(taskController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getAllSuccess() throws Exception {
        doReturn(getDtoList()).when(taskService).readAll();
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getAllShouldThrowExceptionIfUrlIncorrect() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/tasks/"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getByIdSuccess() throws Exception {
        Long id = getDtoWithId().getId();
        doReturn(getDto()).when(taskService).readById(id);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks/{1}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value(getDto().getTitle()));
    }

    @Test
    void getByIdShouldThrowExceptionIfIdIncorrect() throws Exception {
        doThrow(new EntityNotFoundException()).when(taskService).readById(any());
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks/{1}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void saveSuccess() throws Exception {
        doReturn(getDto()).when(taskService).create(any());
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/tasks/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Apple\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value(getDto().getTitle()));
    }

    @Test
    void saveShouldThrowExceptionIfDtoIncorrect() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/tasks/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"ANY_CONTENT\""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateSuccess() throws Exception {
        Long id = getDtoWithId().getId();
        doNothing().when(taskService).update(any(),any());
        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/tasks/{1}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Apple\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(TaskController.UPDATED_MESSAGE));
        verify(taskService, times(1)).update(any(), any());
    }

    @Test
    void updateShouldThrowExceptionIfIdIncorrect() throws Exception {
        Long id = getDtoWithId().getId();
        doThrow(new EntityNotFoundException()).when(taskService).update(any(), any());
        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/tasks/{1}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Apple\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateShouldThrowExceptionIfDtoIncorrect() throws Exception {
        Long id = getDtoWithId().getId();
        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/tasks/{1}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"ANY_CONTENT\""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteByIdSuccess() throws Exception {
        Long id = getDtoWithId().getId();
        doNothing().when(taskService).deleteById(id);
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/tasks/{1}", id))
                .andExpect(status().isOk())
                .andExpect(content().string(TaskController.DELETED_MESSAGE));
        verify(taskService, times(1)).deleteById(id);
    }

    @Test
    void deleteByIdShouldThrowExceptionIfIdIncorrect() throws Exception {
        Long id = getDtoWithId().getId();
        doThrow(new EntityNotFoundException()).when(taskService).deleteById(id);
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/tasks/{1}", id))
                .andExpect(status().isNotFound());
    }

    private TaskDto getDto() {
        var dto = TestData.getTaskDto1();
        var simpleClient = TestData.getSimpleClient1();
        dto.setClient(simpleClient);
        var simpleLawyer1 = TestData.getSimpleLawyer1();
        simpleLawyer1.setId(1L);
        var simpleLawyer2 = TestData.getSimpleLawyer2();
        var simpleLawyer3 = TestData.getSimpleLawyer3();
        dto.setLawyers(List.of(simpleLawyer1, simpleLawyer2, simpleLawyer3));

        return dto;
    }

    private TaskDto getDtoWithId() {
        var dto = getDto();
        dto.setId(1L);
        return dto;
    }

    private List<TaskDto> getDtoList() {
        return List.of(getDtoWithId(), getDtoWithId(), getDtoWithId(), getDtoWithId(), getDtoWithId());
    }

}