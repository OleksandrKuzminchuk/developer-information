package com.sasha.jdbccrud.service.impl;

import com.sasha.jdbccrud.exception.NotFoundException;
import com.sasha.jdbccrud.model.Developer;
import com.sasha.jdbccrud.model.Skill;
import com.sasha.jdbccrud.model.Status;
import com.sasha.jdbccrud.repository.DeveloperRepository;
import com.sasha.jdbccrud.service.DeveloperService;

import java.util.List;

import static com.sasha.jdbccrud.util.constant.Constants.*;

public class DeveloperServiceImpl implements DeveloperService {
    private final DeveloperRepository repository;

    public DeveloperServiceImpl(DeveloperRepository repository) {
        this.repository = repository;
    }

    @Override
    public Developer save(Developer developer) {
        return repository.save(developer).orElseThrow(() -> new NotFoundException(FAILED_TO_SAVE_DEVELOPER));
    }

    @Override
    public Developer update(Developer developer) {
        return repository.update(developer).orElseThrow(() -> new NotFoundException(FAILED_TO_UPDATE_DEVELOPER));
    }

    @Override
    public Developer findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(NOT_FOUND_DEVELOPER));
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
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public List<Skill> findSkillsByDeveloperId(Integer id) {
        return findById(id).getSkills();
    }

    private void isExistsDeveloper(Integer id) {
        if(!findById(id).getStatus().equals(Status.ACTIVE)){
            throw new NotFoundException(NOT_FOUND_DEVELOPER);
        }
    }
}
