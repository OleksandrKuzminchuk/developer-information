package com.sasha.jdbccrud.controller;

import com.sasha.jdbccrud.model.Specialty;
import com.sasha.jdbccrud.service.SpecialtyService;

import java.util.List;

import static com.sasha.jdbccrud.util.constant.Constants.*;

public class SpecialtyController {
    private final SpecialtyService service;

    public SpecialtyController(SpecialtyService service) {
        this.service = service;
    }

    public Specialty save(Specialty specialty) {
        return service.save(specialty);
    }

    public Specialty update(Specialty specialty) {
        return service.update(specialty);
    }

    public Specialty findById(Integer id) {
        return service.findById(id);
    }

    public List<Specialty> findAll() {
        return service.findAll();
    }

    public String deleteById(Integer id) {
        service.deleteById(id);
        return RESPONSE_OK;
    }

    public String deleteAll() {
        service.deleteAll();
        return RESPONSE_OK;
    }
}
