package com.mogilan.service;

import java.util.List;

public interface CrudService<Dto, Id> {
    Dto create(Dto newDto);

    List<Dto> readAll();

    Dto readById(Id id);

    void update(Id id, Dto dto);

    void deleteById(Id id);
}
