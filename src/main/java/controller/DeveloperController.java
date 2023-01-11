package controller;

import exception.NotValidException;
import model.Developer;
import model.Skill;
import model.Specialty;
import model.Status;
import repository.DeveloperRepository;
import repository.SkillRepository;
import repository.SpecialtyRepository;

import java.io.File;
import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static util.Constants.*;

public class DeveloperController {
    private final File file;
    private final DeveloperRepository repository;
    private final SkillRepository skillRepository;
    private final SpecialtyRepository specialtyRepository;

    public DeveloperController(DeveloperRepository developerRepository, SkillRepository skillRepository, SpecialtyRepository specialtyRepository) {
        this.repository = developerRepository;
        this.skillRepository = skillRepository;
        this.specialtyRepository = specialtyRepository;
        this.file = new File(FILE_DEVELOPERS_PATH);
    }

    public Developer save(Developer developer) throws IllegalArgumentException {
        requireNonNull(developer);
        isExistsDeveloperIntoFileById(developer.getId());
        assignStatusActiveIfNull(developer);
        return repository.save(developer);
    }

    public Developer findById(Integer id) {
        requireNonNull(id);
        return repository.findById(id);
    }

    public List<Developer> findAll() {
        return repository.findAll();
    }

    public Developer update(Developer developer) {
        requireNonNull(developer);
        repository.existsById(developer.getId());
        return repository.update(developer);
    }

    public String deleteById(Integer id) {
        requireNonNull(id);
        repository.existsById(id);
        repository.deleteById(id);
        return RESPONSE_OK;
    }

    public String deleteAll() {
        repository.deleteAll();
        return RESPONSE_OK;
    }

    public Skill addSkill(Integer developerId, Integer skillId) throws NotValidException {
        requireNonNull(skillId);
        requireNonNull(developerId);
        repository.existsById(developerId);
        Skill skill = skillRepository.findById(skillId);
        return repository.addSkill(developerId, skill);
    }

    public String deleteSkill(Integer developerId, Integer skillId) throws NotValidException {
        requireNonNull(developerId);
        requireNonNull(skillId);
        repository.existsById(developerId);
        skillRepository.existsById(skillId);
        repository.deleteSkill(developerId, skillId);
        return RESPONSE_OK;
    }

    public Specialty addSpecialty(Integer developerId, Integer specialityId) throws NotValidException {
        requireNonNull(developerId);
        requireNonNull(specialityId);
        repository.existsById(developerId);
        Specialty specialty = specialtyRepository.findById(specialityId);
        return repository.addSpecialty(developerId, specialty);
    }

    public String deleteSpecialty(Integer developerId, Integer specialityId) throws NotValidException {
        requireNonNull(developerId);
        requireNonNull(specialityId);
        repository.existsById(developerId);
        specialtyRepository.existsById(specialityId);
        repository.deleteSpecialty(developerId, specialityId);
        return RESPONSE_OK;
    }

    public List<Skill> findSkillsByDeveloperId(Integer id) {
        return repository.findSkillsByDeveloperId(id);
    }

    private void isExistsDeveloperIntoFileById(Integer id) {
        if (file.length() != 0) {
            List<Developer> developers = findAll();
            if (developers.stream().anyMatch(developer -> developer.getId().equals(id)
                    && developer.getStatus().equals(Status.ACTIVE))) {
                throw new IllegalArgumentException(format("Developer has already taken by id - %d", id));
            } else if (developers.stream().anyMatch(developer -> developer.getId().equals(id)
                    && developer.getStatus().equals(Status.DELETED))) {
                throw new IllegalArgumentException(NOT_FOUND_DEVELOPER);
            }
        }
    }
    private static void assignStatusActiveIfNull(Developer developer) {
        if (developer.getStatus() == null) {
            developer.setStatus(Status.ACTIVE);
        }
    }
}
