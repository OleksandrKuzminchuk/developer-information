package controller;

import model.Developer;
import model.Skill;
import model.Specialty;
import repository.DeveloperRepository;
import repository.SkillRepository;
import repository.SpecialtyRepository;

import java.util.List;

import static util.Constants.*;

public class DeveloperController {
    private final DeveloperRepository repository;
    private final SkillRepository skillRepository;
    private final SpecialtyRepository specialtyRepository;

    public DeveloperController(DeveloperRepository developerRepository, SkillRepository skillRepository, SpecialtyRepository specialtyRepository) {
        this.repository = developerRepository;
        this.skillRepository = skillRepository;
        this.specialtyRepository = specialtyRepository;
    }

    public Developer save(Developer developer) {
        return repository.save(developer);
    }

    public Developer findById(Integer id) {
        return repository.findById(id);
    }

    public List<Developer> findAll() {
        return repository.findAll();
    }

    public Developer update(Developer developer) {
        repository.existsById(developer.getId());
        return repository.update(developer);
    }

    public String deleteById(Integer id) {
        repository.existsById(id);
        repository.deleteById(id);
        return RESPONSE_OK;
    }

    public String deleteAll() {
        repository.deleteAll();
        return RESPONSE_OK;
    }

    public Skill addSkill(Integer developerId, Integer skillId) {
        repository.existsById(developerId);
        Skill skill = skillRepository.findById(skillId);
        return repository.addSkill(developerId, skill);
    }

    public String deleteSkill(Integer developerId, Integer skillId) {
        repository.existsById(developerId);
        skillRepository.existsById(skillId);
        repository.deleteSkill(developerId, skillId);
        return RESPONSE_OK;
    }

    public Specialty addSpecialty(Integer developerId, Integer specialityId) {
        repository.existsById(developerId);
        Specialty specialty = specialtyRepository.findById(specialityId);
        return repository.addSpecialty(developerId, specialty);
    }

    public String deleteSpecialty(Integer developerId, Integer specialityId) {
        repository.existsById(developerId);
        specialtyRepository.existsById(specialityId);
        repository.deleteSpecialty(developerId, specialityId);
        return RESPONSE_OK;
    }

    public List<Skill> findSkillsByDeveloperId(Integer id) {
        return repository.findSkillsByDeveloperId(id);
    }
}
