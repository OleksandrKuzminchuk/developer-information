package service.impl;

import exception.NotFoundException;
import model.Specialty;
import model.Status;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.SpecialtyRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static util.constant.Constants.NOT_FOUND_SPECIALITY;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class SpecialtyServiceImplTest {
    @Mock
    private SpecialtyRepository repository;
    @InjectMocks
    private SpecialtyServiceImpl service;
    private Specialty expectedSpecialtyWithName;
    private Specialty expectedSpecialtyWithIdName;
    private NotFoundException notFoundException;

    @BeforeEach
    void setUp() {
        expectedSpecialtyWithName = getExpectedSpecialtyWithName();
        expectedSpecialtyWithIdName = getExpectedSpecialtyWithIdName();
        notFoundException = getNotFoundException();
        Mockito.reset(repository);
    }

    @Test
    void shouldSaveSpecialty() {
        when(repository.save(argThat(specialty -> specialty.equals(expectedSpecialtyWithName) && specialty.getName().equals(expectedSpecialtyWithName.getName()))))
                .thenReturn(expectedSpecialtyWithName);

        Specialty savedSpecialty = service.save(expectedSpecialtyWithName);

        assertNotNull(savedSpecialty);
        assertEquals(expectedSpecialtyWithName, savedSpecialty);

        verify(repository, atLeastOnce())
                .save(argThat(specialty -> specialty.equals(expectedSpecialtyWithName) && specialty.getName().equals(expectedSpecialtyWithName.getName())));
    }

    @Test
    void shouldUpdateSpecialty() {

        when(repository.existsById(expectedSpecialtyWithIdName.getId())).thenReturn(true);
        when(repository.update(argThat(specialty -> specialty.getId().equals(expectedSpecialtyWithIdName.getId()) && specialty.getName().equals(expectedSpecialtyWithIdName.getName()))))
                .thenReturn(expectedSpecialtyWithIdName);

        Specialty updatedSpecialty = service.update(expectedSpecialtyWithIdName);

        assertNotNull(updatedSpecialty);
        assertEquals(expectedSpecialtyWithIdName, updatedSpecialty);

        verify(repository, atLeastOnce()).existsById(expectedSpecialtyWithIdName.getId());
        verify(repository, atLeastOnce()).update(argThat(specialty -> specialty.getId().equals(expectedSpecialtyWithIdName.getId()) && specialty.getName().equals(expectedSpecialtyWithIdName.getName())));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUpdateSpecialty() {

        when(repository.existsById(expectedSpecialtyWithIdName.getId())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> service.update(expectedSpecialtyWithIdName));

        verify(repository, times(1)).existsById(expectedSpecialtyWithIdName.getId());
        verify(repository, never()).update(expectedSpecialtyWithIdName);

        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldFindByIdSpecialty() {
        when(repository.findById(expectedSpecialtyWithIdName.getId())).thenReturn(Optional.ofNullable(expectedSpecialtyWithIdName));

        Specialty foundSpecialty = service.findById(1);

        assertNotNull(foundSpecialty);
        assertEquals(expectedSpecialtyWithIdName, foundSpecialty);

        verify(repository, times(1)).findById(1);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenFindByIdSpecialty() {
        when(repository.findById(expectedSpecialtyWithIdName.getId())).thenThrow(notFoundException);

        assertThrows(NotFoundException.class, () -> service.findById(1));

        verify(repository, times(1)).findById(1);
    }

    @Test
    void shouldReturnTrueIfSpecialtyExistsWhenExistsById() {
        when(repository.existsById(expectedSpecialtyWithIdName.getId())).thenReturn(true);

        boolean existsSpecialty = service.existsById(expectedSpecialtyWithIdName.getId());

        assertTrue(existsSpecialty);

        verify(repository, times(1)).existsById(expectedSpecialtyWithIdName.getId());
    }

    @Test
    void shouldReturnFalseIfSpecialtyDoesNotExistsWhenExistsById() {
        when(repository.existsById(expectedSpecialtyWithIdName.getId())).thenReturn(false);

        boolean existsSpecialty = service.existsById(expectedSpecialtyWithIdName.getId());

        assertFalse(existsSpecialty);

        verify(repository, times(1)).existsById(expectedSpecialtyWithIdName.getId());
    }

    @Test
    void findAllSpecialties() {
        when(repository.findAll()).thenReturn(getExpectedSpecialties());

        List<Specialty> allFoundSpecialties = service.findAll();

        assertNotNull(allFoundSpecialties);
        assertFalse(allFoundSpecialties.isEmpty());
        assertEquals(getExpectedSpecialties(), allFoundSpecialties);
        assertEquals(2, allFoundSpecialties.size());

        verify(repository, times(1)).findAll();
    }

    @Test
    void shouldDeleteByIdSpecialty() {
        when(repository.existsById(expectedSpecialtyWithIdName.getId())).thenReturn(true);
        doNothing().when(repository).deleteById(expectedSpecialtyWithIdName.getId());

        service.deleteById(expectedSpecialtyWithIdName.getId());

        verify(repository, times(1)).deleteById(expectedSpecialtyWithIdName.getId());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenDeleteByIdSpecialty() {
        Integer deleteBySpecialtyId = expectedSpecialtyWithIdName.getId();

        when(repository.existsById(deleteBySpecialtyId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> service.deleteById(deleteBySpecialtyId));

        verify(repository, times(1)).existsById(expectedSpecialtyWithIdName.getId());
        verify(repository, never()).deleteById(expectedSpecialtyWithIdName.getId());
    }

    @Test
    void shouldDeleteAllSpecialties() {
        doNothing().when(repository).deleteAll();

        service.deleteAll();

        verify(repository, times(1)).deleteAll();
    }


    private Specialty getExpectedSpecialtyWithName() {
        return new Specialty("any");
    }

    private Specialty getExpectedSpecialtyWithIdName() {
        return new Specialty(1, "any");
    }

    private NotFoundException getNotFoundException() {
        return new NotFoundException(NOT_FOUND_SPECIALITY);
    }

    private List<Specialty> getExpectedSpecialties() {
        return new LinkedList<>(List.of(
                new Specialty(1, "any", Status.ACTIVE),
                new Specialty(2, "any", Status.ACTIVE)
        ));
    }
}