package repository;

import model.Developer;
import model.Skill;
import model.Specialty;

import java.util.List;

public interface DeveloperRepository extends GenericRepository<Developer, Integer> {
    Skill addSkill(Integer developerId, Skill skill);
    void deleteSkill(Integer developerId, Integer skillId);
    List<Skill> findSkillsByDeveloperId(Integer id);
    Specialty addSpecialty(Integer developerId, Specialty speciality);
    void deleteSpecialty(Integer developerId, Integer specialityId);
}
