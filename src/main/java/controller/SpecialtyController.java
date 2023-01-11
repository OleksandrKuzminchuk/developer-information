package controller;

import model.Developer;
import model.Specialty;
import model.Status;
import repository.DeveloperRepository;
import repository.SpecialtyRepository;

import java.io.File;
import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static repository.ParametrizeMethodsCrud.cleanFile;
import static util.Constants.*;

public class SpecialtyController {
    private final File file;
    private final SpecialtyRepository repository;
    private final DeveloperRepository developerRepository;

    public SpecialtyController(SpecialtyRepository repository, DeveloperRepository developerRepository) {
        this.repository = repository;
        this.developerRepository = developerRepository;
        this.file = new File(FILE_SPECIALTIES_PATH);
    }
    public Specialty save(Specialty specialty) throws IllegalArgumentException {
        requireNonNull(specialty);
        isExistsSpecialityIntoFileById(specialty.getId());
        assignStatusActiveIfNull(specialty);
        return repository.save(specialty);
    }
    public Specialty update(Specialty specialty){
        requireNonNull(specialty);
        repository.existsById(specialty.getId());
        return repository.update(specialty);
    }
    public Specialty findById(Integer id){
        requireNonNull(id);
        return repository.findById(id);
    }

    public List<Specialty> findAll(){
        return repository.findAll();
    }

    public String deleteById(Integer id){
        requireNonNull(id);
        repository.existsById(id);
        repository.deleteById(id);
        List<Developer> developers = developerRepository.findAll();
        developers.stream().filter(developer -> developer.getSpecialty().getId().equals(id))
                .forEach(developer -> developer.setSpecialty(new Specialty(TWO_BILLION)));
        cleanFile(new File(FILE_DEVELOPERS_PATH));
        developers.forEach(developerRepository::save);
        return RESPONSE_OK;
    }
    public String deleteAll(){
        repository.deleteAll();
        List<Developer> developers = developerRepository.findAll();
        developers.forEach(developer -> developer.setSpecialty(new Specialty(TWO_BILLION)));
        cleanFile(new File(FILE_DEVELOPERS_PATH));
        developers.forEach(developerRepository::save);
        return RESPONSE_OK;
    }
    private void isExistsSpecialityIntoFileById(Integer id) {
        if (file.length() != 0) {
            List<Specialty> specialties = findAll();
            if (specialties.stream().anyMatch(specialty -> specialty.getId().equals(id)
                    && specialty.getStatus().equals(Status.ACTIVE))) {
                throw new IllegalArgumentException(format("Speciality has already taken by id - %d", id));
            } else if (specialties.stream().anyMatch(specialty -> specialty.getStatus().equals(Status.DELETED)
                    && specialty.getId().equals(id))) {
                throw new IllegalArgumentException(NOT_FOUND_SPECIALITY);
            }
        }
    }
    private static void assignStatusActiveIfNull(Specialty specialty) {
        if (specialty.getStatus() == null) {
            specialty.setStatus(Status.ACTIVE);
        }
    }
}
