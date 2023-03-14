package com.sasha.jdbccrud.service.impl;

import com.sasha.jdbccrud.exception.NotFoundException;
import com.sasha.jdbccrud.model.Developer;
import com.sasha.jdbccrud.model.Skill;
import org.apache.commons.lang3.NotImplementedException;
import com.sasha.jdbccrud.repository.DeveloperRepository;
import com.sasha.jdbccrud.repository.SkillRepository;
import com.sasha.jdbccrud.repository.SpecialtyRepository;
import com.sasha.jdbccrud.service.DeveloperService;

import java.util.List;

import static com.sasha.jdbccrud.util.constant.Constants.*;

public class DeveloperServiceImpl implements DeveloperService {
    private final DeveloperRepository repository;
    private final SpecialtyRepository specialtyRepository;
    private final SkillRepository skillRepository;

    public DeveloperServiceImpl(DeveloperRepository repository, SpecialtyRepository specialtyRepository, SkillRepository skillRepository) {
        this.repository = repository;
        this.specialtyRepository = specialtyRepository;
        this.skillRepository = skillRepository;
    }

    @Override
    public Developer save(Developer developer) {
        isExistsSpecialty(developer);
        isExistsSkill(developer);
        return repository.save(developer).orElseThrow(() -> new NotFoundException(FAILED_TO_SAVE_DEVELOPER));
    }

    @Override
    public void saveAll(List<Developer> developers) {
        throw new NotImplementedException();
    }

    @Override
    public Developer update(Developer developer) {
        isExistsSpecialty(developer);
        isExistsSkill(developer);
        return repository.update(developer).orElseThrow(() -> new NotFoundException(FAILED_TO_UPDATE_DEVELOPER));
    }

    @Override
    public Developer findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(NOT_FOUND_DEVELOPER));
    }

    @Override
    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    @Override
    public List<Developer> findAll() {
        return repository.findAll().orElseThrow(() -> new NotFoundException(FAILED_TO_FIND_ALL_DEVELOPERS));
    }

    @Override
    public void deleteById(Integer id) {
        isExistsDeveloper(id);
        repository.deleteById(id);
    }

    @Override
    public void delete(Developer developer) {
        throw new NotImplementedException();
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public List<Skill> findSkillsByDeveloperId(Integer id) {
        return findById(id).getSkills();
    }

    private void isExistsDeveloper(Developer developer) {
        if (!existsById(developer.getId()))
            throw new NotFoundException(NOT_FOUND_DEVELOPER);
    }

    private void isExistsDeveloper(Integer developerId) {
        if (!existsById(developerId))
            throw new NotFoundException(NOT_FOUND_DEVELOPER);
    }

    private void isExistsSpecialty(Developer developer) {
        if (developer.getSpecialty() != null &&
                !specialtyRepository.existsById(developer.getSpecialty().getId()))
            throw new NotFoundException(NOT_FOUND_SPECIALITY);
    }

    private void isExistsSkill(Developer developer) {
        if (developer.getSkills() != null) {
            developer.getSkills().forEach(skill -> {
                        if (!skillRepository.existsById(skill.getId())) {
                            throw new NotFoundException(NOT_FOUND_SKILL);
                        }
                    }
            );
        }
    }

    private boolean isHasDeveloperSkill(Integer developerId, Integer skillId) {
        Developer developer = findById(developerId);
        if (developer.getSkills() == null) {
            return true;
        }
        developer.getSkills().forEach(skill -> {
            if (skill.getId().equals(skillId)) {
                throw new NotFoundException(NOT_FOUND_SKILL);
            }
        });
        return true;
    }
}
