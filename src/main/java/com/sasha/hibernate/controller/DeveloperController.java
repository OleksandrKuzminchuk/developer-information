package com.sasha.hibernate.controller;

import com.sasha.hibernate.service.DeveloperService;
import com.sasha.hibernate.util.constant.Constants;
import com.sasha.hibernate.pojo.Developer;
import com.sasha.hibernate.pojo.Skill;

import java.util.List;

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
        return Constants.RESPONSE_OK;
    }

    public String deleteAll() {
        service.deleteAll();
        return Constants.RESPONSE_OK;
    }

    public List<Skill> findSkillsByDeveloperId(Integer id) {
        return service.findSkillsByDeveloperId(id);
    }
}
