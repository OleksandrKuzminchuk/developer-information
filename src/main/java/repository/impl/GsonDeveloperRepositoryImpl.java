package repository.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import exception.NotFoundException;
import exception.NotValidException;
import model.Developer;
import model.Skill;
import model.Specialty;
import model.Status;
import repository.DeveloperRepository;
import org.apache.commons.lang3.NotImplementedException;
import repository.ParametrizeMethodsCrud;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static java.lang.String.format;
import static repository.ParametrizeMethodsCrud.cleanFile;
import static util.Constants.*;

public class GsonDeveloperRepositoryImpl implements DeveloperRepository {
    private static final File FILE = new File(FILE_DEVELOPERS_PATH);
    private static final Gson GSON = new Gson();
    private static final Type COLLECTION_TYPE = new TypeToken<List<Developer>>() {
    }.getType();

    @Override
    public Developer save(Developer developer) {
        isExistsDeveloperIntoFileById(developer.getId());
        ParametrizeMethodsCrud.save(developer, getDevelopers(), FILE, GSON, COLLECTION_TYPE);
        return developer;
    }

    @Override
    public void saveAll(List<Developer> developers) {
        ParametrizeMethodsCrud.saveAll(developers, FILE, GSON, COLLECTION_TYPE);
    }

    @Override
    public Developer findById(Integer id) {
        return ParametrizeMethodsCrud.findById(getPredicateEqualsIdAndStatusActive(id), getDevelopers(), NOT_FOUND_DEVELOPER);
    }

    @Override
    public boolean existsById(Integer id) {
        return this.findById(id) != null;
    }

    @Override
    public List<Developer> findAll() {
        return ParametrizeMethodsCrud.findAll(FILE, GSON, COLLECTION_TYPE);
    }

    @Override
    public Developer update(Developer developer) {
        List<Developer> developers = getDevelopers();
        ParametrizeMethodsCrud.update(developers, getPredicateEqualsIdDeveloper(developer.getId()),
                getConsumerUpdate(developer), FILE);
        saveAll(developers);
        return developer;
    }

    @Override
    public Long count() {
        throw new NotImplementedException();
    }

    @Override
    public void deleteById(Integer id) {
        List<Developer> developers = getDevelopers();
        ParametrizeMethodsCrud.deleteById(getDevelopers(), getPredicateEqualsIdAndStatusActive(id),
                getConsumerSetStatusDeleted(), FILE);
        saveAll(developers);
    }

    @Override
    public void delete(Developer developer) {
        throw new NotImplementedException(NOT_IMPLEMENTED_DELETE);
    }

    @Override
    public void deleteAll() {
        List<Developer> developers = getDevelopers();
        ParametrizeMethodsCrud.deleteAll(getDevelopers(), getConsumerSetAllStatusDeleted(), FILE);
        saveAll(developers);
    }

    @Override
    public Skill addSkill(Integer developerId, Skill skill) {
        List<Developer> developers = getDevelopers();
        developers.stream()
                .filter(getPredicateEqualsIdDeveloper(developerId))
                .filter(getPredicateIsUniqueSkillInDeveloperList(skill))
                .forEach(getConsumerAddSkill(developerId, skill));
        cleanFile(FILE);
        saveAll(developers);
        return skill;
    }

    @Override
    public void deleteSkill(Integer developerId, Integer skillId) {
        List<Developer> developers = getDevelopers();
        developers.stream()
                .filter(getPredicateEqualsIdDeveloper(developerId))
                .filter(getPredicateIsThereUniqueSkillInDeveloperList(skillId))
                .forEach(getConsumerDeleteSkill(skillId));
        cleanFile(FILE);
        developers.forEach(this::save);
    }

    @Override
    public Specialty addSpecialty(Integer developerId, Specialty specialty) {
        List<Developer> developers = getDevelopers();
        developers.stream()
                .filter(getPredicateEqualsIdDeveloper(developerId))
                .filter(getPredicateIsUniqueSpecialityInDeveloper(specialty))
                .forEach(getPredicateAddSpeciality(developerId, specialty));
        cleanFile(FILE);
        developers.forEach(this::save);
        return specialty;
    }

    @Override
    public void deleteSpecialty(Integer developerId, Integer specialityId) {
        List<Developer> developers = getDevelopers();
        developers.stream()
                .filter(getPredicateFindBySpecialty(developerId, specialityId))
                .findFirst()
                .ifPresentOrElse(getConsumerDeleteSpecialty(), () -> {
                    throw new NotValidException(DEVELOPER_HAS_NOT_SPECIALITY);
                });
        cleanFile(FILE);
        developers.forEach(this::save);
    }

