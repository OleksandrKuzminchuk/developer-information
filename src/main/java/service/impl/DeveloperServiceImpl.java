package service.impl;

import exception.NotFoundException;
import model.Developer;
import model.Skill;
import org.apache.commons.lang3.NotImplementedException;
import repository.DeveloperRepository;
import repository.SkillRepository;
import repository.SpecialtyRepository;
import service.DeveloperService;

import java.util.List;

import static util.constant.Constants.*;

public class DeveloperServiceImpl implements DeveloperService {
    private final DeveloperRepository repository;
    private final SpecialtyRepository specialtyRepository;
    private final SkillRepository skillRepository;

    public DeveloperServiceImpl(DeveloperRepository repository, SpecialtyRepository specialtyRepository, SkillRepository skillRepository) {
        this.repository = repository;
        this.specialtyRepository = specialtyRepository;
        this.skillRepository = skillRepository;
    }

    @Override
    public Developer save(Developer developer) {
        return repository.save(developer);
    }

    @Override
    public void saveAll(List<Developer> developers) {
        throw new NotImplementedException();
    }

    @Override
    public Developer update(Developer developer) {
        isExistsDeveloper(developer.getId());
        return repository.update(developer);
    }

    @Override
    public Developer findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(NOT_FOUND_DEVELOPER));
    }

    @Override
    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    @Override
    public List<Developer> findAll() {
        return repository.findAll();
    }

    @Override
    public Long count() {
        throw new NotImplementedException();
    }

    @Override
    public void deleteById(Integer id) {
        isExistsDeveloper(id);
        repository.deleteById(id);
    }

    @Override
    public void delete(Developer developer) {
        throw new NotImplementedException();
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public void addSkill(Integer developerId, Integer skillId) {
        isExistsDeveloper(developerId);
        isExistsSkill(skillId);
        isHasDeveloperSkill(developerId, skillId);
        repository.addSkill(developerId, skillId);
    }

    @Override
    public void deleteSkill(Integer developerId, Integer skillId) {
        isExistsDeveloper(developerId);
        isExistsSkill(skillId);
        repository.deleteSkill(developerId, skillId);
    }

    @Override
    public List<Skill> findSkillsByDeveloperId(Integer id) {
        return findById(id).getSkills();
    }

    @Override
    public void addSpecialty(Integer developerId, Integer specialityId) {
        isExistsDeveloper(developerId);
        isExistsSpecialty(specialityId);
        repository.addSpecialty(developerId, specialityId);
    }

    @Override
    public void deleteSpecialty(Integer developerId) {
        isExistsDeveloper(developerId);
        repository.deleteSpecialty(developerId);
    }

    /*
    The method 'isExistsDeveloper' check exists the developer by id if not then throw NotFoundException
     */
    private void isExistsDeveloper(Integer developerId){
        if (!existsById(developerId))
            throw new NotFoundException(NOT_FOUND_DEVELOPER);
    }

    /*
The method 'isExistsSpecialty' check exists the specialty by id if not then throw NotFoundException
 */
    private void isExistsSpecialty(Integer specialtyId){
        if (!specialtyRepository.existsById(specialtyId))
            throw new NotFoundException(NOT_FOUND_SPECIALITY);
    }

    /*
The method 'isExistsSkill' check exists the skill by id if not then throw NotFoundException
*/
    private void isExistsSkill(Integer skillId){
        if (!skillRepository.existsById(skillId))
            throw new NotFoundException(NOT_FOUND_SKILL);
    }

    /*
    The method 'isHasDeveloperSkill' is checking have already a developer of the skill if not then throw NotFoundException()
     */
    private boolean isHasDeveloperSkill(Integer developerId, Integer skillId){
        Developer developer = findById(developerId);
        if (developer.getSkills() == null){
            return true;
        }
        developer.getSkills().forEach(skill -> {
            if (skill.getId().equals(skillId)) {
                throw new NotFoundException(NOT_FOUND_SKILL);
            }
        });
        return true;
    }
}
