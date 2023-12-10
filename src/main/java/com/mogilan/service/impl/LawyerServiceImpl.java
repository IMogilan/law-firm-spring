package com.mogilan.service.impl;

import com.mogilan.dto.LawyerDto;
import com.mogilan.mapper.LawyerMapper;
import com.mogilan.model.Lawyer;
import com.mogilan.model.Task;
import com.mogilan.repository.LawyerRepository;
import com.mogilan.repository.TaskRepository;
import com.mogilan.service.LawyerService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class LawyerServiceImpl implements LawyerService {
    private final LawyerRepository lawyerRepository;
    private final LawyerMapper lawyerMapper;
    private final TaskRepository taskRepository;

    @Autowired
    public LawyerServiceImpl(LawyerRepository lawyerRepository, LawyerMapper lawyerMapper, TaskRepository taskRepository) {
        this.lawyerRepository = lawyerRepository;
        this.lawyerMapper = lawyerMapper;
        this.taskRepository = taskRepository;
    }

    @Override
    public LawyerDto create(LawyerDto lawyerDto) {
        Objects.requireNonNull(lawyerDto);

        var lawyer = lawyerMapper.toEntity(lawyerDto);

        var tasks = lawyer.getTasks();
        populateTasksWithClient(tasks);
        populateTasksWithLawyers(tasks, lawyer);

        var savedLawyer = lawyerRepository.save(lawyer);

        return lawyerMapper.toDto(savedLawyer);
    }

    @Override
    public List<LawyerDto> readAll() {
        return lawyerRepository.findAll().stream()
                .map(lawyerMapper::toDto)
                .toList();
    }

    @Override
    public LawyerDto readById(Long id) {
        Objects.requireNonNull(id);

        var lawyer = lawyerRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Lawyer with id = " + id + " not found"));
        return lawyerMapper.toDto(lawyer);
    }

    @Override
    public void update(Long id, LawyerDto lawyerDto) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(lawyerDto);
        if (!lawyerRepository.existsById(id)) {
            throw new EntityNotFoundException("Lawyer with id = " + id + " not found");
        }

        var lawyer = lawyerMapper.toEntity(lawyerDto);
        lawyer.setId(id);

        var tasks = lawyer.getTasks();
        populateTasksWithClient(tasks);
        populateTasksWithLawyers(tasks, lawyer);

        lawyerRepository.save(lawyer);
    }

    @Override
    public void deleteById(Long id) {
        Objects.requireNonNull(id);

        if (!lawyerRepository.existsById(id)) {
            throw new EntityNotFoundException("Lawyer with id = " + id + " not found");
        }
        lawyerRepository.deleteById(id);
    }

    private void populateTasksWithClient(List<Task> tasks) {
        tasks.stream()
                .filter(task -> task.getId() != null)
                .forEach(task -> taskRepository.findById(task.getId())
                        .ifPresent(existing -> task.setClient(existing.getClient())));
    }

    private void populateTasksWithLawyers(List<Task> tasks, Lawyer currentLawyer) {
        tasks.stream()
                .filter(task -> task.getId() == null)
                .forEach(task -> task.setLawyers(List.of(currentLawyer)));

        tasks.stream()
                .filter(task -> task.getId() != null)
                .forEach(task -> taskRepository.findById(task.getId())
                        .ifPresent(existing -> {
                            List<Lawyer> lawyerList = existing.getLawyers() != null ?
                                    new LinkedList<>(existing.getLawyers()) :
                                    new LinkedList<>();
                            lawyerList.add(currentLawyer);
                            task.setLawyers(lawyerList);
                        }));
    }
}