    @Override
    public List<Skill> findSkillsByDeveloperId(Integer id) {
        Developer developer = findById(id);
        if (developer.getSkills().isEmpty())
            throw new NotFoundException(EMPTY_LIST);
        return developer.getSkills();
    }

    private void isExistsDeveloperIntoFileById(Integer id) {
        if (FILE.length() != 0) {
            List<Developer> developers = findAll();
            if (developers.stream().anyMatch(developer -> developer.getId().equals(id)
                    && developer.getStatus().equals(Status.ACTIVE))) {
                throw new IllegalArgumentException(format(EXCEPTION_DEVELOPER_HAS_ALREADY_TAKEN, id));
            } else if (developers.stream().anyMatch(developer -> developer.getId().equals(id)
                    && developer.getStatus().equals(Status.DELETED))) {
                throw new IllegalArgumentException(NOT_FOUND_DEVELOPER);
            }
        }
    }

    private List<Developer> getDevelopers() {
        return findAll();
    }

    private Predicate<Developer> getPredicateEqualsIdAndStatusActive(Integer developerId) {
        return dev -> dev.getId().equals(developerId) && dev.getStatus().equals(Status.ACTIVE);
    }

    private Predicate<Developer> getPredicateEqualsIdDeveloper(Integer developerId) {
        return dev -> dev.getId().equals(developerId);
    }

    private Consumer<Developer> getConsumerSetAllStatusDeleted() {
        return dev -> {
            if (dev.getStatus() == Status.ACTIVE)
                dev.setStatus(Status.DELETED);
        };
    }

    private Consumer<Developer> getConsumerSetStatusDeleted() {
        return dev -> dev.setStatus(Status.DELETED);
    }

    private Predicate<Developer> getPredicateIsUniqueSkillInDeveloperList(Skill skill) {
        return developer -> {
            if (developer.getSkills().stream().anyMatch(s -> s.getId().equals(skill.getId())))
                throw new NotValidException(DEVELOPER_HAS_SKILL);
            return true;
        };
    }

    private Consumer<Developer> getConsumerAddSkill(Integer developerId, Skill skill) {
        return dev -> {
            if (dev.getId().equals(developerId))
                dev.getSkills().add(skill);
        };
    }

    private Predicate<Developer> getPredicateIsThereUniqueSkillInDeveloperList(Integer skillId) {
        return developer -> {
            if (developer.getSkills().stream().anyMatch(skill -> skill.getId().equals(skillId))) {
                return true;
            } else {
                throw new NotValidException(DEVELOPER_HAS_NOT_SKILL);
            }
        };
    }

    private Predicate<Skill> getPredicateEqualsIdSkill(Integer skillId) {
        return skill -> skill.getId().equals(skillId);
    }

    private Consumer<Developer> getConsumerDeleteSkill(Integer skillId) {
        return dev -> dev.getSkills().removeIf(getPredicateEqualsIdSkill(skillId));
    }

    private Predicate<Developer> getPredicateIsUniqueSpecialityInDeveloper(Specialty specialty) {
        return developer -> {
            if (developer.getSpecialty() != null && developer.getSpecialty().getId().equals(specialty.getId()))
                throw new NotValidException(DEVELOPER_HAS_SPECIALITY);
            return true;
        };
    }

    private Consumer<Developer> getPredicateAddSpeciality(Integer developerId, Specialty specialty) {
        return dev -> {
            if (dev.getId().equals(developerId))
                dev.setSpecialty(specialty);
        };
    }

    private Consumer<Developer> getConsumerDeleteSpecialty() {
        return dev -> dev.setSpecialty(null);
    }

    private Predicate<Developer> getPredicateFindBySpecialty(Integer developerId, Integer specialityId) {
        return dev -> dev.getId().equals(developerId) && dev.getSpecialty().getId().equals(specialityId);
    }

    private Consumer<Developer> getConsumerUpdate(Developer developer) {
        return dev -> {
            if (developer.getFirstName() != null && !developer.getFirstName().equals(dev.getFirstName())) {
                dev.setFirstName(developer.getFirstName());
                if (developer.getLastName() != null && !developer.getLastName().equals(dev.getLastName())) {
                    dev.setLastName(developer.getLastName());
                }
            }
        };
    }
}
