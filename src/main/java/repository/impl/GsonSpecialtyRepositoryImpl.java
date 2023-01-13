package repository.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Specialty;
import model.Status;
import org.apache.commons.lang3.NotImplementedException;
import repository.ParametrizeMethodsCrud;
import repository.SpecialtyRepository;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static java.lang.String.format;
import static util.Constants.*;

public class GsonSpecialtyRepositoryImpl implements SpecialtyRepository {
    private static final File FILE = new File(FILE_SPECIALTIES_PATH);
    private static final Gson GSON = new Gson();
    private static final Type COLLECTION_TYPE = new TypeToken<List<Specialty>>() {
    }.getType();

    @Override
    public Specialty save(Specialty specialty) {
        isExistsSpecialityIntoFileById(specialty.getId());
        ParametrizeMethodsCrud.save(specialty, getSpecialties(), FILE, GSON, COLLECTION_TYPE);
        return specialty;
    }

    @Override
    public void saveAll(List<Specialty> specialties) {
        specialties.forEach(this::save);
    }

    @Override
    public Specialty update(Specialty specialty) {
        List<Specialty> specialties = getSpecialties();
        ParametrizeMethodsCrud.update(specialties, getPredicateEqualsIdSpecialty(specialty.getId()),
                getConsumerUpdate(specialty), FILE);
        saveAll(specialties);
        return specialty;
    }

    @Override
    public Specialty findById(Integer id) {
        return ParametrizeMethodsCrud.findById(getPredicateEqualsIdAndStatusActive(id), getSpecialties(), NOT_FOUND_SPECIALITY);
    }

    @Override
    public boolean existsById(Integer id) {
        return findById(id) != null;
    }

    @Override
    public List<Specialty> findAll() {
        return ParametrizeMethodsCrud.findAll(FILE, GSON, COLLECTION_TYPE);
    }

    @Override
    public Long count() {
        throw new NotImplementedException(NOT_IMPLEMENTED_COUNT);
    }

    @Override
    public void deleteById(Integer id) {
        List<Specialty> specialties = getSpecialties();
        ParametrizeMethodsCrud.deleteById(specialties, getPredicateEqualsIdAndStatusActive(id), getConsumerSetStatusDeleted(), FILE);
        saveAll(specialties);
    }

    @Override
    public void delete(Specialty specialty) {
        throw new NotImplementedException(NOT_IMPLEMENTED_DELETE);
    }

    @Override
    public void deleteAll() {
        List<Specialty> specialties = getSpecialties();
        ParametrizeMethodsCrud.deleteAll(specialties, getConsumerSetStatusDeletedIfEqualsActive(), FILE);
        saveAll(specialties);
    }

    private void isExistsSpecialityIntoFileById(Integer id) {
        if (FILE.length() != 0) {
            List<Specialty> specialties = findAll();
            if (specialties.stream().anyMatch(specialty -> specialty.getId().equals(id)
                    && specialty.getStatus().equals(Status.ACTIVE))) {
                throw new IllegalArgumentException(format(EXCEPTION_SPECIALTY_HAS_ALREADY_TAKEN, id));
            } else if (specialties.stream().anyMatch(specialty -> specialty.getStatus().equals(Status.DELETED)
                    && specialty.getId().equals(id))) {
                throw new IllegalArgumentException(NOT_FOUND_SPECIALITY);
            }
        }
    }

    private List<Specialty> getSpecialties() {
        return this.findAll();
    }

    private Predicate<Specialty> getPredicateEqualsIdSpecialty(Integer specialtyId) {
        return specialty -> specialty.getId().equals(specialtyId);
    }

    private Consumer<Specialty> getConsumerUpdate(Specialty specialty) {
        return s -> {
            if (specialty.getName() != null && !specialty.getName().equals(s.getName()))
                s.setName(specialty.getName());
        };
    }

    private Predicate<Specialty> getPredicateEqualsIdAndStatusActive(Integer specialtyId) {
        return specialty -> specialty.getId().equals(specialtyId)
                && specialty.getStatus().equals(Status.ACTIVE);
    }

    private Consumer<Specialty> getConsumerSetStatusDeleted() {
        return specialty -> specialty.setStatus(Status.DELETED);
    }

    private Consumer<Specialty> getConsumerSetStatusDeletedIfEqualsActive() {
        return specialty -> {
            if (specialty.getStatus() == Status.ACTIVE)
                specialty.setStatus(Status.DELETED);
        };
    }
}
