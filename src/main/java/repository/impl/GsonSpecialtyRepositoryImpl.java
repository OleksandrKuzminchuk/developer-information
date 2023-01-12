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

import static repository.ParametrizeMethodsCrud.cleanFile;
import static util.Constants.*;

public class GsonSpecialtyRepositoryImpl implements SpecialtyRepository {
    private final File file;
    private final Gson gson;
    private final Type collectionType;
    public GsonSpecialtyRepositoryImpl() {
        this.file = new File(FILE_SPECIALTIES_PATH);
        this.gson = new Gson();
        this.collectionType = new TypeToken<List<Specialty>>() {}.getType();
    }
    @Override
    public Specialty save(Specialty specialty) {
        List<Specialty> specialties = findAll();
        ParametrizeMethodsCrud.save(specialty, specialties, file, gson, collectionType);
        return specialty;
    }

    @Override
    public Specialty update(Specialty specialty) {
        List<Specialty> specialties = findAll();
        Predicate<Specialty> findSpecialty = s -> s.getId().equals(specialty.getId());
        Consumer<Specialty> setName = s -> {
            if (specialty.getName() != null)
                s.setName(specialty.getName());
        };
        ParametrizeMethodsCrud.update(specialties, findSpecialty, setName);
        cleanFile(file);
        specialties.forEach(this::save);
        return specialty;
    }

    @Override
    public Specialty findById(Integer id) {
        List<Specialty> specialties = findAll();
        Predicate<Specialty> findSpecialty = specialty -> specialty.getId().equals(id)
                && specialty.getStatus().equals(Status.ACTIVE);
        return ParametrizeMethodsCrud.findById(findSpecialty, specialties, NOT_FOUND_SPECIALITY);
    }

    @Override
    public boolean existsById(Integer id) {
        return findById(id) != null;
    }

    @Override
    public List<Specialty> findAll() {
        return ParametrizeMethodsCrud.findAll(file, gson, collectionType);
    }

    @Override
    public Long count() {
        throw new NotImplementedException(NOT_IMPLEMENTED_COUNT);
    }

    @Override
    public void deleteById(Integer id) {
        List<Specialty> specialties = findAll();
        Predicate<Specialty> findSpecialty = specialty -> specialty.getId().equals(id)
                && specialty.getStatus().equals(Status.ACTIVE);
        Consumer<Specialty> setStatusDeleted = specialty -> specialty.setStatus(Status.DELETED);
        ParametrizeMethodsCrud.deleteById(specialties, findSpecialty, setStatusDeleted);
        cleanFile(file);
        specialties.forEach(this::save);
    }

    @Override
    public void delete(Specialty specialty) {
        throw new NotImplementedException(NOT_IMPLEMENTED_DELETE);
    }

    @Override
    public void deleteAll() {
        List<Specialty> specialties = findAll();
        Consumer<Specialty> setStatusDeleted = specialty -> {
            if (specialty.getStatus() == Status.ACTIVE)
                specialty.setStatus(Status.DELETED);
        };
        ParametrizeMethodsCrud.deleteAll(specialties, setStatusDeleted);
        cleanFile(file);
        specialties.forEach(this::save);
    }
}
