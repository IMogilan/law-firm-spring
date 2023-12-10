//package com.mogilan.service.impl;
//
//import com.mogilan.TestData;
//import com.mogilan.dto.ClientDto;
//import com.mogilan.dto.SimpleTaskDto;
//import com.mogilan.mapper.ClientMapper;
//import com.mogilan.model.Client;
//import com.mogilan.model.Task;
//import com.mogilan.repository.ClientRepository;
//import com.mogilan.repository.TaskRepository;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class ClientServiceImplTest {
//
//    @Mock
//    ClientRepository clientRepository;
//    @Mock
//    ClientMapper clientMapper;
//    @Mock
//    TaskRepository taskRepository;
//    @InjectMocks
//    ClientServiceImpl clientService;
//
//    @Test
//    void createSuccess() {
//        var dto = getDto();
//        var entity = getEntity();
//        doReturn(entity).when(clientMapper).toEntity(dto);
//
//        var entityWithId = getEntityWithId();
//        doReturn(entityWithId).when(clientDao).save(entity);
//
//        var dtoWithId = getDtoWithId();
//        doReturn(dtoWithId).when(clientMapper).toDto(entityWithId);
//
//        var taskDtoList = List.of(new TaskDto(), new TaskDto(), new TaskDto(), new TaskDto(), new TaskDto());
//        doReturn(taskDtoList).when(simpleTaskMapper).toTaskDtoList(dto.getTasks());
//
//        doReturn(taskDtoList).when(taskService).readAllByClientId(dtoWithId.getId());
//        doReturn(dto.getTasks()).when(simpleTaskMapper).toSimpleTaskDtoList(taskDtoList);
//
//        var actualResult = clientService.create(dto);
//
//        assertThat(actualResult).isNotNull();
//        assertThat(actualResult.getId()).isNotNull();
//        assertThat(actualResult.getId()).isEqualTo(dtoWithId.getId());
//        assertThat(actualResult.getTasks()).isNotNull();
//        assertThat(actualResult.getTasks()).isNotEmpty();
//        assertThat(actualResult.getTasks().size()).isEqualTo(taskDtoList.size());
//        verify(taskService, times(1)).readAllByClientId(dtoWithId.getId());
//        verify(taskService, times(5)).create(any(TaskDto.class));
//    }
//
//    @Test
//    void createShouldThrowExceptionIfDtoIsNull() {
//        ClientDto clientDto = null;
//        Assertions.assertThrows(NullPointerException.class, () -> clientService.create(clientDto));
//    }
//
//    @Test
//    void readAllSuccess() {
//        var entityList = getEntityList();
//        doReturn(entityList).when(clientDao).findAll();
//        var dtoList = getDtoList();
//        doReturn(dtoList).when(clientMapper).toDtoList(entityList);
//
//        var actualResult = clientService.readAll();
//        assertThat(actualResult).isNotNull();
//        assertThat(actualResult).isNotEmpty();
//        assertThat(actualResult).hasSize(entityList.size());
//    }
//
//    @Test
//    void readById() {
//        Long id = 1L;
//        var entityWithId = getEntityWithId();
//        var dtoWithId = getDtoWithId();
//        doReturn(Optional.of(entityWithId)).when(clientDao).findById(id);
//        doReturn(dtoWithId).when(clientMapper).toDto(entityWithId);
//
//        var actualResult = clientService.readById(id);
//        assertThat(actualResult).isNotNull();
//        assertThat(actualResult.getId()).isEqualTo(id);
//    }
//
//    @Test
//    void readByIdShouldThrowExceptionIfIdIsNull() {
//        Long id = null;
//        Assertions.assertThrows(NullPointerException.class, () -> clientService.readById(id));
//    }
//
//    @Test
//    void readByIdShouldThrowExceptionIfEntityNotFound() {
//        Long id = 1000L;
//        doReturn(Optional.empty()).when(clientDao).findById(id);
//        Assertions.assertThrows(EntityNotFoundException.class, () -> clientService.readById(id));
//    }
//
//    @Test
//    void update() {
//        Long id = 1L;
//        var entity = getEntity();
//        var dto = getDto();
//        doReturn(entity).when(clientMapper).toEntity(dto);
//        doReturn(Optional.of(entity)).when(clientDao).findById(id);
//        var taskDto1 = new TaskDto();
//        taskDto1.setId(1L);
//        var taskDto2 = new TaskDto();
//        taskDto2.setId(2L);
//        var taskDto3 = new TaskDto();
//        taskDto2.setId(3L);
//        var prevTaskList = new ArrayList<>(List.of(taskDto1, taskDto2));
//        doReturn(prevTaskList).when(taskMapper).toDtoList(any());
//        var newTaskList = new ArrayList<>(List.of(taskDto2, taskDto3));
//        doReturn(newTaskList).when(taskService).readAllByClientId(id);
//
//        clientService.update(id, dto);
//
//        verify(clientDao, times(1)).update(captor.capture());
//        var value = captor.getValue();
//        assertThat(value).isNotNull();
//        assertThat(value.getId()).isEqualTo(id);
//
//        verify(taskService, times(2)).update(any(), any(TaskDto.class));
//        verify(taskService, times(1)).deleteById(any());
//    }
//
//    @Test
//    void updateShouldThrowExceptionIfIdIsNull() {
//        Long id = null;
//        ClientDto any = new ClientDto();
//        Assertions.assertThrows(NullPointerException.class, () -> clientService.update(id, any));
//    }
//
//    @Test
//    void updateShouldThrowExceptionIfDtoIsNull() {
//        Long id = 1L;
//        ClientDto any = null;
//        Assertions.assertThrows(NullPointerException.class, () -> clientService.update(id, any));
//    }
//
//    @Test
//    void updateShouldThrowExceptionIfEntityNotFound() {
//        Long id = 1000L;
//        ClientDto any = new ClientDto();
//        doReturn(Optional.empty()).when(clientDao).findById(id);
//        Assertions.assertThrows(EntityNotFoundException.class, () -> clientService.update(id, any));
//    }
//
//    @Test
//    void deleteById() {
//        Long id = 1L;
//        var entity = getEntity();
//        doReturn(Optional.of(entity)).when(clientDao).findById(id);
//
//        clientService.deleteById(id);
//
//        verify(clientDao, times(1)).delete(id);
//    }
//
//    @Test
//    void deleteShouldThrowExceptionIfIdIsNull() {
//        Long id = null;
//        Assertions.assertThrows(NullPointerException.class, () -> clientService.deleteById(id));
//    }
//
//    @Test
//    void deleteShouldThrowExceptionIfEntityNotFound() {
//        Long id = 1000L;
//        doReturn(Optional.empty()).when(clientDao).findById(id);
//        Assertions.assertThrows(EntityNotFoundException.class, () -> clientService.deleteById(id));
//    }
//
//    private ClientDto getDto() {
//        var dto = TestData.getClientDto1();
//
//        var simpleTask1 = TestData.getSimpleTask1();
//        var simpleTask2 = TestData.getSimpleTask2();
//        var simpleTask3 = TestData.getSimpleTask3();
//        dto.setTasks(List.of(simpleTask1, simpleTask2, simpleTask3));
//
//        return dto;
//    }
//
//    private ClientDto getDtoWithId() {
//        var dto = getDto();
//
//        dto.setId(1L);
//
//        return dto;
//    }
//
//    private Client getEntity() {
//        var entity = TestData.getClient1();
//
//        var task1 = TestData.getTask1();
//        var task2 = TestData.getTask2();
//        var task3 = TestData.getTask3();
//        entity.setTasks(List.of(task1, task2, task3));
//
//        return entity;
//    }
//
//    private Client getEntityWithId() {
//        var entity = getEntity();
//
//        entity.setId(1L);
//
//        return entity;
//    }
//
//    private List<ClientDto> getDtoList() {
//        return List.of(getDtoWithId(), getDtoWithId(), getDtoWithId(), getDtoWithId(), getDtoWithId());
//    }
//
//    private List<Client> getEntityList() {
//        return List.of(getEntityWithId(), getEntityWithId(), getEntityWithId(), getEntityWithId(), getEntityWithId());
//    }
//}