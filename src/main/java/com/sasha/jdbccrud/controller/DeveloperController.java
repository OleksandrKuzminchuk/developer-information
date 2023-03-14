package com.sasha.jdbccrud.controller;

import com.sasha.jdbccrud.model.Developer;
import com.sasha.jdbccrud.model.Skill;
import com.sasha.jdbccrud.service.DeveloperService;

import java.util.List;

import static com.sasha.jdbccrud.util.constant.Constants.*;

public class DeveloperController {
    private final DeveloperService service;

    public DeveloperController(DeveloperService service) {
        this.service = service;
    }

    public Developer save(Developer developer) {
        return service.save(developer);
    }

    public Developer findById(Integer id) {
        return service.findById(id);
    }

    public List<Developer> findAll() {
        return service.findAll();
    }

    public Developer update(Developer developer) {
        return service.update(developer);
    }

    public String deleteById(Integer id) {
        service.deleteById(id);
        return RESPONSE_OK;
    }

    public String deleteAll() {
        service.deleteAll();
        return RESPONSE_OK;
    }

    public List<Skill> findSkillsByDeveloperId(Integer id) {
        return service.findSkillsByDeveloperId(id);
    }
}
