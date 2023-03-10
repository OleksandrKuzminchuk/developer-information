package service.impl;

import exception.NotFoundException;
import model.Developer;
import model.Skill;
import model.Specialty;
import model.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.DeveloperRepository;
import repository.SkillRepository;
import repository.SpecialtyRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static util.constant.Constants.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class DeveloperServiceImplTest {
    @Mock
    private SpecialtyRepository specialtyRepository;
    @Mock
    private SkillRepository skillRepository;
    @Mock
    private DeveloperRepository repository;
    @InjectMocks
    private DeveloperServiceImpl service;
    private Integer developerId;
    private Integer specialtyId;
    private Integer skillId;
    private Integer newSkillId;
    private Developer expectedDeveloperWithFirstNameLastName;
    private Developer expectedDeveloperWithIdFirstNameLastName;
    private Developer expectedDeveloperWithSpecialtySkills;
    private Skill expectedNewSkillWithIdName;
    private NotFoundException notFoundExceptionDeveloper;

    @BeforeEach
    void setUp() {
        expectedDeveloperWithFirstNameLastName = getExpectedDeveloperWithFirstNameLastName();
        expectedDeveloperWithIdFirstNameLastName = getExpectedDeveloperWithIdFirstNameLastName();
        expectedDeveloperWithSpecialtySkills = getExpectedDeveloperWithSpecialtySkills();
        Specialty expectedSpecialtyWithIdName = getExpectedSpecialtyWithIdName();
        Skill expectedSkillWithIdName = getExpectedSkillWithIdName();
        expectedNewSkillWithIdName = getExpectedNewSkillWithIdName();
        notFoundExceptionDeveloper = getNotFoundExceptionDeveloper();
        developerId = expectedDeveloperWithIdFirstNameLastName.getId();
        specialtyId = expectedSpecialtyWithIdName.getId();
        skillId = expectedSkillWithIdName.getId();
        newSkillId = expectedNewSkillWithIdName.getId();

        Mockito.reset(repository);
    }

    @Test
    void shouldSaveDeveloper() {
        Developer expectedDeveloper = expectedDeveloperWithFirstNameLastName;

        when(repository.save(
                argThat(developer -> developer.getFirstName().equals(expectedDeveloper.getFirstName())
                        && developer.getLastName().equals(expectedDeveloper.getLastName()))))
                .thenReturn(expectedDeveloper);

        Developer savedDeveloper = service.save(expectedDeveloper);

        assertNotNull(savedDeveloper);
        assertEquals(expectedDeveloper, savedDeveloper);

        verify(repository, times(1)).save(
                argThat(developer ->
                        developer.getFirstName().equals(expectedDeveloper.getFirstName()) &&
                                developer.getLastName().equals(expectedDeveloper.getLastName())));
    }

    @Test
    void shouldUpdateDeveloper() {

        when(repository.existsById(developerId)).thenReturn(true);
        when(repository.update(argThat(developer -> developer.getId().equals(developerId)))).thenReturn(expectedDeveloperWithIdFirstNameLastName);

        Developer updatedDeveloper = service.update(expectedDeveloperWithIdFirstNameLastName);

        assertNotNull(updatedDeveloper);
        assertEquals(expectedDeveloperWithIdFirstNameLastName, updatedDeveloper);

        verify(repository, times(1)).existsById(developerId);
        verify(repository, times(1)).update(argThat(developer -> developer.getId().equals(developerId)));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUpdateDeveloper() {

        when(repository.existsById(developerId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> service.update(expectedDeveloperWithIdFirstNameLastName));

        verify(repository, times(1)).existsById(developerId);
        verify(repository, times(0)).update(expectedDeveloperWithIdFirstNameLastName);
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
    void shouldReturnTrueIfDeveloperExistsWhenExistsById() {

        when(repository.existsById(developerId)).thenReturn(true);

        boolean existsDeveloper = service.existsById(developerId);

        assertTrue(existsDeveloper);

        verify(repository, times(1)).existsById(developerId);
    }

    @Test
    void shouldReturnFalseIfDeveloperExistsWhenExistsById() {

        when(repository.existsById(developerId)).thenReturn(false);

        boolean existsDeveloper = service.existsById(developerId);

        assertFalse(existsDeveloper);

        verify(repository, times(1)).existsById(developerId);
    }

    @Test
    void shouldFindAllDevelopers() {

        when(repository.findAll()).thenReturn(getExpectedDevelopers());

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

        when(repository.existsById(developerId)).thenReturn(true);
        doNothing().when(repository).deleteById(developerId);

        service.deleteById(developerId);

        verify(repository, times(1)).existsById(developerId);
        verify(repository, times(1)).deleteById(developerId);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenDeleteByIdDeveloper() {

        when(repository.existsById(developerId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> service.deleteById(developerId));

        verify(repository, times(1)).existsById(developerId);
        verify(repository, never()).deleteById(developerId);
    }

    @Test
    void shouldDeleteAllDevelopers() {

        doNothing().when(repository).deleteAll();

        service.deleteAll();

        verify(repository, times(1)).deleteAll();
    }

    @Test
    void shouldAddSkillToDeveloper() {

        when(repository.existsById(developerId)).thenReturn(true);
        when(skillRepository.existsById(newSkillId)).thenReturn(true);
        when(repository.findById(developerId)).thenReturn(Optional.ofNullable(expectedDeveloperWithSpecialtySkills));
        doNothing().when(repository).addSkill(developerId, newSkillId);

        service.addSkill(developerId, newSkillId);

        verify(repository, times(1)).existsById(developerId);
        verify(skillRepository, times(1)).existsById(newSkillId);
        verify(repository, times(1)).findById(developerId);
        verify(repository, times(1)).addSkill(developerId, newSkillId);
    }

    @Test
    void shouldThrowNotFoundExceptionDeveloperWhenAddSkillToDeveloper() {

        when(repository.existsById(developerId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> service.addSkill(developerId, newSkillId));

        verify(repository, times(1)).existsById(developerId);
        verify(skillRepository, never()).existsById(newSkillId);
        verify(repository, never()).findById(developerId);
        verify(repository, never()).addSkill(developerId, newSkillId);
    }

    @Test
    void shouldThrowNotFoundExceptionSkillWhenAddSkillToDeveloper() {

        when(repository.existsById(developerId)).thenReturn(true);
        when(skillRepository.existsById(newSkillId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> service.addSkill(developerId, newSkillId));

        verify(repository, times(1)).existsById(developerId);
        verify(skillRepository, times(1)).existsById(newSkillId);
        verify(repository, never()).findById(developerId);
        verify(repository, never()).addSkill(developerId, newSkillId);
    }

    @Test
    void shouldThrowNotFoundExceptionIfSkillHasAlreadyTakenIntoDevelopersListSkillsWhenAddSkillToDeveloper() {

        when(repository.existsById(expectedDeveloperWithSpecialtySkills.getId())).thenReturn(true);
        when(skillRepository.existsById(newSkillId)).thenReturn(true);

        Developer expectedDeveloper = expectedDeveloperWithSpecialtySkills;
        expectedDeveloper.getSkills().add(expectedNewSkillWithIdName);

        when(repository.findById(expectedDeveloper.getId())).thenReturn(Optional.of(expectedDeveloper));

        assertThrows(NotFoundException.class, () -> service.addSkill(developerId, newSkillId));

        verify(repository, times(1)).existsById(expectedDeveloper.getId());
        verify(skillRepository, times(1)).existsById(newSkillId);
        verify(repository, times(1)).findById(expectedDeveloper.getId());
        verify(repository, never()).addSkill(expectedDeveloper.getId(), newSkillId);
    }

    @Test
    void shouldDeleteSkillFromDeveloper() {

        when(repository.existsById(developerId)).thenReturn(true);
        when(skillRepository.existsById(skillId)).thenReturn(true);
        doNothing().when(repository).deleteSkill(developerId, skillId);

        service.deleteSkill(developerId, skillId);

        verify(repository, times(1)).existsById(developerId);
        verify(skillRepository, times(1)).existsById(skillId);
        verify(repository, times(1)).deleteSkill(developerId, skillId);
    }

    @Test
    void shouldThrowNotFoundExceptionDeveloperWhenDeleteSkillFromDeveloper() {

        when(repository.existsById(developerId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> service.deleteSkill(developerId, skillId));

        verify(repository, times(1)).existsById(developerId);
        verify(skillRepository, never()).existsById(skillId);
        verify(repository, never()).deleteSkill(developerId, skillId);
    }

    @Test
    void shouldThrowNotFoundExceptionSkillWhenDeleteSkillFromDeveloper() {

        when(repository.existsById(developerId)).thenReturn(true);
        when(skillRepository.existsById(skillId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> service.deleteSkill(developerId, skillId));

        verify(repository, times(1)).existsById(developerId);
        verify(skillRepository, times(1)).existsById(skillId);
        verify(repository, never()).deleteSkill(developerId, skillId);
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

    @Test
    void shouldAddSpecialtyToDeveloper() {

        when(repository.existsById(developerId)).thenReturn(true);
        when(specialtyRepository.existsById(specialtyId)).thenReturn(true);
        doNothing().when(repository).addSpecialty(developerId, specialtyId);

        service.addSpecialty(developerId, specialtyId);

        verify(repository, times(1)).existsById(developerId);
        verify(specialtyRepository, times(1)).existsById(specialtyId);
        verify(repository, times(1)).addSpecialty(developerId, specialtyId);
    }

    @Test
    void shouldThrowNotFoundExceptionDeveloperWhenAddSpecialtyToDeveloper() {

        when(repository.existsById(developerId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> service.addSpecialty(developerId, specialtyId));

        verify(repository, times(1)).existsById(developerId);
        verify(specialtyRepository, never()).existsById(specialtyId);
        verify(repository, never()).addSpecialty(developerId, specialtyId);
    }

    @Test
    void shouldThrowNotFoundExceptionSpecialtyWhenAddSpecialtyToDeveloper() {

        when(repository.existsById(developerId)).thenReturn(true);
        when(specialtyRepository.existsById(specialtyId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> service.addSpecialty(developerId, specialtyId));

        verify(repository, times(1)).existsById(developerId);
        verify(specialtyRepository, times(1)).existsById(specialtyId);
        verify(repository, never()).addSpecialty(developerId, specialtyId);
    }

    @Test
    void shouldDeleteSpecialtyFromDeveloper() {

        when(repository.existsById(developerId)).thenReturn(true);
        doNothing().when(repository).deleteSpecialty(developerId);

        service.deleteSpecialty(developerId);

        verify(repository, times(1)).existsById(developerId);
        verify(repository, times(1)).deleteSpecialty(specialtyId);
    }

    @Test
    void shouldThrowNotFoundExceptionDeveloperWhenDeleteSpecialtyFromDeveloper() {

        when(repository.existsById(developerId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> service.deleteSpecialty(developerId));

        verify(repository, times(1)).existsById(developerId);
        verify(repository, never()).deleteSpecialty(specialtyId);
    }

    private Developer getExpectedDeveloperWithFirstNameLastName() {
        return new Developer("any", "any");
    }

    private Developer getExpectedDeveloperWithIdFirstNameLastName() {
        return new Developer(1, "any", "any");
    }

    private Developer getExpectedDeveloperWithSpecialtySkills() {
        return new Developer(
                1,
                "any",
                "any",
                getExpectedSkills(),
                getExpectedSpecialtyWithIdName()
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
        return new Specialty(1, "any");
    }

    private Skill getExpectedSkillWithIdName() {
        return new Skill(1, "any");
    }

    private Skill getExpectedNewSkillWithIdName() {
        return new Skill(5, "any");
    }

    private List<Skill> getExpectedSkills() {
        return new LinkedList<>(List.of(
                new Skill(1, "any", Status.ACTIVE),
                new Skill(2, "any", Status.ACTIVE)
        ));
    }
}