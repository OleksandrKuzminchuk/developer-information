package service.impl;

import exception.NotFoundException;
import model.Skill;
import model.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.SkillRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static util.constant.Constants.NOT_FOUND_SKILL;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class SkillServiceImplTest {
    @Mock
    private SkillRepository repository;
    @InjectMocks
    private SkillServiceImpl service;
    private Skill expectedSkillWithName;
    private Skill expectedSkillWithIdName;
    private NotFoundException notFoundException;

    @BeforeEach
    void setUp() {
        expectedSkillWithName = getExpectedSkillWithName();
        expectedSkillWithIdName = getExpectedSkillWithIdName();
        notFoundException = getNotFoundException();
        Mockito.reset(repository);
    }

    @Test
    void shouldSaveSkill() {
        when(repository.save(argThat(skill -> skill.equals(expectedSkillWithName) && skill.getName().equals(expectedSkillWithName.getName()))))
                .thenReturn(expectedSkillWithName);

        Skill savedSkill = service.save(expectedSkillWithName);

        assertNotNull(savedSkill);
        assertEquals(expectedSkillWithName, savedSkill);

        verify(repository, atLeastOnce())
                .save(argThat(skill -> skill.equals(expectedSkillWithName) && skill.getName().equals(expectedSkillWithName.getName())));
    }

    @Test
    void shouldUpdateSkill() {

        when(repository.existsById(expectedSkillWithIdName.getId())).thenReturn(true);
        when(repository.update(argThat(skill -> skill.getId().equals(expectedSkillWithIdName.getId()) && skill.getName().equals(expectedSkillWithIdName.getName()))))
                .thenReturn(expectedSkillWithIdName);

        Skill updatedSkill = service.update(expectedSkillWithIdName);

        assertNotNull(updatedSkill);
        assertEquals(expectedSkillWithIdName, updatedSkill);

        verify(repository, atLeastOnce()).existsById(expectedSkillWithIdName.getId());
        verify(repository, atLeastOnce()).update(argThat(skill -> skill.getId().equals(expectedSkillWithIdName.getId()) && skill.getName().equals(expectedSkillWithIdName.getName())));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUpdateSkill() {

        when(repository.existsById(expectedSkillWithIdName.getId())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> service.update(expectedSkillWithIdName));

        verify(repository, times(1)).existsById(expectedSkillWithIdName.getId());
        verify(repository, never()).update(expectedSkillWithIdName);

        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldFindByIdSkill() {
        when(repository.findById(expectedSkillWithIdName.getId())).thenReturn(Optional.ofNullable(expectedSkillWithIdName));

        Skill foundSkill = service.findById(1);

        assertNotNull(foundSkill);
        assertEquals(expectedSkillWithIdName, foundSkill);

        verify(repository, times(1)).findById(1);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenFindByIdSkill() {
        when(repository.findById(expectedSkillWithIdName.getId())).thenThrow(notFoundException);

        assertThrows(NotFoundException.class, () -> service.findById(1));

        verify(repository, times(1)).findById(1);
    }

    @Test
    void shouldReturnTrueIfSkillExistsWhenExistsById() {
        when(repository.existsById(expectedSkillWithIdName.getId())).thenReturn(true);

        boolean existsSpecialty = service.existsById(expectedSkillWithIdName.getId());

        assertTrue(existsSpecialty);

        verify(repository, times(1)).existsById(expectedSkillWithIdName.getId());
    }

    @Test
    void shouldReturnFalseIfSkillDoesNotExistsWhenExistsById() {
        when(repository.existsById(expectedSkillWithIdName.getId())).thenReturn(false);

        boolean existsSpecialty = service.existsById(expectedSkillWithIdName.getId());

        assertFalse(existsSpecialty);

        verify(repository, times(1)).existsById(expectedSkillWithIdName.getId());
    }

    @Test
    void findAllSkills() {
        when(repository.findAll()).thenReturn(getExpectedSkills());

        List<Skill> allFoundSkills = service.findAll();

        assertNotNull(allFoundSkills);
        assertFalse(allFoundSkills.isEmpty());
        assertEquals(getExpectedSkills(), allFoundSkills);
        assertEquals(2, allFoundSkills.size());

        verify(repository, times(1)).findAll();
    }

    @Test
    void shouldDeleteByIdSkill() {
        when(repository.existsById(expectedSkillWithIdName.getId())).thenReturn(true);
        doNothing().when(repository).deleteById(expectedSkillWithIdName.getId());

        service.deleteById(expectedSkillWithIdName.getId());

        verify(repository, times(1)).deleteById(expectedSkillWithIdName.getId());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenDeleteByIdSkill() {
        Integer deleteBySkillId = expectedSkillWithIdName.getId();

        when(repository.existsById(deleteBySkillId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> service.deleteById(deleteBySkillId));

        verify(repository, times(1)).existsById(expectedSkillWithIdName.getId());
        verify(repository, never()).deleteById(expectedSkillWithIdName.getId());
    }

    @Test
    void shouldDeleteAllSkill() {
        doNothing().when(repository).deleteAll();

        service.deleteAll();

        verify(repository, times(1)).deleteAll();
    }

    private Skill getExpectedSkillWithName() {
        return new Skill("any");
    }

    private Skill getExpectedSkillWithIdName() {
        return new Skill(1, "any");
    }

    private NotFoundException getNotFoundException() {
        return new NotFoundException(NOT_FOUND_SKILL);
    }

    private List<Skill> getExpectedSkills() {
        return new LinkedList<>(List.of(
                new Skill(1, "any", Status.ACTIVE),
                new Skill(2, "any", Status.ACTIVE)
        ));
    }
}