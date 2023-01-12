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

import static repository.ParametrizeMethodsCrud.cleanFile;
import static util.Constants.*;

public class GsonDeveloperRepositoryImpl implements DeveloperRepository {
    private final File file;
    private final Gson gson;
    private final Type collectionType;
    public GsonDeveloperRepositoryImpl() {
        this.file = new File(FILE_DEVELOPERS_PATH);
        this.gson = new Gson();
        this.collectionType = new TypeToken<List<Developer>>() {}.getType();
    }

    @Override
    public Developer save(Developer developer) {
        List<Developer> developers = findAll();
        ParametrizeMethodsCrud.save(developer, developers, file, gson, collectionType);
        return developer;
    }

    @Override
    public Developer findById(Integer id) {
        List<Developer> developers = findAll();
        Predicate<Developer> findDeveloper = dev -> dev.getId().equals(id)
                && dev.getStatus().equals(Status.ACTIVE);
        return ParametrizeMethodsCrud.findById(findDeveloper, developers, NOT_FOUND_DEVELOPER);
    }

    @Override
    public boolean existsById(Integer id) {
        return this.findById(id) != null;
    }

    @Override
    public List<Developer> findAll() {
        return ParametrizeMethodsCrud.findAll(file, gson, collectionType);
    }

    @Override
    public Developer update(Developer developer) {
        List<Developer> developers = findAll();
        Predicate<Developer> findDeveloper = dev -> dev.getId().equals(developer.getId());
        Consumer<Developer> recordNewData = dev -> {
            if (developer.getFirstName() != null) {
                dev.setFirstName(developer.getFirstName());
                if (developer.getLastName() != null) {
                    dev.setLastName(developer.getLastName());
                }
            }
        };
        ParametrizeMethodsCrud.update(developers, findDeveloper, recordNewData);
        cleanFile(file);
        developers.forEach(this::save);
        return developer;
    }

    @Override
    public Long count() {
        throw new NotImplementedException();
    }

    @Override
    public void deleteById(Integer id) {
        List<Developer> developers = findAll();
        Predicate<Developer> findDeveloper = dev -> dev.getId().equals(id) && dev.getStatus().equals(Status.ACTIVE);
        Consumer<Developer> setStatusDeleted = dev -> dev.setStatus(Status.DELETED);
        ParametrizeMethodsCrud.deleteById(developers, findDeveloper, setStatusDeleted);
        cleanFile(file);
        developers.forEach(this::save);
    }

    @Override
    public void delete(Developer developer) {
        throw new NotImplementedException(NOT_IMPLEMENTED_DELETE);
    }

    @Override
    public void deleteAll() {
        List<Developer> developers = findAll();
        Consumer<Developer> setStatusDeleted = dev -> {
            if (dev.getStatus() == Status.ACTIVE)
                dev.setStatus(Status.DELETED);
        };
        ParametrizeMethodsCrud.deleteAll(developers, setStatusDeleted);
        cleanFile(file);
        developers.forEach(this::save);
    }

    @Override
    public Skill addSkill(Integer developerId, Skill skill) {
        List<Developer> developers = findAll();
        Predicate<Developer> isUniqueSkillInDeveloperList = developer -> {
            if (developer.getSkills().stream().anyMatch(s -> s.getId().equals(skill.getId())))
                throw new NotValidException(DEVELOPER_HAS_SKILL);
            return true;
        };
        Consumer<Developer> addSkill = dev -> {
            if (dev.getId().equals(developerId))
                dev.getSkills().add(skill);
        };
        Predicate<Developer> findDeveloper = dev -> dev.getId().equals(developerId);
        developers.stream().filter(findDeveloper).filter(isUniqueSkillInDeveloperList).forEach(addSkill);
        cleanFile(file);
        developers.forEach(this::save);
        return skill;
    }

    @Override
    public void deleteSkill(Integer developerId, Integer skillId) {
        List<Developer> developers = findAll();
        Predicate<Developer> isThereUniqueSkillInDeveloperList = developer -> {
            if (developer.getSkills().stream().anyMatch(skill -> skill.getId().equals(skillId))){
                return true;
            }else {
                throw new NotValidException(DEVELOPER_HAS_NOT_SKILL);
            }
        };
        Predicate<Developer> findDeveloper = dev -> dev.getId().equals(developerId);
        Predicate<Skill> removeSkillIfExistsInDeveloperList = skill -> skill.getId().equals(skillId);
        Consumer<Developer> deleteSkill = dev -> dev.getSkills().removeIf(removeSkillIfExistsInDeveloperList);
        developers.stream().filter(findDeveloper).filter(isThereUniqueSkillInDeveloperList).forEach(deleteSkill);
        cleanFile(file);
        developers.forEach(this::save);
    }

    @Override
    public Specialty addSpecialty(Integer developerId, Specialty specialty) {
        List<Developer> developers = findAll();
        Predicate<Developer> isUniqueSpecialityInDeveloper = developer -> {
          if (developer.getSpecialty() != null && developer.getSpecialty().getId().equals(specialty.getId()))
              throw new NotValidException(DEVELOPER_HAS_SPECIALITY);
          return true;
        };
        Predicate<Developer> findDeveloper = developer -> developer.getId().equals(developerId);
        Consumer<Developer> addSpeciality = dev -> {
            if (dev.getId().equals(developerId))
                dev.setSpecialty(specialty);
        };
        developers.stream().filter(findDeveloper).filter(isUniqueSpecialityInDeveloper).forEach(addSpeciality);
        cleanFile(file);
        developers.forEach(this::save);
        return specialty;
    }

    @Override
    public void deleteSpecialty(Integer developerId, Integer specialityId) {
        List<Developer> developers = findAll();
        Consumer<Developer> deleteSpeciality = dev -> dev.setSpecialty(null);
        Runnable exception = () -> {throw new NotValidException(DEVELOPER_HAS_NOT_SPECIALITY);};
        Predicate<Developer> findDeveloper = dev -> dev.getId().equals(developerId) && dev.getSpecialty().getId().equals(specialityId);
        developers.stream().filter(findDeveloper).findFirst().ifPresentOrElse(deleteSpeciality, exception);
        cleanFile(file);
        developers.forEach(this::save);
    }

    @Override
    public List<Skill> findSkillsByDeveloperId(Integer id) {
        Developer developer = findById(id);
        if (developer.getSkills().isEmpty())
            throw new NotFoundException(EMPTY_LIST);
        return developer.getSkills();
    }
}
