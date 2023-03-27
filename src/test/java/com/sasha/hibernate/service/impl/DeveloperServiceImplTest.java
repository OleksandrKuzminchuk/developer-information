package com.sasha.hibernate.service.impl;

import com.sasha.hibernate.exception.NotFoundException;
import com.sasha.hibernate.pojo.Developer;
import com.sasha.hibernate.pojo.Skill;
import com.sasha.hibernate.pojo.Specialty;
import com.sasha.hibernate.pojo.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.sasha.hibernate.repository.DeveloperRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.sasha.hibernate.util.constant.Constants.NOT_FOUND_DEVELOPER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class DeveloperServiceImplTest {
    @Mock
    private DeveloperRepository repository;
    @InjectMocks
    private DeveloperServiceImpl service;
    private Integer developerId;
    private Developer expectedDeveloperWithIdFirstNameLastName;
    private Developer expectedDeveloperWithSpecialtySkills;
    private NotFoundException notFoundExceptionDeveloper;

    @BeforeEach
    void setUp() {
        expectedDeveloperWithIdFirstNameLastName = getExpectedDeveloperWithIdFirstNameLastName();
        expectedDeveloperWithSpecialtySkills = getExpectedDeveloperWithSpecialtySkills();
        notFoundExceptionDeveloper = getNotFoundExceptionDeveloper();
        developerId = expectedDeveloperWithIdFirstNameLastName.getId();

        Mockito.reset(repository);
    }

    @Test
    void shouldSaveDeveloper() {

        when(repository.save(
                argThat(developer ->
                        developer.getFirstName().equals(expectedDeveloperWithSpecialtySkills.getFirstName()) &&
                                developer.getLastName().equals(expectedDeveloperWithSpecialtySkills.getLastName()) &&
                                developer.getSpecialty().equals(expectedDeveloperWithSpecialtySkills.getSpecialty()) &&
                                developer.getSkills().equals(expectedDeveloperWithSpecialtySkills.getSkills()))))
                .thenReturn(Optional.of(expectedDeveloperWithSpecialtySkills));

        Developer savedDeveloper = service.save(expectedDeveloperWithSpecialtySkills);

        assertNotNull(savedDeveloper);
        assertEquals(expectedDeveloperWithSpecialtySkills, savedDeveloper);
        assertEquals(1, savedDeveloper.getId());

        verify(repository, times(1)).save(
                argThat(developer ->
                        developer.getFirstName().equals(expectedDeveloperWithSpecialtySkills.getFirstName()) &&
                                developer.getLastName().equals(expectedDeveloperWithSpecialtySkills.getLastName()) &&
                                developer.getSpecialty().equals(expectedDeveloperWithSpecialtySkills.getSpecialty()) &&
                                developer.getSkills().equals(expectedDeveloperWithSpecialtySkills.getSkills())));
    }

    @Test
    void shouldUpdateDeveloper() {

        when(repository.update(argThat(developer -> developer.getId().equals(expectedDeveloperWithSpecialtySkills.getId())))).thenReturn(Optional.ofNullable(expectedDeveloperWithSpecialtySkills));

        Developer updatedDeveloper = service.update(expectedDeveloperWithSpecialtySkills);

        assertNotNull(updatedDeveloper);
        assertEquals(expectedDeveloperWithSpecialtySkills, updatedDeveloper);
        assertEquals(expectedDeveloperWithSpecialtySkills.getSpecialty(), updatedDeveloper.getSpecialty());
        assertEquals(expectedDeveloperWithSpecialtySkills.getSkills(), updatedDeveloper.getSkills());

        verify(repository, times(1)).update(argThat(developer -> developer.getId().equals(expectedDeveloperWithSpecialtySkills.getId())));
    }

    @Test
    void shouldFindByIdDeveloper() {

        when(repository.findById(developerId)).thenReturn(Optional.ofNullable(expectedDeveloperWithIdFirstNameLastName));

        Developer foundDeveloper = service.findById(developerId);

        assertNotNull(foundDeveloper);
        assertEquals(expectedDeveloperWithIdFirstNameLastName, foundDeveloper);

        verify(repository, times(1)).findById(developerId);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenFindByIdDeveloper() {

        when(repository.findById(developerId)).thenThrow(notFoundExceptionDeveloper);

        assertThrows(NotFoundException.class, () -> service.findById(developerId));

        verify(repository, times(1)).findById(developerId);
    }

    @Test
    void shouldFindAllDevelopers() {

        when(repository.findAll()).thenReturn(Optional.of(getExpectedDevelopers()));

        List<Developer> foundAllDevelopers = service.findAll();

        assertNotNull(foundAllDevelopers);
        assertFalse(foundAllDevelopers.isEmpty());
        assertEquals(getExpectedDevelopers(), foundAllDevelopers);
        assertEquals(2, foundAllDevelopers.size());
        assertEquals(2, foundAllDevelopers.get(0).getSkills().size());
        assertEquals(2, foundAllDevelopers.get(1).getSkills().size());
        assertEquals(getExpectedDevelopers().get(0).getSpecialty(), foundAllDevelopers.get(0).getSpecialty());
        assertEquals(getExpectedDevelopers().get(1).getSpecialty(), foundAllDevelopers.get(1).getSpecialty());
        assertEquals(getExpectedDevelopers().get(0), foundAllDevelopers.get(0));
        assertEquals(getExpectedDevelopers().get(1), foundAllDevelopers.get(1));

        verify(repository, times(1)).findAll();
    }

    @Test
    void shouldDeleteByIdDeveloper() {

        when(repository.findById(developerId)).thenReturn(Optional.ofNullable(expectedDeveloperWithIdFirstNameLastName));
        doNothing().when(repository).deleteById(developerId);

        service.deleteById(developerId);

        verify(repository, times(1)).findById(developerId);
        verify(repository, times(1)).deleteById(developerId);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenDeleteByIdDeveloper() {

        when(repository.findById(developerId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.deleteById(developerId));

        verify(repository, times(1)).findById(developerId);
        verify(repository, never()).deleteById(developerId);
    }

    @Test
    void shouldDeleteAllDevelopers() {

        doNothing().when(repository).deleteAll();

        service.deleteAll();

        verify(repository, times(1)).deleteAll();
    }

    @Test
    void shouldFindSkillsByDeveloperId() {
        List<Skill> expectedSkills = expectedDeveloperWithSpecialtySkills.getSkills();

        when(repository.findById(developerId)).thenReturn(Optional.ofNullable(expectedDeveloperWithSpecialtySkills));

        List<Skill> foundSkills = service.findSkillsByDeveloperId(developerId);

        assertNotNull(foundSkills);
        assertEquals(expectedSkills, foundSkills);
        assertEquals(2, foundSkills.size());

        verify(repository, times(1)).findById(developerId);
    }

    private Developer getExpectedDeveloperWithIdFirstNameLastName() {
        return new Developer(1, "any", "any", Status.ACTIVE);
    }

    private Developer getExpectedDeveloperWithSpecialtySkills() {
        return new Developer(
                1,
                "any",
                "any",
                getExpectedSkills(),
                getExpectedSpecialtyWithIdName(),
                Status.ACTIVE
        );
    }

    private NotFoundException getNotFoundExceptionDeveloper() {
        return new NotFoundException(NOT_FOUND_DEVELOPER);
    }

    private List<Developer> getExpectedDevelopers() {
        return new LinkedList<>(List.of(
                new Developer(1, "any", "any", getExpectedSkills(), getExpectedSpecialtyWithIdName()),
                new Developer(2, "any", "any", getExpectedSkills(), getExpectedSpecialtyWithIdName())
        ));
    }

    private Specialty getExpectedSpecialtyWithIdName() {
        return new Specialty(1, "any", Status.ACTIVE);
    }

    private List<Skill> getExpectedSkills() {
        return new LinkedList<>(List.of(
                new Skill(1, "any", Status.ACTIVE),
                new Skill(2, "any", Status.ACTIVE)
        ));
    }
}