package com.sasha.hibernate.repository.impl;

import com.sasha.hibernate.pojo.Developer;
import com.sasha.hibernate.pojo.Specialty;
import com.sasha.hibernate.pojo.Status;
import com.sasha.hibernate.util.HibernateUtil;
import com.sasha.hibernate.repository.SpecialtyRepository;
import jakarta.persistence.LockModeType;
import org.hibernate.LockMode;
import org.hibernate.Session;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.sasha.hibernate.util.constant.SqlConstantsSpecialty.*;
import static java.lang.String.format;
import static com.sasha.hibernate.util.constant.Constants.*;

public class SpecialtyRepositoryImpl implements SpecialtyRepository {
    private final HibernateUtil hibernateUtil = HibernateUtil.getInstance();

    @Override
    public Optional<Specialty> save(Specialty specialty) {
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            session.getTransaction().begin();
            specialty.setStatus(Status.ACTIVE);
            session.persist(specialty);
            session.flush();
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(FAILED_TO_SAVE_A_SPECIALTY + e);
            return Optional.empty();
        }
        return Optional.of(specialty);
    }

    @Override
    public Optional<Specialty> update(Specialty specialty) {
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            session.getTransaction().begin();
            Specialty updateSpecialty = session.get(Specialty.class, specialty.getId(), LockMode.PESSIMISTIC_WRITE);
            updateSpecialty.setName(specialty.getName());
            session.getTransaction().commit();
            return Optional.of(updateSpecialty);
        } catch (Exception e) {
            System.out.println(format(FAILED_TO_UPDATE_A_SPECIALTY_BY_ID, specialty.getId()) + e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Specialty> findById(Integer id) {
        Specialty result = null;
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            result = session.get(Specialty.class, id);
        } catch (Exception e) {
            System.out.println(format(FAILED_TO_FIND_A_SPECIALTY_BY_ID, id) + e);
        }
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<List<Specialty>> findAll() {
        List<Specialty> specialities;
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            specialities = session
                    .createSelectionQuery(FIND_ALL_SPECIALTIES, Specialty.class)
                    .setParameter(PARAMETER_ACTIVE, Status.ACTIVE)
                    .getResultList();
        } catch (Exception e) {
            System.out.println(FAILED_TO_FIND_ALL_SPECIALTIES + e);
            return Optional.empty();
        }
        return Optional.of(specialities);
    }

    @Override
    public void deleteById(Integer id) {
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            session.getTransaction().begin();
            Specialty deleteSpecialty = session.get(Specialty.class, id, LockMode.PESSIMISTIC_WRITE);

            List<Developer> developers = session
                    .createSelectionQuery(FIND_ALL_DEVELOPERS_WITH_SPECIALTIES, Developer.class)
                    .setParameter(PARAMETER_ACTIVE, Status.ACTIVE)
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                    .getResultList();
            developers.forEach(developer -> {
                if (developer.getSpecialty() != null && developer.getSpecialty().getId().equals(deleteSpecialty.getId())) {
                    developer.setSpecialty(null);
                    session.merge(developer);
                }
            });

            deleteSpecialty.setStatus(Status.DELETED);
            session.merge(deleteSpecialty);

            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(format(FAILED_TO_DELETE_A_SPECIALTY_BY_ID, id) + e);
        }
    }

    @Override
    public void deleteAll() {
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            session.getTransaction().begin();

            List<Developer> developers = session
                    .createSelectionQuery(FIND_ALL_DEVELOPERS_WITH_SPECIALTIES, Developer.class)
                    .setParameter(PARAMETER_ACTIVE, Status.ACTIVE)
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                    .getResultList();
            developers.forEach(developer -> {
                if (developer.getSpecialty() != null) {
                    developer.setSpecialty(null);
                    session.merge(developer);
                }
            });

            session
                    .createMutationQuery(DELETE_ALL_SPECIALTIES)
                    .setProperties(Map.of(PARAMETER_DELETED, Status.DELETED, PARAMETER_ACTIVE, Status.ACTIVE))
                    .executeUpdate();

            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(FAILED_TO_DELETE_ALL_SPECIALTIES + e);
        }
    }
}
