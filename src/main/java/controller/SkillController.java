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
import static java.util.Objects.requireNonNull;
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
    public Skill save(Skill skill) throws IllegalArgumentException {
        requireNonNull(skill);
        isExistsSkillIntoFileById(skill.getId());
        assignStatusActiveIfNull(skill);
        return repository.save(skill);
    }
    public Skill findById(Integer id){
        requireNonNull(id);
        return repository.findById(id);
    }
    public Skill update(Skill skill){
        requireNonNull(skill);
        repository.existsById(skill.getId());
        return repository.update(skill);
    }
    public List<Skill> findAll(){
        return repository.findAll();
    }
    public String deleteById(Integer id){
        requireNonNull(id);
        repository.existsById(id);
        repository.deleteById(id);
        List<Developer> developers = developerRepository.findAll();
        Predicate<Skill> removeIf = s -> s.getId().equals(id);
        Consumer<Developer> removeSkillById = dev -> dev.getSkills().removeIf(removeIf);
        developers.forEach(removeSkillById);
        cleanFile(new File(FILE_DEVELOPERS_PATH));
        developers.forEach(developerRepository::save);
        return RESPONSE_OK;
    }
    public String deleteAll(){
        repository.deleteAll();
        List<Developer> developers = developerRepository.findAll();
        developers.forEach(developer -> developer.getSkills().removeAll(developer.getSkills()));
        cleanFile(new File(FILE_DEVELOPERS_PATH));
        developers.forEach(developerRepository::save);
        return RESPONSE_OK;
    }
    private void isExistsSkillIntoFileById(Integer id) {
        if (file.length() != 0) {
            List<Skill> skills = findAll();
            if (skills.stream().anyMatch(skill -> skill.getId().equals(id)
                    && skill.getStatus().equals(Status.ACTIVE))) {
                throw new IllegalArgumentException(format("Skill has already taken by id - %d", id));
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
