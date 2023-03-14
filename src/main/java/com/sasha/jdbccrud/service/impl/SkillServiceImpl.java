package com.sasha.jdbccrud.service.impl;

import com.sasha.jdbccrud.exception.NotFoundException;
import com.sasha.jdbccrud.model.Skill;
import org.apache.commons.lang3.NotImplementedException;
import com.sasha.jdbccrud.repository.SkillRepository;
import com.sasha.jdbccrud.service.SkillService;

import java.util.List;

import static com.sasha.jdbccrud.util.constant.Constants.*;

public class SkillServiceImpl implements SkillService {
    private final SkillRepository repository;
    public SkillServiceImpl(SkillRepository repository) {
        this.repository = repository;
    }

    @Override
    public Skill save(Skill skill) {
        return repository.save(skill).orElseThrow(() -> new NotFoundException(FAILED_TO_SAVE_SKILL));
    }

    @Override
    public void saveAll(List<Skill> entities) {
        throw new NotImplementedException();
    }

    @Override
    public Skill update(Skill skill) {
        isExistsSkill(skill.getId());
        return repository.update(skill).orElseThrow(() -> new NotFoundException(FAILED_TO_UPDATE_SKILL_BY_ID));
    }

    @Override
    public Skill findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(NOT_FOUND_SKILL));
    }

    @Override
    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    @Override
    public List<Skill> findAll() {
        return repository.findAll().orElseThrow(() -> new NotFoundException(FAILED_TO_FIND_ALL_SKILLS));
    }

    @Override
    public void deleteById(Integer id) {
        isExistsSkill(id);
        repository.deleteById(id);
    }

    @Override
    public void delete(Skill skill) {
        throw new NotImplementedException();
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    protected void isExistsSkill(Integer id){
        if (!existsById(id))
            throw new NotFoundException(NOT_FOUND_SKILL);
    }
}
