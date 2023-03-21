package com.sasha.hibernate.controller;

import com.sasha.hibernate.pojo.Specialty;
import com.sasha.hibernate.service.SpecialtyService;
import com.sasha.hibernate.util.constant.Constants;

import java.util.List;

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
        return Constants.RESPONSE_OK;
    }

    public String deleteAll() {
        service.deleteAll();
        return Constants.RESPONSE_OK;
    }
}
