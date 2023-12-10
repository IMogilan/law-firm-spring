package com.mogilan.service.impl;

import com.mogilan.dto.TaskDto;
import com.mogilan.mapper.TaskMapper;
import com.mogilan.model.Client;
import com.mogilan.model.Lawyer;
import com.mogilan.model.Task;
import com.mogilan.repository.ClientRepository;
import com.mogilan.repository.LawyerRepository;
import com.mogilan.repository.TaskRepository;
import com.mogilan.service.TaskService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final LawyerRepository lawyerRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper, LawyerRepository lawyerRepository, ClientRepository clientRepository) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.lawyerRepository = lawyerRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public TaskDto create(TaskDto newTaskDto) {
        Objects.requireNonNull(newTaskDto);

        var task = taskMapper.toEntity(newTaskDto);
        var client = task.getClient();
        populateClientWithTasks(client, task);
        var lawyers = task.getLawyers();
        populateLawyersWithTasks(lawyers, task);

        var savedTask = taskRepository.save(task);

        return taskMapper.toDto(savedTask);
    }

    @Override
    public List<TaskDto> readAll() {
        return taskRepository.findAll().stream()
                .map(taskMapper::toDto)
                .toList();
    }


    @Override
    public TaskDto readById(Long id) {
        Objects.requireNonNull(id);

        var task = taskRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Task with id = " + id + " not found"));
        return taskMapper.toDto(task);
    }

    @Override
    public void update(Long id, TaskDto taskDto) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(taskDto);
        if (!taskRepository.existsById(id)) {
            throw new EntityNotFoundException("Task with id = " + id + " not found");
        }

        var task = taskMapper.toEntity(taskDto);
        task.setId(id);

        var client = task.getClient();
        populateClientWithTasks(client, task);
        var lawyers = task.getLawyers();
        populateLawyersWithTasks(lawyers, task);

        taskRepository.save(task);
    }

    @Override
    public void deleteById(Long id) {
        Objects.requireNonNull(id);

        if (!taskRepository.existsById(id)) {
            throw new EntityNotFoundException("Task with id = " + id + " not found");
        }
        taskRepository.deleteById(id);
    }

    private void populateClientWithTasks(Client client, Task currentTask) {
        if (client != null) {
            if(client.getId() == null){
                client.setTasks(List.of(currentTask));
            } else {
                clientRepository.findById(client.getId())
                        .ifPresent(existing -> {
                            List<Task> taskList = existing.getTasks() != null ?
                                    new LinkedList<>(existing.getTasks()) :
                                    new LinkedList<>();
                            taskList.add(currentTask);
                            client.setTasks(taskList);
                        });
            }
        }
    }

    private void populateLawyersWithTasks(List<Lawyer> lawyers, Task currentTask) {
        lawyers.stream()
                .filter(lawyer -> lawyer.getId() == null)
                .forEach(lawyer -> lawyer.setTasks(List.of(currentTask)));

        lawyers.stream()
                .filter(lawyer -> lawyer.getId() != null)
                .forEach(lawyer -> lawyerRepository.findById(lawyer.getId())
                        .ifPresent(existing -> {
                            List<Task> taskList = existing.getTasks() != null ?
                                    new LinkedList<>(existing.getTasks()) :
                                    new LinkedList<>();
                            taskList.add(currentTask);
                            lawyer.setTasks(taskList);
                        }));
    }
}
