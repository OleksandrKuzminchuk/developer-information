package controller;

import model.Skill;
import repository.DeveloperRepository;
import service.DeveloperService;
import service.SkillService;

import java.util.List;

import static util.constant.Constants.*;

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
//        developerService.deleteSkillByIdIfActiveOrSetStatusDeletedIfDeleted(id);
        return RESPONSE_OK;
    }

    public String deleteAll() {
        service.deleteAll();
//        developerService.deleteAllSkillsActiveOrAllSetStatusDeletedIfDeleted();
        return RESPONSE_OK;
    }
}
