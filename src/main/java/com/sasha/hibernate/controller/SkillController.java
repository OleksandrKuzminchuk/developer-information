package com.sasha.hibernate.controller;

import com.sasha.hibernate.pojo.Skill;
import com.sasha.hibernate.service.SkillService;
import com.sasha.hibernate.util.constant.Constants;

import java.util.List;

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
        return Constants.RESPONSE_OK;
    }

    public String deleteAll() {
        service.deleteAll();
        return Constants.RESPONSE_OK;
    }
}
