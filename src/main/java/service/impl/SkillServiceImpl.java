package service.impl;

import exception.NotFoundException;
import model.Skill;
import org.apache.commons.lang3.NotImplementedException;
import repository.SkillRepository;
import service.SkillService;

import java.util.List;

import static util.constant.Constants.NOT_FOUND_SKILL;

public class SkillServiceImpl implements SkillService {
    private final SkillRepository repository;
    public SkillServiceImpl(SkillRepository repository) {
        this.repository = repository;
    }

    @Override
    public Skill save(Skill skill) {
        return repository.save(skill);
    }

    @Override
    public void saveAll(List<Skill> entities) {
        throw new NotImplementedException();
    }

    @Override
    public Skill update(Skill skill) {
        isExistsSkill(skill.getId());
        return repository.update(skill);
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
        return repository.findAll();
    }

    @Override
    public Long count() {
        throw new NotImplementedException();
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

    /*
        The method 'isExistsSkill' check exists the skill by id if not then throw NotFoundException
     */
    protected void isExistsSkill(Integer id){
        if (!existsById(id))
            throw new NotFoundException(NOT_FOUND_SKILL);
    }
}
