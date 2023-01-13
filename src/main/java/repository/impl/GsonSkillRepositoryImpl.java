package repository.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Skill;
import model.Status;
import org.apache.commons.lang3.NotImplementedException;
import repository.ParametrizeMethodsCrud;
import repository.SkillRepository;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static repository.ParametrizeMethodsCrud.cleanFile;
import static util.Constants.*;

public class GsonSkillRepositoryImpl implements SkillRepository {
    private final File file;
    private final Gson gson;
    private final Type collectionType;
    public GsonSkillRepositoryImpl() {
        this.file = new File(FILE_SKILLS_PATH);
        this.gson = new Gson();
        this.collectionType = new TypeToken<List<Skill>>() {}.getType();
    }

    @Override
    public Skill save(Skill skill) {
        List<Skill> skills = findAll();
        ParametrizeMethodsCrud.save(skill, skills, file, gson, collectionType);
        return skill;
    }

    @Override
    public Skill findById(Integer id) {
        List<Skill> skills = findAll();
        Predicate<Skill> findSkill = skill -> skill.getId().equals(id)
                && skill.getStatus().equals(Status.ACTIVE);
        return ParametrizeMethodsCrud.findById(findSkill, skills, NOT_FOUND_SKILL);
    }

    @Override
    public Skill update(Skill skill) {
        List<Skill> skills = findAll();
        Predicate<Skill> findSkill = s -> s.getId().equals(skill.getId());
        Consumer<Skill> setName = s -> {
            if (skill.getName() != null && !skill.getName().equals(s.getName()))
                s.setName(skill.getName());
        };
        ParametrizeMethodsCrud.update(skills, findSkill, setName);
        cleanFile(file);
        skills.forEach(this::save);
        return skill;
    }

    @Override
    public boolean existsById(Integer id) {
        return findById(id) != null;
    }

    @Override
    public List<Skill> findAll() {
        return ParametrizeMethodsCrud.findAll(file, gson, collectionType);
    }

    @Override
    public Long count() {
        throw new NotImplementedException(NOT_IMPLEMENTED_COUNT);
    }

    @Override
    public void deleteById(Integer id) {
        List<Skill> skills = findAll();
        Predicate<Skill> findSkill = skill -> skill.getId().equals(id) && skill.getStatus().equals(Status.ACTIVE);
        Consumer<Skill> setStatusDeleted = skill -> skill.setStatus(Status.DELETED);
        ParametrizeMethodsCrud.deleteById(skills, findSkill, setStatusDeleted);
        cleanFile(file);
        skills.forEach(this::save);
     }

    @Override
    public void delete(Skill skill) {
        throw new NotImplementedException(NOT_IMPLEMENTED_DELETE);
    }

    @Override
    public void deleteAll() {
        List<Skill> skills = findAll();
        Consumer<Skill> setStatusDeleted = skill -> {
            if (skill.getStatus() == Status.ACTIVE)
                skill.setStatus(Status.DELETED);
        };
        ParametrizeMethodsCrud.deleteAll(skills, setStatusDeleted);
        cleanFile(file);
        skills.forEach(this::save);
     }
}
