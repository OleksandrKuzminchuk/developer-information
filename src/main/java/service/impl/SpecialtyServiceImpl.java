package service.impl;

import exception.NotFoundException;
import model.Specialty;
import org.apache.commons.lang3.NotImplementedException;
import repository.SpecialtyRepository;
import service.SpecialtyService;

import java.util.List;

import static util.constant.Constants.NOT_FOUND_SPECIALITY;

public class SpecialtyServiceImpl implements SpecialtyService {
    private final SpecialtyRepository repository;
    public SpecialtyServiceImpl(SpecialtyRepository repository) {
        this.repository = repository;
    }

    @Override
    public Specialty save(Specialty specialty) {
        return repository.save(specialty);
    }

    @Override
    public void saveAll(List<Specialty> specialities) {
        throw new NotImplementedException();
    }

    @Override
    public Specialty update(Specialty specialty) {
        isExistsSpecialty(specialty.getId());
        return repository.update(specialty);
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
        return repository.findAll();
    }

    @Override
    public Long count() {
        throw new NotImplementedException();
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

     /*
         The method 'isExistsSpecialty' check exists the specialty by id if not then throw NotFoundException
      */
    protected void isExistsSpecialty(Integer id){
        if (!existsById(id))
            throw new NotFoundException(NOT_FOUND_SPECIALITY);
    }
}
