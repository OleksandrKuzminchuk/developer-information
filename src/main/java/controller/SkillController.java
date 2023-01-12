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

import static java.lang.String.format;
import static repository.ParametrizeMethodsCrud.cleanFile;
import static util.Constants.*;

public class SkillController {
    private final File file;
    private final SkillRepository repository;
    private final DeveloperRepository developerRepository;

    public SkillController(SkillRepository skillRepository, DeveloperRepository developerRepository) {
        this.repository = skillRepository;
        this.developerRepository = developerRepository;
        this.file = new File(FILE_SKILLS_PATH);
    }

    public Skill save(Skill skill) {
        isExistsSkillIntoFileById(skill.getId());
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
        List<Developer> developers = developerRepository.findAll();
        Predicate<Skill> setStatusDeleteIf = s -> s.getId().equals(id);
        Consumer<Skill> setStatusDelete = skill -> {
            if (skill.getId().equals(id))
                skill.setStatus(Status.DELETED);
        };
        Consumer<Developer> setSkillStatusDeleteById = dev -> {
            if (dev.getStatus().equals(Status.ACTIVE))
                dev.getSkills().removeIf(setStatusDeleteIf);
            dev.getSkills().forEach(setStatusDelete);
        };
        developers.forEach(setSkillStatusDeleteById);
        cleanFile(new File(FILE_DEVELOPERS_PATH));
        developers.forEach(developerRepository::save);
        return RESPONSE_OK;
    }

    public String deleteAll() {
        repository.deleteAll();
        List<Developer> developers = developerRepository.findAll();
        Consumer<Skill> setStatusDelete = skill -> skill.setStatus(Status.DELETED);
        Consumer<Developer> setSkillStatusDeleteById = dev -> {
            if (dev.getStatus().equals(Status.ACTIVE))
                dev.getSkills().removeAll(dev.getSkills());
            dev.getSkills().forEach(setStatusDelete);
        };
        developers.forEach(setSkillStatusDeleteById);
        cleanFile(new File(FILE_DEVELOPERS_PATH));
        developers.forEach(developerRepository::save);
        return RESPONSE_OK;
    }
    private void isExistsSkillIntoFileById(Integer id) {
        if (file.length() != 0) {
            List<Skill> skills = findAll();
            if (skills.stream().anyMatch(skill -> skill.getId().equals(id)
                    && skill.getStatus().equals(Status.ACTIVE))) {
                throw new IllegalArgumentException(format(EXCEPTION_SKILL_HAS_ALREADY_TAKEN, id));
            } else if (skills.stream().anyMatch(developer -> developer.getStatus().equals(Status.DELETED)
                    && developer.getId().equals(id))) {
                throw new IllegalArgumentException(NOT_FOUND_SKILL);
            }
        }
    }
    private static void assignStatusActiveIfNull(Skill skill) {
        if (skill.getStatus() == null) {
            skill.setStatus(Status.ACTIVE);
        }
    }
}
