package com.sasha.hibernate.service.impl;

import com.sasha.hibernate.exception.NotFoundException;
import com.sasha.hibernate.pojo.Specialty;
import com.sasha.hibernate.pojo.Status;
import com.sasha.hibernate.repository.SpecialtyRepository;
import com.sasha.hibernate.service.SpecialtyService;
import com.sasha.hibernate.util.constant.Constants;

import java.util.List;

public class SpecialtyServiceImpl implements SpecialtyService {
    private final SpecialtyRepository repository;
    public SpecialtyServiceImpl(SpecialtyRepository repository) {
        this.repository = repository;
    }

    @Override
    public Specialty save(Specialty specialty) {
        return repository.save(specialty).orElseThrow(() -> new NotFoundException(Constants.FAILED_TO_SAVE_A_SPECIALTY));
    }

    @Override
    public Specialty update(Specialty specialty) {
        isExistsSpecialty(specialty.getId());
        return repository.update(specialty).orElseThrow(() -> new NotFoundException(Constants.FAILED_TO_UPDATE_A_SPECIALTY_BY_ID));
    }

    @Override
    public Specialty findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(Constants.NOT_FOUND_SPECIALITY));
    }

    @Override
    public List<Specialty> findAll() {
        return repository.findAll().orElseThrow(() -> new NotFoundException(Constants.FAILED_TO_FIND_ALL_SPECIALTIES));
    }

    @Override
    public void deleteById(Integer id) {
        isExistsSpecialty(id);
        repository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    protected void isExistsSpecialty(Integer id){
        if(!findById(id).getStatus().equals(Status.ACTIVE)){
            throw new NotFoundException(Constants.NOT_FOUND_SPECIALITY);
        }
    }
}
