package com.mogilan.controller;

import com.mogilan.dto.TaskDto;
import com.mogilan.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/tasks/")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(taskService.readAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id){
        return ResponseEntity.ok(taskService.readById(id));
    }

    @PostMapping()
    public ResponseEntity<?> save(@RequestBody TaskDto newDto) throws URISyntaxException {
        var createdDto = taskService.create(newDto);
        URI uri = new URI("/api/clients/" + createdDto.getId());
        return ResponseEntity.created(uri).body(createdDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody TaskDto dto) {
        taskService.update(id, dto);
        return ResponseEntity.ok("Updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id){
        taskService.deleteById(id);
        return ResponseEntity.ok("Deleted");
    }
}
