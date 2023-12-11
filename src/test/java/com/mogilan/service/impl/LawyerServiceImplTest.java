package com.mogilan.service.impl;

import com.mogilan.TestData;
import com.mogilan.dto.LawyerDto;
import com.mogilan.mapper.LawyerMapper;
import com.mogilan.model.Lawyer;
import com.mogilan.model.Task;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LawyerServiceImplTest {

    @Mock
    LawyerRepository lawyerRepository;
    @Mock
    LawyerMapper lawyerMapper;
    @Mock
    TaskRepository taskRepository;
    @InjectMocks
    LawyerServiceImpl lawyerService;
    @Captor
    ArgumentCaptor<Lawyer> lawyerArgumentCaptor;

    @Test
    void createSuccess() {
        var dto = getDto();
        var entity = getEntity();
        doReturn(entity).when(lawyerMapper).toEntity(dto);

        doReturn(Optional.of(new Task())).when(taskRepository).findById(any());

        var entityWithId = getEntityWithId();
        doReturn(entityWithId).when(lawyerRepository).save(entity);

        var dtoWithId = getDtoWithId();
        doReturn(dtoWithId).when(lawyerMapper).toDto(entityWithId);

        var actualResult = lawyerService.create(dto);

        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isEqualTo(dtoWithId);
        assertThat(actualResult.getTasks()).isNotNull();
        assertThat(actualResult.getTasks()).isNotEmpty();

        assertThat(actualResult.getTasks().size()).isEqualTo(dto.getTasks().size());
        var numberOfTaskWithId = 1;
        var numberOfTaskRepositoryInvokingForTaskWithId = 2;
        verify(taskRepository, times(numberOfTaskWithId * numberOfTaskRepositoryInvokingForTaskWithId)).findById(any());
        verify(lawyerRepository, times(1)).save(lawyerArgumentCaptor.capture());
        var value = lawyerArgumentCaptor.getValue();
        assertThat(value.getTasks()).hasSize(dto.getTasks().size());
        value.getTasks().forEach(task -> assertThat(task.getLawyers()).contains(entity));
    }

    @Test
    void createShouldThrowExceptionIfDtoIsNull() {
        LawyerDto lawyerDto = null;
        Assertions.assertThrows(NullPointerException.class, () -> lawyerService.create(lawyerDto));
    }

    @Test
    void readAll() {
        var entityList = getEntityList();
        doReturn(entityList).when(lawyerRepository).findAll();

        var actualResult = lawyerService.readAll();
        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isNotEmpty();
        assertThat(actualResult).hasSize(entityList.size());
        verify(lawyerMapper, times(entityList.size())).toDto(any());
    }

    @Test
    void readById() {
        Long id = 1L;
        var entityWithId = getEntityWithId();
        var dtoWithId = getDtoWithId();
        doReturn(Optional.of(entityWithId)).when(lawyerRepository).findById(id);
        doReturn(dtoWithId).when(lawyerMapper).toDto(entityWithId);

        var actualResult = lawyerService.readById(id);
        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isEqualTo(dtoWithId);
    }

    @Test
    void readByIdShouldThrowExceptionIfIdIsNull() {
        Long id = null;
        Assertions.assertThrows(NullPointerException.class, () -> lawyerService.readById(id));
    }

    @Test
    void readByIdShouldThrowExceptionIfEntityNotFound() {
        Long id = 1000L;
        doReturn(Optional.empty()).when(lawyerRepository).findById(id);
        Assertions.assertThrows(EntityNotFoundException.class, () -> lawyerService.readById(id));
    }

    @Test
    void update() {
        Long id = getEntityWithId().getId();
        doReturn(true).when(lawyerRepository).existsById(id);

        var entity = getEntity();
        var dto = getDto();
        doReturn(entity).when(lawyerMapper).toEntity(dto);

        doReturn(Optional.of(new Task())).when(taskRepository).findById(any());

        var entityWithId = getEntityWithId();
        doReturn(entityWithId).when(lawyerRepository).save(entity);

        lawyerService.update(id, dto);

        var numberOfTaskWithId = 1;
        var numberOfLawyerRepositoryInvokingForTaskWithId = 2;
        verify(taskRepository, times(numberOfTaskWithId * numberOfLawyerRepositoryInvokingForTaskWithId)).findById(any());
        verify(lawyerRepository, times(1)).save(lawyerArgumentCaptor.capture());
        var value = lawyerArgumentCaptor.getValue();
        assertThat(value.getTasks()).hasSize(dto.getTasks().size());
        value.getTasks().forEach(task -> assertThat(task.getLawyers()).contains(entity));
    }

    @Test
    void updateShouldThrowExceptionIfIdIsNull() {
        Long id = null;
        LawyerDto any = new LawyerDto();
        Assertions.assertThrows(NullPointerException.class, () -> lawyerService.update(id, any));
    }

    @Test
    void updateShouldThrowExceptionIfDtoIsNull() {
        Long id = 1L;
        LawyerDto any = null;
        Assertions.assertThrows(NullPointerException.class, () -> lawyerService.update(id, any));
    }

    @Test
    void updateShouldThrowExceptionIfEntityNotFound() {
        Long id = 1000L;
        LawyerDto any = new LawyerDto();
        doReturn(false).when(lawyerRepository).existsById(id);
        Assertions.assertThrows(EntityNotFoundException.class, () -> lawyerService.update(id, any));
    }

    @Test
    void deleteById() {
        Long id = getEntityWithId().getId();
        doReturn(true).when(lawyerRepository).existsById(id);

        lawyerService.deleteById(id);

        verify(lawyerRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteShouldThrowExceptionIfIdIsNull() {
        Long id = null;
        Assertions.assertThrows(NullPointerException.class, () -> lawyerService.deleteById(id));
    }

    @Test
    void deleteShouldThrowExceptionIfEntityNotFound() {
        Long id = 1000L;
        doReturn(false).when(lawyerRepository).existsById(id);
        Assertions.assertThrows(EntityNotFoundException.class, () -> lawyerService.deleteById(id));
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

    private Lawyer getEntity() {
        var entity = TestData.getLawyer1();

        var task1 = TestData.getTask1();
        task1.setId(1L);
        var task2 = TestData.getTask2();
        task2.setLawyers(List.of(new Lawyer()));

        var task3 = TestData.getTask3();
        entity.setTasks(List.of(task1, task2, task3));

        return entity;
    }

    private Lawyer getEntityWithId() {
        var entity = getEntity();
        entity.setId(1L);
        return entity;
    }

    private List<LawyerDto> getDtoList() {
        return List.of(getDtoWithId(), getDtoWithId(), getDtoWithId(), getDtoWithId(), getDtoWithId());
    }

    private List<Lawyer> getEntityList() {
        return List.of(getEntityWithId(), getEntityWithId(), getEntityWithId(), getEntityWithId(), getEntityWithId());
    }
}