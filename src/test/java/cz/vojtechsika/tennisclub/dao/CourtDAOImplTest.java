package cz.vojtechsika.tennisclub.dao;

import cz.vojtechsika.tennisclub.entity.Court;
import cz.vojtechsika.tennisclub.entity.SurfaceType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourtDAOImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Court> query;

    @InjectMocks
    private CourtDAOImpl courtDAO;


    @Test
    @DisplayName("Create new court")
    void save_returnSavedNewCourtFromDatabase() {

        // Arrange
        Court court = new Court();

        // Act
        Court actual = courtDAO.save(court);

        // Assert
        assertEquals(court, actual,"Court should be the same");

        // verify
        verify(entityManager, times(1)).persist(court);

    }

    @Test
    @DisplayName("Fetch court by id")
    void findById_validId_returnCourtFromDatabase() {

        // Arrange
        Long courtId = 1L;

        when(entityManager.createQuery(anyString(), eq(Court.class))).thenReturn(query);
        when(query.setParameter(eq("id"), eq(courtId))).thenReturn(query);
        when(query.setParameter(eq("isFalse"), eq(false))).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(new Court()));

        // Act
        Optional<Court> actual = courtDAO.findById(courtId);

        // Assert
        assertTrue(actual.isPresent(), "Court should be found");

    }


    @Test
    @DisplayName("Fetch court by invalid id")
    void findById_inValidId_returnOptional() {
        Long courtId = 999L;

        when(entityManager.createQuery(anyString(), eq(Court.class))).thenReturn(query);
        when(query.setParameter(eq("id"), eq(courtId))).thenReturn(query);
        when(query.setParameter(eq("isFalse"), eq(false))).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of());

        // Act
        Optional<Court> actual = courtDAO.findById(courtId);

        // Assert
        assertTrue(actual.isEmpty(), "Court should not be found");




    }


    @Test
    @DisplayName("Fetch court by court number")
    void findByCourtNumber_validCourNumber_returnCourtFromDatabase() {

        // Arrange
        int courtNumber = 101;

        when(entityManager.createQuery(anyString(), eq(Court.class))).thenReturn(query);
        when(query.setParameter(eq("courtNumber"), eq(courtNumber))).thenReturn(query);
        when(query.setParameter(eq("isFalse"), eq(false))).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(new Court()));

        // Act
        Optional<Court> actual = courtDAO.findByCourtNumber(courtNumber);

        // Assert
        assertTrue(actual.isPresent(), "Court should be found");
    }


    @Test
    @DisplayName("Fetch court by invalid court number")
    void findByCourtNumber_inValidCourNumber_returnOptional() {

        // Arrange
        int courtNumber = 999;

        when(entityManager.createQuery(anyString(), eq(Court.class))).thenReturn(query);
        when(query.setParameter(eq("courtNumber"), eq(courtNumber))).thenReturn(query);
        when(query.setParameter(eq("isFalse"), eq(false))).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of());

        // Act
        Optional<Court> actual = courtDAO.findByCourtNumber(courtNumber);

        // Assert
        assertTrue(actual.isEmpty(), "Court should be found");
    }

    @Test
    @DisplayName("Fetch all court")
    void findAll_returnCourtFromDatabase() {

        // Arrange
        Court court1 = new Court();
        court1.setId(1L);

        Court court2 = new Court();
        court2.setId(2L);

        List<Court> courts = List.of(court1, court2);

        when(entityManager.createQuery(anyString(), eq(Court.class))).thenReturn(query);
        when(query.setParameter(eq("isFalse"), eq(false))).thenReturn(query);
        when(query.getResultList()).thenReturn(courts);

        // Act
        List<Court> actual = courtDAO.findAll();

        // Assert
        assertEquals(courts, actual,"Object should be the same");

    }

    @Test
    @DisplayName("Fetch all court by surface type id")
    void findAllBySurfaceTypeId_validSurfaceTypeId_returnCourtFromDatabase() {

        // Arrange
        Long surfaceTypeId = 3L;

        SurfaceType surfaceType = new SurfaceType();
        surfaceType.setId(surfaceTypeId);

        Court court1 = new Court();
        court1.setId(1L);
        court1.setSurfaceType(surfaceType);

        Court court2 = new Court();
        court2.setId(2L);
        court2.setSurfaceType(surfaceType);

        List<Court> courts = List.of(court1, court2);

        when(entityManager.createQuery(anyString(), eq(Court.class))).thenReturn(query);
        when(query.setParameter(eq("id"),eq(surfaceTypeId))).thenReturn(query);
        when(query.setParameter(eq("isFalse"), eq(false))).thenReturn(query);
        when(query.getResultList()).thenReturn(courts);

        // Act
        List<Court> actual = courtDAO.findAllBySurfaceTypeId(surfaceTypeId);

        // Assert
        assertEquals(courts, actual,"Object should be the same");
        assertEquals(surfaceType, actual.get(0).getSurfaceType(),"Object should be the same");
        assertEquals(surfaceType, actual.get(1).getSurfaceType(),"Object should be the same");

    }


    @Test
    @DisplayName("Update court")
    void update_returnUpdatedCourtFromDatabase() {

        // Arrange
        Court court = new Court();
        when(entityManager.merge(court)).thenReturn(court);

        // Act
        Court actual = courtDAO.update(court);

        // Assert
        assertEquals(court, actual,"Court should be the same");

        // verify
        verify(entityManager, times(1)).merge(court);

    }

}