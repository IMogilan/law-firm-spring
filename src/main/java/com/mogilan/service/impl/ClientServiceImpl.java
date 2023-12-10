package com.mogilan.service.impl;

import com.mogilan.dto.ClientDto;
import com.mogilan.mapper.ClientMapper;
import com.mogilan.model.Client;
import com.mogilan.model.Task;
import com.mogilan.repository.ClientRepository;
import com.mogilan.repository.TaskRepository;
import com.mogilan.service.ClientService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final TaskRepository taskRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, ClientMapper clientMapper, TaskRepository taskRepository) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.taskRepository = taskRepository;
    }

    @Override
    public ClientDto create(ClientDto newClientDto) {
        Objects.requireNonNull(newClientDto);

        var client = clientMapper.toEntity(newClientDto);

        var tasks = client.getTasks();
        populateTasksWithClient(tasks, client);
        populateTasksWithLawyers(tasks);

        var savedClient = clientRepository.save(client);
        return clientMapper.toDto(savedClient);
    }


    @Override
    public List<ClientDto> readAll() {
        return clientRepository.findAll().stream()
                .map(clientMapper::toDto)
                .toList();
    }

    @Override
    public ClientDto readById(Long id) {
        Objects.requireNonNull(id);

        var client = clientRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Client with id = " + id + " not found"));
        return clientMapper.toDto(client);
    }

    @Override
    public void update(Long id, ClientDto clientDto) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(clientDto);
        if (!clientRepository.existsById(id)) {
            throw new EntityNotFoundException("Client with id = " + id + " not found");
        }

        var client = clientMapper.toEntity(clientDto);
        client.setId(id);

        var tasks = client.getTasks();
        populateTasksWithClient(tasks, client);
        populateTasksWithLawyers(tasks);

        clientRepository.save(client);
    }

    @Override
    public void deleteById(Long id) {
        Objects.requireNonNull(id);

        if (!clientRepository.existsById(id)) {
            throw new EntityNotFoundException("Client with id = " + id + " not found");
        }
        clientRepository.deleteById(id);
    }

    private void populateTasksWithClient(List<Task> tasks, Client client) {
        tasks.forEach(task -> task.setClient(client));
    }

    private void populateTasksWithLawyers(List<Task> tasks) {
        tasks.stream()
                .filter(task -> task.getId() != null)
                .forEach(task -> taskRepository.findById(task.getId())
                        .ifPresent(existing -> task.setLawyers(existing.getLawyers())));
    }
}
