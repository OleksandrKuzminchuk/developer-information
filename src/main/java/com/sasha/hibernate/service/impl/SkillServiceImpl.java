package com.sasha.hibernate.service.impl;

import com.sasha.hibernate.exception.NotFoundException;
import com.sasha.hibernate.pojo.Skill;
import com.sasha.hibernate.pojo.Status;
import com.sasha.hibernate.repository.SkillRepository;
import com.sasha.hibernate.service.SkillService;
import com.sasha.hibernate.util.constant.Constants;

import java.util.List;

public class SkillServiceImpl implements SkillService {
    private final SkillRepository repository;
    public SkillServiceImpl(SkillRepository repository) {
        this.repository = repository;
    }

    @Override
    public Skill save(Skill skill) {
        return repository.save(skill).orElseThrow(() -> new NotFoundException(Constants.FAILED_TO_SAVE_SKILL));
    }

    @Override
    public Skill update(Skill skill) {
        isExistsSkill(skill.getId());
        return repository.update(skill).orElseThrow(() -> new NotFoundException(Constants.FAILED_TO_UPDATE_SKILL_BY_ID));
    }

    @Override
    public Skill findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(Constants.NOT_FOUND_SKILL));
    }

    @Override
    public List<Skill> findAll() {
        return repository.findAll().orElseThrow(() -> new NotFoundException(Constants.FAILED_TO_FIND_ALL_SKILLS));
    }

    @Override
    public void deleteById(Integer id) {
        isExistsSkill(id);
        repository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    protected void isExistsSkill(Integer id){
        if(!findById(id).getStatus().equals(Status.ACTIVE)){
            throw new NotFoundException(Constants.NOT_FOUND_SKILL);
        }
    }
}
