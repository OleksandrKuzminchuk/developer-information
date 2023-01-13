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

import static java.lang.String.format;
import static util.Constants.*;

public class GsonSkillRepositoryImpl implements SkillRepository {
    private static final File FILE = new File(FILE_SKILLS_PATH);
    private static final Gson GSON = new Gson();
    private static final Type COLLECTION_TYPE = new TypeToken<List<Skill>>() {
    }.getType();

    @Override
    public Skill save(Skill skill) {
        isExistsSkillIntoFileById(skill.getId());
        ParametrizeMethodsCrud.save(skill, getSkills(), FILE, GSON, COLLECTION_TYPE);
        return skill;
    }

    @Override
    public void saveAll(List<Skill> skills) {
        skills.forEach(this::save);
    }

    @Override
    public Skill findById(Integer id) {
        return ParametrizeMethodsCrud.findById(getPredicateEqualsIdAndStatusActive(id), getSkills(), NOT_FOUND_SKILL);
    }

    @Override
    public Skill update(Skill skill) {
        ParametrizeMethodsCrud.update(getSkills(), getPredicateEqualsId(skill.getId()), getConsumerSetName(skill),
                FILE, GSON, COLLECTION_TYPE);
        return skill;
    }

    @Override
    public boolean existsById(Integer id) {
        return findById(id) != null;
    }

    @Override
    public List<Skill> findAll() {
        return ParametrizeMethodsCrud.findAll(FILE, GSON, COLLECTION_TYPE);
    }

    @Override
    public Long count() {
        throw new NotImplementedException(NOT_IMPLEMENTED_COUNT);
    }

    @Override
    public void deleteById(Integer id) {
        ParametrizeMethodsCrud.deleteById(getSkills(), getPredicateEqualsIdAndStatusActive(id), getConsumerSetStatusDelete(),
                FILE, GSON, COLLECTION_TYPE);
    }

    @Override
    public void delete(Skill skill) {
        throw new NotImplementedException(NOT_IMPLEMENTED_DELETE);
    }

    @Override
    public void deleteAll() {
        ParametrizeMethodsCrud.deleteAll(getSkills(), getConsumerSetStatusDeleteIfStatusActive(), FILE, GSON, COLLECTION_TYPE);
    }

    private void isExistsSkillIntoFileById(Integer id) {
        if (FILE.length() != 0) {
            List<Skill> skills = findAll();
            if (skills.stream().anyMatch(skill -> skill.getId().equals(id)
                    && skill.getStatus().equals(Status.ACTIVE))) {
                throw new IllegalArgumentException(format(EXCEPTION_SKILL_HAS_ALREADY_TAKEN, id));
            } else if (skills.stream().anyMatch(developer -> developer.getStatus().equals(Status.DELETED)
                    && developer.getId().equals(id))) {
                throw new IllegalArgumentException(NOT_FOUND_SKILL);
            }
        }
    }

    private List<Skill> getSkills() {
        return this.findAll();
    }

    private Predicate<Skill> getPredicateEqualsId(Integer skillId) {
        return skill -> skill.getId().equals(skillId);
    }

    private Predicate<Skill> getPredicateEqualsIdAndStatusActive(Integer skillId) {
        return skill -> skill.getId().equals(skillId) && skill.getStatus().equals(Status.ACTIVE);
    }

    private Consumer<Skill> getConsumerSetName(Skill skill) {
        return s -> {
            if (skill.getName() != null && !skill.getName().equals(s.getName()))
                s.setName(skill.getName());
        };
    }

    private Consumer<Skill> getConsumerSetStatusDelete() {
        return skill -> skill.setStatus(Status.DELETED);
    }

    private Consumer<Skill> getConsumerSetStatusDeleteIfStatusActive() {
        return skill -> {
            if (skill.getStatus() == Status.ACTIVE)
                skill.setStatus(Status.DELETED);
        };
    }
}
