package com.sasha.jdbccrud.service.impl;

import com.sasha.jdbccrud.exception.NotFoundException;
import com.sasha.jdbccrud.model.Specialty;
import org.apache.commons.lang3.NotImplementedException;
import com.sasha.jdbccrud.repository.SpecialtyRepository;
import com.sasha.jdbccrud.service.SpecialtyService;

import java.util.List;

import static com.sasha.jdbccrud.util.constant.Constants.*;

public class SpecialtyServiceImpl implements SpecialtyService {
    private final SpecialtyRepository repository;
    public SpecialtyServiceImpl(SpecialtyRepository repository) {
        this.repository = repository;
    }

    @Override
    public Specialty save(Specialty specialty) {
        return repository.save(specialty).orElseThrow(() -> new NotFoundException(FAILED_TO_SAVE_A_SPECIALTY));
    }

    @Override
    public void saveAll(List<Specialty> specialities) {
        throw new NotImplementedException();
    }

    @Override
    public Specialty update(Specialty specialty) {
        isExistsSpecialty(specialty.getId());
        return repository.update(specialty).orElseThrow(() -> new NotFoundException(FAILED_TO_UPDATE_A_SPECIALTY_BY_ID));
    }

    @Override
    public Specialty findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(NOT_FOUND_SPECIALITY));
    }

    @Override
    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    @Override
    public List<Specialty> findAll() {
        return repository.findAll().orElseThrow(() -> new NotFoundException(FAILED_TO_FIND_ALL_SPECIALTIES));
    }

    @Override
    public void deleteById(Integer id) {
        isExistsSpecialty(id);
        repository.deleteById(id);
    }

    @Override
    public void delete(Specialty specialty) {
        throw new NotImplementedException();
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    protected void isExistsSpecialty(Integer id){
        if (!existsById(id))
            throw new NotFoundException(NOT_FOUND_SPECIALITY);
    }
}
