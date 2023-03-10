package service;

import model.Developer;
import model.Skill;

import java.util.List;

public interface DeveloperService extends GenericService<Developer, Integer>{
    void addSkill(Integer developerId, Integer skillId);
    void deleteSkill(Integer developerId, Integer skillId);
    List<Skill> findSkillsByDeveloperId(Integer id);
    void addSpecialty(Integer developerId, Integer specialityId);
    void deleteSpecialty(Integer developerId);
}
