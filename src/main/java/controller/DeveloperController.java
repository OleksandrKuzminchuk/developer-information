package controller;

import model.Developer;
import model.Skill;
import service.DeveloperService;

import java.util.List;

import static util.constant.Constants.*;

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

    public void addSkill(Integer developerId, Integer skillId) {
        service.addSkill(developerId, skillId);
    }

    public String deleteSkill(Integer developerId, Integer skillId) {
        service.deleteSkill(developerId, skillId);
        return RESPONSE_OK;
    }

    public void addSpecialty(Integer developerId, Integer specialityId) {
        service.addSpecialty(developerId, specialityId);
    }

    public String deleteSpecialty(Integer developerId) {
        service.deleteSpecialty(developerId);
        return RESPONSE_OK;
    }

    public List<Skill> findSkillsByDeveloperId(Integer id) {
        return service.findSkillsByDeveloperId(id);
    }
}
