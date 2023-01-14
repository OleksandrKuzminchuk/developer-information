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
        assignStatusActiveIfNull(developer);
        isExistsDeveloperIntoFileById(developer.getId());
        ParametrizeMethodsCrud.save(developer, getDevelopers(), FILE, GSON, COLLECTION_TYPE);
        return developer;
    }

    @Override
    public void saveAll(List<Developer> developers) {
        developers.forEach(this::save);
    }

    @Override
    public Developer findById(Integer id) {
        return ParametrizeMethodsCrud.findById(getPredicateDeveloperIdEqualsIdAndStatusActive(id), getDevelopers(), NOT_FOUND_DEVELOPER);
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
        ParametrizeMethodsCrud.update(developers, getPredicateDeveloperIdEqualsId(developer.getId()),
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
        ParametrizeMethodsCrud.deleteById(developers, getPredicateDeveloperIdEqualsIdAndStatusActive(id),
                getConsumerSetDeveloperDeletedStatus(), FILE);
        saveAll(developers);
    }

    @Override
    public void delete(Developer developer) {
        throw new NotImplementedException(NOT_IMPLEMENTED_DELETE);
    }

    @Override
    public void deleteAll() {
        List<Developer> developers = getDevelopers();
        ParametrizeMethodsCrud.deleteAll(developers, getConsumerSetDeletedStatusIfStatusEqualsActive(), FILE);
        saveAll(developers);
    }

    @Override
    public Skill addSkill(Integer developerId, Skill skill) {
        List<Developer> developers = getDevelopers();
        developers.stream()
                .filter(getPredicateDeveloperIdEqualsId(developerId))
                .filter(getPredicateIsUniqueSkillInDeveloperList(skill))
                .forEach(getConsumerAddSkillIfDevIdEqualsDevId(developerId, skill));
        cleanFile(FILE);
        saveAll(developers);
        return skill;
    }

    @Override
    public void deleteSkill(Integer developerId, Integer skillId) {
        List<Developer> developers = getDevelopers();
        developers.stream()
                .filter(getPredicateDeveloperIdEqualsId(developerId))
                .filter(getPredicateIsThereUniqueSkillInDeveloperList(skillId))
                .forEach(getConsumerDeleteSkillIfSkillIdEqualsId(skillId));
        cleanFile(FILE);
        developers.forEach(this::save);
    }

    @Override
    public Specialty addSpecialty(Integer developerId, Specialty specialty) {
        List<Developer> developers = getDevelopers();
        developers.stream()
                .filter(getPredicateDeveloperIdEqualsId(developerId))
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
                .filter(getPredicateFilterSpecialtyIfDevIdEqualsIdAndSpecialtyIdEqualsId(developerId, specialityId))
                .findFirst()
                .ifPresentOrElse(getConsumerDeleteSpecialtySetNull(), () -> {
                    throw new NotValidException(DEVELOPER_HAS_NOT_SPECIALITY);
                });
        cleanFile(FILE);
        developers.forEach(this::save);
    }

    @Override
    public List<Skill> findSkillsByDeveloperId(Integer id) {
        Developer developer = findById(id);
        if (developer.getSkills().isEmpty()) {
            throw new NotFoundException(EMPTY_LIST);
        }else {
            return developer.getSkills();
        }
    }

    @Override
    public void deleteSkillByIdIfActiveOrSetStatusDeletedIfDeleted(Integer skillId) {
        List<Developer> developers = getDevelopers();
        developers.forEach(getConsumerDeleteSkillIfActiveOrSetStatusDeletedIfStatusDeleted(skillId));
        cleanFile(FILE);
        saveAll(developers);
    }

    @Override
    public void deleteAllSkillsActiveOrAllSetStatusDeletedIfDeleted() {
        List<Developer> developers = getDevelopers();
        developers.forEach(getConsumerDeleteAllSkillsActiveAndAllSetStatusDeletedIfDeleted());
        cleanFile(FILE);
        saveAll(developers);
    }

    @Override
    public void deleteSpecialtyByIdOrSetSpecialtyStatusDeleteIfNonNullAndEqualsId(Integer specialtyId) {
        List<Developer> developers = getDevelopers();
        developers.stream()
                .filter(getPredicateSpecialtyIdNonNullAndEqualsId(specialtyId))
                .forEach(getConsumerSetSpecialtyDeleteStatus());
        cleanFile(FILE);
        saveAll(developers);
    }

    @Override
    public void deleteAllSpecialtyOrSetSpecialtyStatusDeletedIfStatusDeleted() {
        List<Developer> developers = getDevelopers();
        developers.forEach(getConsumerSetSpecialtyDeleteStatus());
        cleanFile(FILE);
        saveAll(developers);
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

    private Predicate<Developer> getPredicateDeveloperIdEqualsIdAndStatusActive(Integer developerId) {
        return dev -> dev.getId().equals(developerId) && dev.getStatus().equals(Status.ACTIVE);
    }

    private Predicate<Developer> getPredicateDeveloperIdEqualsId(Integer developerId) {
        return dev -> dev.getId().equals(developerId);
    }

    private Consumer<Developer> getConsumerSetDeletedStatusIfStatusEqualsActive() {
        return dev -> {
            if (dev.getStatus() == Status.ACTIVE)
                dev.setStatus(Status.DELETED);
        };
    }

    private Consumer<Developer> getConsumerSetDeveloperDeletedStatus() {
        return dev -> dev.setStatus(Status.DELETED);
    }

    private Predicate<Developer> getPredicateIsUniqueSkillInDeveloperList(Skill skill) {
        return developer -> {
            if (developer.getSkills().stream().anyMatch(s -> s.getId().equals(skill.getId()))) {
                throw new NotValidException(DEVELOPER_HAS_SKILL);
            }else {
                return true;
            }
        };
    }

    private Consumer<Developer> getConsumerAddSkillIfDevIdEqualsDevId(Integer developerId, Skill skill) {
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

    private Predicate<Skill> getPredicateSkillIdEqualsId(Integer skillId) {
        return skill -> skill.getId().equals(skillId);
    }

    private Consumer<Developer> getConsumerDeleteSkillIfSkillIdEqualsId(Integer skillId) {
        return dev -> dev.getSkills().removeIf(getPredicateSkillIdEqualsId(skillId));
    }

    private Predicate<Developer> getPredicateIsUniqueSpecialityInDeveloper(Specialty specialty) {
        return developer -> {
            if (developer.getSpecialty() != null && developer.getSpecialty().getId().equals(specialty.getId())){
                throw new NotValidException(DEVELOPER_HAS_SPECIALITY);
            }else {
                return true;
            }
        };
    }

    private Consumer<Developer> getPredicateAddSpeciality(Integer developerId, Specialty specialty) {
        return dev -> {
            if (dev.getId().equals(developerId))
                dev.setSpecialty(specialty);
        };
    }

    private Consumer<Developer> getConsumerDeleteSpecialtySetNull() {
        return dev -> dev.setSpecialty(null);
    }

    private Predicate<Developer> getPredicateFilterSpecialtyIfDevIdEqualsIdAndSpecialtyIdEqualsId(Integer developerId, Integer specialityId) {
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
    private Consumer<Developer> getConsumerDeleteSkillIfActiveOrSetStatusDeletedIfStatusDeleted(Integer skillId){
        return dev -> {
            if (dev.getStatus().equals(Status.ACTIVE))
                dev.getSkills().removeIf(getPredicateSkillIdEqualsId(skillId));
            dev.getSkills().forEach(getConsumerIfSkillIdEqualsIdSetStatusDeleted(skillId));
        };
    }
    private Consumer<Skill> getConsumerIfSkillIdEqualsIdSetStatusDeleted(Integer skillId){
        return skill -> {
            if (skill.getId().equals(skillId))
                skill.setStatus(Status.DELETED);
        };
    }
    private Consumer<Developer> getConsumerDeleteAllSkillsActiveAndAllSetStatusDeletedIfDeleted(){
        return dev -> {
            if (dev.getStatus().equals(Status.ACTIVE))
                dev.getSkills().removeAll(dev.getSkills());
            dev.getSkills().forEach(getConsumerSetSkillDeletedStatus());
        };
    }
    private Consumer<Skill> getConsumerSetSkillDeletedStatus(){
        return skill -> skill.setStatus(Status.DELETED);
    }
    private Consumer<Developer> getConsumerSetSpecialtyDeleteStatus() {
        return dev -> {
            if (dev.getStatus().equals(Status.ACTIVE)) {
                dev.setSpecialty(null);
            } else {
                dev.getSpecialty().setStatus(Status.DELETED);
            }
        };
    }
    private Predicate<Developer> getPredicateSpecialtyIdNonNullAndEqualsId(Integer specialtyId){
        return dev -> dev.getSpecialty() != null &&
                dev.getSpecialty().getId().equals(specialtyId);
    }
    private void assignStatusActiveIfNull(Developer developer) {
        if (developer.getStatus() == null) {
            developer.setStatus(Status.ACTIVE);
        }
    }
}
