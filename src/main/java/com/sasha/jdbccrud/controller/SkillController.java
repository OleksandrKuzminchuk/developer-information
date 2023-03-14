package com.sasha.jdbccrud.controller;

import com.sasha.jdbccrud.model.Skill;
import com.sasha.jdbccrud.service.SkillService;

import java.util.List;

import static com.sasha.jdbccrud.util.constant.Constants.*;

public class SkillController {
    private final SkillService service;

    public SkillController(SkillService service) {
        this.service = service;
    }

    public Skill save(Skill skill) {
        return service.save(skill);
    }

    public Skill findById(Integer id) {
        return service.findById(id);
    }

    public Skill update(Skill skill) {
        return service.update(skill);
    }

    public List<Skill> findAll() {
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
