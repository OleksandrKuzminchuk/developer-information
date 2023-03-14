package com.sasha.jdbccrud.service;

import com.sasha.jdbccrud.model.Developer;
import com.sasha.jdbccrud.model.Skill;

import java.util.List;

public interface DeveloperService extends GenericService<Developer, Integer>{
    List<Skill> findSkillsByDeveloperId(Integer id);
}
