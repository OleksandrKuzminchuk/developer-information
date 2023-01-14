package controller;

import model.Developer;
import model.Skill;
import model.Status;
import repository.DeveloperRepository;
import repository.SkillRepository;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static repository.ParametrizeMethodsCrud.cleanFile;
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
        List<Developer> developers = getDevelopers();
        developers.forEach(getConsumerDeleteSkillIfActiveSetStatusDeletedIfDeleted(id));
        cleanFile(new File(FILE_DEVELOPERS_PATH));
        developers.forEach(developerRepository::save);
        return RESPONSE_OK;
    }

    public String deleteAll() {
        repository.deleteAll();
        List<Developer> developers = getDevelopers();
        developers.forEach(getConsumerDeleteAllSkillsActiveAndAllSetStatusDeletedIfDeleted());
        cleanFile(new File(FILE_DEVELOPERS_PATH));
        developers.forEach(developerRepository::save);
        return RESPONSE_OK;
    }

    private static void assignStatusActiveIfNull(Skill skill) {
        if (skill.getStatus() == null) {
            skill.setStatus(Status.ACTIVE);
        }
    }
    private List<Developer> getDevelopers(){
        return developerRepository.findAll();
    }
    private Consumer<Developer> getConsumerDeleteSkillIfActiveSetStatusDeletedIfDeleted(Integer skillId){
        Predicate<Skill> setStatusDeleteIf = skill -> skill.getId().equals(skillId);
        Consumer<Skill> setStatusDelete = skill -> {
            if (skill.getId().equals(skillId))
                skill.setStatus(Status.DELETED);
        };
        return dev -> {
            if (dev.getStatus().equals(Status.ACTIVE))
                dev.getSkills().removeIf(setStatusDeleteIf);
            dev.getSkills().forEach(setStatusDelete);
        };
    }
    private Consumer<Developer> getConsumerDeleteAllSkillsActiveAndAllSetStatusDeletedIfDeleted(){
        Consumer<Skill> setStatusDelete = skill -> skill.setStatus(Status.DELETED);
        return dev -> {
            if (dev.getStatus().equals(Status.ACTIVE))
                dev.getSkills().removeAll(dev.getSkills());
            dev.getSkills().forEach(setStatusDelete);
        };
    }
}
