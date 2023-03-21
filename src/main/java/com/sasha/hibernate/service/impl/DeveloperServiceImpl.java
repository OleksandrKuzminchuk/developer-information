package com.sasha.hibernate.service.impl;

import com.sasha.hibernate.exception.NotFoundException;
import com.sasha.hibernate.pojo.Developer;
import com.sasha.hibernate.pojo.Skill;
import com.sasha.hibernate.pojo.Status;
import com.sasha.hibernate.repository.DeveloperRepository;
import com.sasha.hibernate.service.DeveloperService;
import com.sasha.hibernate.util.constant.Constants;

import java.util.List;

public class DeveloperServiceImpl implements DeveloperService {
    private final DeveloperRepository repository;

    public DeveloperServiceImpl(DeveloperRepository repository) {
        this.repository = repository;
    }

    @Override
    public Developer save(Developer developer) {
        return repository.save(developer).orElseThrow(() -> new NotFoundException(Constants.FAILED_TO_SAVE_DEVELOPER));
    }

    @Override
    public Developer update(Developer developer) {
        return repository.update(developer).orElseThrow(() -> new NotFoundException(Constants.FAILED_TO_UPDATE_DEVELOPER));
    }

    @Override
    public Developer findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(Constants.NOT_FOUND_DEVELOPER));
    }

    @Override
    public List<Developer> findAll() {
        return repository.findAll().orElseThrow(() -> new NotFoundException(Constants.FAILED_TO_FIND_ALL_DEVELOPERS));
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
            throw new NotFoundException(Constants.NOT_FOUND_DEVELOPER);
        }
    }
}
