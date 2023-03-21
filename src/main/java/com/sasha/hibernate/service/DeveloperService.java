package com.sasha.hibernate.service;

import com.sasha.hibernate.pojo.Developer;
import com.sasha.hibernate.pojo.Skill;

import java.util.List;

public interface DeveloperService extends GenericService<Developer, Integer>{
    List<Skill> findSkillsByDeveloperId(Integer id);
}
