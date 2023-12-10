package com.mogilan.controller;

import com.mogilan.dto.ClientDto;
import com.mogilan.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/clients/")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(clientService.readAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id){
        return ResponseEntity.ok(clientService.readById(id));
    }

    @PostMapping()
    public ResponseEntity<?> save(@RequestBody ClientDto newDto) throws URISyntaxException {
        var createdDto = clientService.create(newDto);
        URI uri = new URI("/api/clients/" + createdDto.getId());
        return ResponseEntity.created(uri).body(createdDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody ClientDto dto) {
        clientService.update(id, dto);
        return ResponseEntity.ok("Updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id){
        clientService.deleteById(id);
        return ResponseEntity.ok("Deleted");
    }

}
