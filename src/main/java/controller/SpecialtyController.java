package controller;

import model.Developer;
import model.Specialty;
import model.Status;
import repository.DeveloperRepository;
import repository.SpecialtyRepository;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static repository.ParametrizeMethodsCrud.cleanFile;
import static util.Constants.*;

public class SpecialtyController {
    private final SpecialtyRepository repository;
    private final DeveloperRepository developerRepository;

    public SpecialtyController(SpecialtyRepository repository, DeveloperRepository developerRepository) {
        this.repository = repository;
        this.developerRepository = developerRepository;
    }

    public Specialty save(Specialty specialty) {
        assignStatusActiveIfNull(specialty);
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
        List<Developer> developers = developerRepository.findAll();
        Predicate<Developer> filterActiveAndHasSpecialty = dev -> dev.getSpecialty().getId().equals(id);
        developers.stream().filter(filterActiveAndHasSpecialty).forEach(getConsumerSetSpecialtyStatusDelete());
        cleanFile(new File(FILE_DEVELOPERS_PATH));
        developers.forEach(developerRepository::save);
        return RESPONSE_OK;
    }

    public String deleteAll() {
        repository.deleteAll();
        List<Developer> developers = developerRepository.findAll();
        developers.forEach(getConsumerSetSpecialtyStatusDelete());
        cleanFile(new File(FILE_DEVELOPERS_PATH));
        developers.forEach(developerRepository::save);
        return RESPONSE_OK;
    }

    private static void assignStatusActiveIfNull(Specialty specialty) {
        if (specialty.getStatus() == null) {
            specialty.setStatus(Status.ACTIVE);
        }
    }

    private Consumer<Developer> getConsumerSetSpecialtyStatusDelete() {
        return dev -> {
            if (dev.getStatus().equals(Status.ACTIVE))
                dev.setSpecialty(null);
            dev.getSpecialty().setStatus(Status.DELETED);
        };
    }
}
