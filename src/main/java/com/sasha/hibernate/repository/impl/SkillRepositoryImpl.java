package com.sasha.hibernate.repository.impl;

import com.sasha.hibernate.pojo.Developer;
import com.sasha.hibernate.pojo.Skill;
import com.sasha.hibernate.pojo.Status;
import com.sasha.hibernate.repository.SkillRepository;
import com.sasha.hibernate.util.HibernateUtil;
import com.sasha.hibernate.util.constant.Constants;
import jakarta.persistence.LockModeType;
import org.hibernate.LockMode;
import org.hibernate.Session;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.sasha.hibernate.util.constant.Constants.PARAMETER_ACTIVE;
import static com.sasha.hibernate.util.constant.Constants.PARAMETER_DELETED;
import static com.sasha.hibernate.util.constant.SqlConstantsSkill.*;

public class SkillRepositoryImpl implements SkillRepository {
    private HibernateUtil hibernateUtil = HibernateUtil.getInstance();

    @Override
    public Optional<Skill> save(Skill skill) {
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            session.getTransaction().begin();
            skill.setStatus(Status.ACTIVE);
            session.persist(skill);
            session.flush();
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(Constants.FAILED_TO_SAVE_SKILL + e);
            return Optional.empty();
        }
        return Optional.of(skill);
    }

    @Override
    public Optional<Skill> update(Skill skill) {
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            session.getTransaction().begin();
            Skill updateSkill = session.get(Skill.class, skill.getId(), LockMode.PESSIMISTIC_WRITE);
            updateSkill.setName(skill.getName());
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(String.format(Constants.FAILED_TO_UPDATE_SKILL_BY_ID, skill.getId()) + e);
            return Optional.empty();
        }
        return Optional.of(skill);
    }

    @Override
    public Optional<Skill> findById(Integer id) {
        Skill result = null;
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            result = session.get(Skill.class, id);
        } catch (Exception e) {
            System.out.println(String.format(Constants.FAILED_TO_FIND_SKILL_BY_ID, id) + e);
            return Optional.empty();
        }
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<List<Skill>> findAll() {
        List<Skill> skills;
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            skills = session
                    .createSelectionQuery(FIND_ALL_SKILLS, Skill.class)
                    .setParameter(PARAMETER_ACTIVE, Status.ACTIVE)
                    .getResultList();
        } catch (Exception e) {
            System.out.println(Constants.FAILED_TO_FIND_ALL_SKILLS + e);
            return Optional.empty();
        }
        return Optional.of(skills);
    }

    @Override
    public void deleteById(Integer id) {
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            session.getTransaction().begin();

            Skill deleteSkill = session.get(Skill.class, id, LockMode.PESSIMISTIC_WRITE);

            List<Developer> developers = session
                    .createSelectionQuery(FIND_DEVELOPERS_WITH_SKILLS, Developer.class)
                    .setParameter(PARAMETER_ACTIVE, Status.ACTIVE)
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                    .getResultList();
            developers.forEach(developer -> {
                developer.getSkills().removeIf(skill -> skill.getId().equals(deleteSkill.getId()));
                session.merge(developer);
            });

            deleteSkill.setStatus(Status.DELETED);
            session.merge(deleteSkill);

            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(String.format(Constants.FAILED_TO_DELETE_SKILL_BY_ID, id) + e);
        }
    }

    @Override
    public void deleteAll() {
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            session.getTransaction().begin();

            List<Developer> developers = session
                    .createSelectionQuery(FIND_DEVELOPERS_WITH_SKILLS, Developer.class)
                    .setParameter(PARAMETER_ACTIVE, Status.ACTIVE)
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                    .getResultList();
            developers.forEach(developer -> {
                developer.setSkills(null);
                session.merge(developer);
            });

            session
                    .createMutationQuery(DELETE_ALL_SKILLS)
                    .setProperties(Map.of(PARAMETER_DELETED, Status.DELETED, PARAMETER_ACTIVE, Status.ACTIVE))
                    .executeUpdate();

            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(Constants.FAILED_TO_DELETE_ALL_SKILLS + e);
        }
    }
}
