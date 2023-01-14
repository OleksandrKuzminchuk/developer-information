package controller;

import model.Skill;
import model.Status;
import repository.DeveloperRepository;
import repository.SkillRepository;

import java.util.List;

import static util.Constants.*;

public class SkillController {
    private final SkillRepository repository;
    private final DeveloperRepository developerRepository;

    public SkillController(SkillRepository skillRepository, DeveloperRepository developerRepository) {
        this.repository = skillRepository;
        this.developerRepository = developerRepository;
    }

    public Skill save(Skill skill) {
        assignStatusActiveIfNull(skill);
        return repository.save(skill);
    }

    public Skill findById(Integer id) {
        return repository.findById(id);
    }

    public Skill update(Skill skill) {
        repository.existsById(skill.getId());
        return repository.update(skill);
    }

    public List<Skill> findAll() {
        return repository.findAll();
    }

    public String deleteById(Integer id) {
        repository.existsById(id);
        repository.deleteById(id);
        developerRepository.deleteSkillByIdIfActiveSetStatusDeletedIfDeleted(id);
        return RESPONSE_OK;
    }

    public String deleteAll() {
        repository.deleteAll();
        developerRepository.deleteAllSkillsActiveAndAllSetStatusDeletedIfDeleted();
        return RESPONSE_OK;
    }

    private static void assignStatusActiveIfNull(Skill skill) {
        if (skill.getStatus() == null) {
            skill.setStatus(Status.ACTIVE);
        }
    }
}
