package com.mogilan.service.impl;

import com.mogilan.TestData;
import com.mogilan.dto.ClientDto;
import com.mogilan.mapper.ClientMapper;
import com.mogilan.model.Client;
import com.mogilan.repository.ClientRepository;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    ClientRepository clientRepository;
    @Mock
    ClientMapper clientMapper;
    @Mock
    TaskRepository taskRepository;
    @InjectMocks
    ClientServiceImpl clientService;
    @Captor
    ArgumentCaptor<Client> clientArgumentCaptor;

    @Test
    void createSuccess() {
        var dto = getDto();
        var entity = getEntity();
        doReturn(entity).when(clientMapper).toEntity(dto);

        doReturn(Optional.empty()).when(taskRepository).findById(any());

        var entityWithId = getEntityWithId();
        doReturn(entityWithId).when(clientRepository).save(entity);

        var dtoWithId = getDtoWithId();
        doReturn(dtoWithId).when(clientMapper).toDto(entityWithId);

        var actualResult = clientService.create(dto);

        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isEqualTo(dtoWithId);
        assertThat(actualResult.getTasks()).isNotNull();
        assertThat(actualResult.getTasks()).isNotEmpty();
        assertThat(actualResult.getTasks().size()).isEqualTo(dto.getTasks().size());
        verify(taskRepository, times(dto.getTasks().size())).findById(any());
        verify(clientRepository, times(1)).save(clientArgumentCaptor.capture());
        var value = clientArgumentCaptor.getValue();
        assertThat(value.getTasks()).hasSize(dto.getTasks().size());
        value.getTasks().forEach(task -> assertThat(task.getClient()).isEqualTo(entity));
    }

    @Test
    void createShouldThrowExceptionIfDtoIsNull() {
        ClientDto clientDto = null;
        Assertions.assertThrows(NullPointerException.class, () -> clientService.create(clientDto));
    }

    @Test
    void readAllSuccess() {
        var entityList = getEntityList();
        doReturn(entityList).when(clientRepository).findAll();

        var actualResult = clientService.readAll();
        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isNotEmpty();
        assertThat(actualResult).hasSize(entityList.size());
        verify(clientMapper, times(entityList.size())).toDto(any());
    }

    @Test
    void readById() {
        Long id = 1L;
        var entityWithId = getEntityWithId();
        var dtoWithId = getDtoWithId();
        doReturn(Optional.of(entityWithId)).when(clientRepository).findById(id);
        doReturn(dtoWithId).when(clientMapper).toDto(entityWithId);

        var actualResult = clientService.readById(id);
        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isEqualTo(dtoWithId);
    }

    @Test
    void readByIdShouldThrowExceptionIfIdIsNull() {
        Long id = null;
        Assertions.assertThrows(NullPointerException.class, () -> clientService.readById(id));
    }

    @Test
    void readByIdShouldThrowExceptionIfEntityNotFound() {
        Long id = 1000L;
        doReturn(Optional.empty()).when(clientRepository).findById(id);
        Assertions.assertThrows(EntityNotFoundException.class, () -> clientService.readById(id));
    }

    @Test
    void update() {
        Long id = getEntityWithId().getId();
        doReturn(true).when(clientRepository).existsById(id);
        var entity = getEntity();
        var dto = getDto();
        doReturn(entity).when(clientMapper).toEntity(dto);

        doReturn(Optional.empty()).when(taskRepository).findById(any());

        var entityWithId = getEntityWithId();
        doReturn(entityWithId).when(clientRepository).save(entity);

        clientService.update(id, dto);

        verify(taskRepository, times(dto.getTasks().size())).findById(any());
        verify(clientRepository, times(1)).save(clientArgumentCaptor.capture());
        var value = clientArgumentCaptor.getValue();
        assertThat(value.getTasks()).hasSize(dto.getTasks().size());
        value.getTasks().forEach(task -> assertThat(task.getClient()).isEqualTo(entity));
    }

    @Test
    void updateShouldThrowExceptionIfIdIsNull() {
        Long id = null;
        ClientDto any = new ClientDto();
        Assertions.assertThrows(NullPointerException.class, () -> clientService.update(id, any));
    }

    @Test
    void updateShouldThrowExceptionIfDtoIsNull() {
        Long id = 1L;
        ClientDto any = null;
        Assertions.assertThrows(NullPointerException.class, () -> clientService.update(id, any));
    }

    @Test
    void updateShouldThrowExceptionIfEntityNotFound() {
        Long id = 1000L;
        ClientDto any = new ClientDto();
        doReturn(false).when(clientRepository).existsById(id);
        Assertions.assertThrows(EntityNotFoundException.class, () -> clientService.update(id, any));
    }

    @Test
    void deleteById() {
        Long id = getEntityWithId().getId();
        doReturn(true).when(clientRepository).existsById(id);

        clientService.deleteById(id);

        verify(clientRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteShouldThrowExceptionIfIdIsNull() {
        Long id = null;
        Assertions.assertThrows(NullPointerException.class, () -> clientService.deleteById(id));
    }

    @Test
    void deleteShouldThrowExceptionIfEntityNotFound() {
        Long id = 1000L;
        doReturn(false).when(clientRepository).existsById(id);
        Assertions.assertThrows(EntityNotFoundException.class, () -> clientService.deleteById(id));
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

    private Client getEntity() {
        var entity = TestData.getClient1();

        var task1 = TestData.getTask1();
        task1.setId(1L);
        var task2 = TestData.getTask2();
        task2.setId(2L);
        var task3 = TestData.getTask3();
        task3.setId(3L);
        entity.setTasks(List.of(task1, task2, task3));

        return entity;
    }

    private Client getEntityWithId() {
        var entity = getEntity();
        entity.setId(1L);
        return entity;
    }

    private List<ClientDto> getDtoList() {
        return List.of(getDtoWithId(), getDtoWithId(), getDtoWithId(), getDtoWithId(), getDtoWithId());
    }

    private List<Client> getEntityList() {
        return List.of(getEntityWithId(), getEntityWithId(), getEntityWithId(), getEntityWithId(), getEntityWithId());
    }
}