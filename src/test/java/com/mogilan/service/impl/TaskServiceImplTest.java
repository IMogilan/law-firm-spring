package com.mogilan.service.impl;

import com.mogilan.TestData;
import com.mogilan.dto.TaskDto;
import com.mogilan.mapper.TaskMapper;
import com.mogilan.model.Client;
import com.mogilan.model.Lawyer;
import com.mogilan.model.Task;
import com.mogilan.repository.ClientRepository;
import com.mogilan.repository.LawyerRepository;
import com.mogilan.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    TaskRepository taskRepository;
    @Mock
    TaskMapper taskMapper;
    @Mock
    LawyerRepository lawyerRepository;
    @Mock
    ClientRepository clientRepository;
    @InjectMocks
    TaskServiceImpl taskService;
    @Captor
    ArgumentCaptor<Task> taskArgumentCaptor;

    @Test
    void create() {
        var dto = getDto();
        var entity = getEntity();
        doReturn(entity).when(taskMapper).toEntity(dto);

        var client = TestData.getClient1();
        client.setId(1L);
        entity.setClient(client);

        doReturn(Optional.of(client)).when(clientRepository).findById(client.getId());
        doReturn(Optional.of(new Lawyer())).when(lawyerRepository).findById(any());

        var entityWithId = getEntityWithId();
        doReturn(entityWithId).when(taskRepository).save(entity);

        var dtoWithId = getDtoWithId();
        doReturn(dtoWithId).when(taskMapper).toDto(entityWithId);

        var actualResult = taskService.create(dto);

        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isEqualTo(dtoWithId);

        assertThat(actualResult.getLawyers()).isNotNull();
        assertThat(actualResult.getLawyers()).isNotEmpty();
        assertThat(actualResult.getLawyers().size()).isEqualTo(dto.getLawyers().size());

        var numberOfTaskWithId = 1;
        var numberOfLawyerRepositoryInvokingForTaskWithId = 1;
        verify(lawyerRepository, times(numberOfTaskWithId * numberOfLawyerRepositoryInvokingForTaskWithId)).findById(any());
        verify(clientRepository, times(1)).findById(any());
        verify(taskRepository, times(1)).save(taskArgumentCaptor.capture());
        var value = taskArgumentCaptor.getValue();
        assertThat(value.getLawyers()).hasSize(dto.getLawyers().size());
        value.getLawyers().forEach(lawyer -> assertThat(lawyer.getTasks()).contains(entity));
        assertThat(value.getClient().getTasks()).contains(entity);
    }

    @Test
    void createShouldThrowExceptionIfDtoIsNull() {
        TaskDto taskDto = null;
        Assertions.assertThrows(NullPointerException.class, () -> taskService.create(taskDto));
    }

    @Test
    void readAll() {
        var entityList = getEntityList();
        doReturn(entityList).when(taskRepository).findAll();

        var actualResult = taskService.readAll();
        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isNotEmpty();
        assertThat(actualResult).hasSize(entityList.size());
        verify(taskMapper, times(entityList.size())).toDto(any());
    }

    @Test
    void readById() {
        Long id = 1L;
        var entityWithId = getEntityWithId();
        var dtoWithId = getDtoWithId();
        doReturn(Optional.of(entityWithId)).when(taskRepository).findById(id);
        doReturn(dtoWithId).when(taskMapper).toDto(entityWithId);

        var actualResult = taskService.readById(id);
        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isEqualTo(dtoWithId);
    }

    @Test
    void readByIdShouldThrowExceptionIfIdIsNull() {
        Long id = null;
        Assertions.assertThrows(NullPointerException.class, () -> taskService.readById(id));
    }

    @Test
    void readByIdShouldThrowExceptionIfEntityNotFound() {
        Long id = 1000L;
        doReturn(Optional.empty()).when(taskRepository).findById(id);
        Assertions.assertThrows(EntityNotFoundException.class, () -> taskService.readById(id));
    }

    @Test
    void update() {
        Long id = getDtoWithId().getId();
        doReturn(true).when(taskRepository).existsById(id);

        var dto = getDto();
        var entity = getEntity();
        doReturn(entity).when(taskMapper).toEntity(dto);

        var client = TestData.getClient1();
        client.setId(1L);
        entity.setClient(client);

        doReturn(Optional.of(client)).when(clientRepository).findById(client.getId());
        doReturn(Optional.of(new Lawyer())).when(lawyerRepository).findById(any());

        var entityWithId = getEntityWithId();
        doReturn(entityWithId).when(taskRepository).save(entity);

        taskService.update(id, dto);

        var numberOfTaskWithId = 1;
        var numberOfLawyerRepositoryInvokingForTaskWithId = 1;
        verify(lawyerRepository, times(numberOfTaskWithId * numberOfLawyerRepositoryInvokingForTaskWithId)).findById(any());
        verify(clientRepository, times(1)).findById(any());
        verify(taskRepository, times(1)).save(taskArgumentCaptor.capture());
        var value = taskArgumentCaptor.getValue();
        assertThat(value.getLawyers()).hasSize(dto.getLawyers().size());
        value.getLawyers().forEach(lawyer -> assertThat(lawyer.getTasks()).contains(entity));
        assertThat(value.getClient().getTasks()).contains(entity);
    }

    @Test
    void updateShouldThrowExceptionIfIdIsNull() {
        Long id = null;
        TaskDto any = new TaskDto();
        Assertions.assertThrows(NullPointerException.class, () -> taskService.update(id, any));
    }

    @Test
    void updateShouldThrowExceptionIfDtoIsNull() {
        Long id = 1L;
        TaskDto any = null;
        Assertions.assertThrows(NullPointerException.class, () -> taskService.update(id, any));
    }

    @Test
    void updateShouldThrowExceptionIfEntityNotFound() {
        Long id = 1000L;
        TaskDto any = new TaskDto();
        doReturn(false).when(taskRepository).existsById(id);
        Assertions.assertThrows(EntityNotFoundException.class, () -> taskService.update(id, any));
    }

    @Test
    void deleteById() {
        Long id = getEntityWithId().getId();
        doReturn(true).when(taskRepository).existsById(id);

        taskService.deleteById(id);

        verify(taskRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteShouldThrowExceptionIfIdIsNull() {
        Long id = null;
        Assertions.assertThrows(NullPointerException.class, () -> taskService.deleteById(id));
    }

    @Test
    void deleteShouldThrowExceptionIfEntityNotFound() {
        Long id = 1000L;
        doReturn(false).when(taskRepository).existsById(id);
        Assertions.assertThrows(EntityNotFoundException.class, () -> taskService.deleteById(id));
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

    private Task getEntity() {
        var entity = TestData.getTask1();
        var client = TestData.getClient1();
        entity.setClient(client);
        var lawyer1 = TestData.getLawyer1();
        lawyer1.setId(1L);
        var lawyer2 = TestData.getLawyer2();
        var lawyer3 = TestData.getLawyer3();
        entity.setLawyers(List.of(lawyer1, lawyer2, lawyer3));

        return entity;
    }

    private Task getEntityWithId() {
        var entity = getEntity();
        entity.setId(1L);
        return entity;
    }

    private List<TaskDto> getDtoList() {
        return List.of(getDtoWithId(), getDtoWithId(), getDtoWithId(), getDtoWithId(), getDtoWithId());
    }

    private List<Task> getEntityList() {
        return List.of(getEntityWithId(), getEntityWithId(), getEntityWithId(), getEntityWithId(), getEntityWithId());
    }
}