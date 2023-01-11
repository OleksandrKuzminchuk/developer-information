package repository;

import model.Developer;
import model.Skill;

public interface SkillRepository extends GenericRepository<Skill, Integer> {
    Skill update(Skill skill);
}
