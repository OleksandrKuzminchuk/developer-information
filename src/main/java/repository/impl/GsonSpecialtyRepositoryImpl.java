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
        Predicate<Specialty> predicate = s -> s.getId().equals(specialty.getId());
        Consumer<Specialty> consumer = s -> {
            if (specialty.getName() != null)
                s.setName(specialty.getName());
        };
        ParametrizeMethodsCrud.update(specialty, specialties, predicate, consumer);
        cleanFile(file);
        specialties.forEach(this::save);
        return specialty;
    }

    @Override
    public Specialty findById(Integer id) {
        List<Specialty> specialties = findAll();
        Predicate<Specialty> predicate = specialty -> specialty.getId().equals(id)
                && specialty.getStatus().equals(Status.ACTIVE);
        return ParametrizeMethodsCrud.findById(id, predicate, specialties, NOT_FOUND_SPECIALITY);
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
        Predicate<Specialty> predicate = specialty -> specialty.getId().equals(id)
                && specialty.getStatus().equals(Status.ACTIVE);
        Consumer<Specialty> consumer = specialty -> specialty.setStatus(Status.DELETED);
        ParametrizeMethodsCrud.deleteById(id, specialties, predicate, consumer);
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
        Consumer<Specialty> consumer = specialty -> {
            if (specialty.getStatus() == Status.ACTIVE)
                specialty.setStatus(Status.DELETED);
        };
        ParametrizeMethodsCrud.deleteAll(specialties, consumer);
        cleanFile(file);
        specialties.forEach(this::save);
    }
}
