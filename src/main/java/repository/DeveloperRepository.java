package repository;

import model.Developer;

public interface DeveloperRepository extends GenericRepository<Developer, Integer> {
    void addSkill(Integer developerId, Integer skillId);
    void deleteSkill(Integer developerId, Integer skillId);
    void addSpecialty(Integer developerId, Integer specialityId);
    void deleteSpecialty(Integer developerId);
}
