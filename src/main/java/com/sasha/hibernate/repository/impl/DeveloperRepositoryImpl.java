package com.sasha.hibernate.repository.impl;

import com.sasha.hibernate.pojo.Developer;
import com.sasha.hibernate.pojo.Status;
import com.sasha.hibernate.repository.DeveloperRepository;
import com.sasha.hibernate.util.HibernateUtil;
import com.sasha.hibernate.util.constant.Constants;
import jakarta.persistence.LockModeType;
import org.hibernate.LockMode;
import org.hibernate.Session;

import java.util.*;

import static com.sasha.hibernate.util.constant.Constants.PARAMETER_ACTIVE;
import static com.sasha.hibernate.util.constant.Constants.PARAMETER_ID;
import static com.sasha.hibernate.util.constant.SqlConstantsDeveloper.FIND_ALL_DEVELOPERS;
import static com.sasha.hibernate.util.constant.SqlConstantsDeveloper.FIND_BY_ID_DEVELOPER;

public class DeveloperRepositoryImpl implements DeveloperRepository {
    private final HibernateUtil hibernateUtil = HibernateUtil.getInstance();

    @Override
    public Optional<Developer> save(Developer developer) {
        Developer savedDeveloper;
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            session.getTransaction().begin();
            developer.setStatus(Status.ACTIVE);
            savedDeveloper = session.merge(developer);
            session.flush();
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(Constants.FAILED_TO_SAVE_DEVELOPER + e);
            return Optional.empty();
        }
        return Optional.of(savedDeveloper);
    }

    @Override
    public Optional<Developer> update(Developer developer) {
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            session.getTransaction().begin();
            Developer updateDeveloper = session.get(Developer.class, developer.getId(), LockMode.PESSIMISTIC_WRITE);
            updateDeveloper.setFirstName(developer.getFirstName() != null && !developer.getFirstName().equals(updateDeveloper.getFirstName())? developer.getFirstName() : null);
            updateDeveloper.setLastName(developer.getLastName() != null && !developer.getLastName().equals(updateDeveloper.getLastName()) ? developer.getLastName() : null);
            updateDeveloper.setSpecialty(developer.getSpecialty() != null && !developer.getSpecialty().equals(updateDeveloper.getSpecialty()) ? developer.getSpecialty() : null);
            if (developer.getSkills() != null) {
                developer
                        .getSkills()
                        .forEach(skill -> updateDeveloper.getSkills().add(skill));
            }
            session.getTransaction().commit();
            return Optional.of(updateDeveloper);
        } catch (Exception e) {
            System.out.println(Constants.FAILED_TO_UPDATE_DEVELOPER + e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Developer> findById(Integer id) {
        Developer result;
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            result = session
                    .createSelectionQuery(FIND_BY_ID_DEVELOPER, Developer.class)
                    .setParameter(PARAMETER_ID, id)
                    .getSingleResult();
        } catch (Exception e) {
            System.out.println(String.format(Constants.FAILED_TO_FIND_DEVELOPER_BY_ID, id) + e);
            return Optional.empty();
        }
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<List<Developer>> findAll() {
        List<Developer> developers;
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            developers = session
                    .createSelectionQuery(FIND_ALL_DEVELOPERS, Developer.class)
                    .setParameter(PARAMETER_ACTIVE, Status.ACTIVE)
                    .getResultList();
        } catch (Exception e) {
            System.out.println(Constants.FAILED_TO_FIND_ALL_DEVELOPERS + e);
            return Optional.empty();
        }
        return Optional.of(developers);
    }

    @Override
    public void deleteById(Integer id) {
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            session.getTransaction().begin();
            Developer deleteDeveloper = session.get(Developer.class, id, LockMode.PESSIMISTIC_WRITE);
            processDeleteDeveloper(deleteDeveloper, session);
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(String.format(Constants.FAILED_TO_DELETE_DEVELOPER_BY_ID, id) + e);
        }
    }

    @Override
    public void deleteAll() {
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            session.getTransaction().begin();
            List<Developer> deleteAll = session
                    .createSelectionQuery(FIND_ALL_DEVELOPERS, Developer.class)
                    .setParameter(PARAMETER_ACTIVE, Status.ACTIVE)
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                    .getResultList();
            deleteAll.forEach(developer -> processDeleteDeveloper(developer, session));
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(Constants.FAILED_TO_DELETE_ALL_DEVELOPERS + e);
        }
    }

    private void processDeleteDeveloper(Developer deleteDeveloper, Session session) {
        if (deleteDeveloper.getSpecialty() != null){
            deleteDeveloper.setSpecialty(null);
            session.merge(deleteDeveloper);
        }
        if (deleteDeveloper.getSkills() != null){
            deleteDeveloper.setSkills(null);
            session.merge(deleteDeveloper);
        }
        deleteDeveloper.setStatus(Status.DELETED);
    }
}
