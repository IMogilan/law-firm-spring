package com.mogilan.controller;

import com.mogilan.dto.LawyerDto;
import com.mogilan.service.LawyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/lawyers/")
public class LawyerController {

    private final LawyerService lawyerService;
    public static final String UPDATED_MESSAGE = "Updated";
    public static final String DELETED_MESSAGE = "Deleted";

    @Autowired
    public LawyerController(LawyerService lawyerService) {
        this.lawyerService = lawyerService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(lawyerService.readAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id){
        return ResponseEntity.ok(lawyerService.readById(id));
    }

    @PostMapping()
    public ResponseEntity<?> save(@RequestBody LawyerDto newDto) throws URISyntaxException {
        var createdDto = lawyerService.create(newDto);
        URI uri = new URI("/api/clients/" + createdDto.getId());
        return ResponseEntity.created(uri).body(createdDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody LawyerDto dto) {
        lawyerService.update(id, dto);
        return ResponseEntity.ok(UPDATED_MESSAGE);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id){
        lawyerService.deleteById(id);
        return ResponseEntity.ok(DELETED_MESSAGE);
    }
}
