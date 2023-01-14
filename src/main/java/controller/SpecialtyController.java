package controller;

import model.Specialty;
import repository.DeveloperRepository;
import repository.SpecialtyRepository;

import java.util.List;

import static util.Constants.*;

public class SpecialtyController {
    private final SpecialtyRepository repository;
    private final DeveloperRepository developerRepository;

    public SpecialtyController(SpecialtyRepository repository, DeveloperRepository developerRepository) {
        this.repository = repository;
        this.developerRepository = developerRepository;
    }

    public Specialty save(Specialty specialty) {
        return repository.save(specialty);
    }

    public Specialty update(Specialty specialty) {
        repository.existsById(specialty.getId());
        return repository.update(specialty);
    }

    public Specialty findById(Integer id) {
        return repository.findById(id);
    }

    public List<Specialty> findAll() {
        return repository.findAll();
    }

    public String deleteById(Integer id) {
        repository.existsById(id);
        repository.deleteById(id);
        developerRepository.deleteSpecialtyByIdOrSetSpecialtyStatusDeleteIfNonNullAndEqualsId(id);
        return RESPONSE_OK;
    }

    public String deleteAll() {
        repository.deleteAll();
        developerRepository.deleteAllSpecialtyOrSetSpecialtyStatusDeletedIfStatusDeleted();
        return RESPONSE_OK;
    }
}
