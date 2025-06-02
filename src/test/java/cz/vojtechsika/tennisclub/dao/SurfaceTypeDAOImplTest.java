package cz.vojtechsika.tennisclub.dao;

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
class SurfaceTypeDAOImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<SurfaceType> query;

    @InjectMocks
    private SurfaceTypeDAOImpl surfaceTypeDAO;

    @Test
    @DisplayName("Save surface type to database")
    void save_returnSavedSurfaceTypesFromDatabase() {

        // Arrange
        SurfaceType surfaceType = new SurfaceType();

        // Act
        SurfaceType actual = surfaceTypeDAO.save(surfaceType);

        // Assert
        assertEquals(surfaceType, actual,"Objets should be same");

        // Verify
        verify(entityManager, times(1)).persist(surfaceType);

    }

    @Test
    @DisplayName("Fetch surface type by valid id")
    void findById_validId_returnsEntityFromDatabase() {

        // Arrange
        Long surfaceTypeId = 1L;

        SurfaceType surfaceType = new SurfaceType();
        List<SurfaceType> surfaceTypes = List.of(surfaceType);

        when(entityManager.createQuery(anyString(), eq(SurfaceType.class))).thenReturn(query);
        when(query.setParameter(eq("id"), eq(surfaceTypeId))).thenReturn(query);
        when(query.setParameter(eq("isFalse"),eq(false))).thenReturn(query);
        when(query.getResultList()).thenReturn(surfaceTypes);

        // Act
        Optional<SurfaceType> actual = surfaceTypeDAO.findById(surfaceTypeId);

        // Assert
        assertTrue(actual.isPresent());
        assertEquals(surfaceType, actual.get(), "Objets should be same");

    }

    @Test
    @DisplayName("Fetch surface type by invalid id")
    void findById_inValidId_returnEmptyOptional() {

        // Arrange
        Long surfaceTypeId = 1L;

        List<SurfaceType> surfaceTypes = List.of();

        when(entityManager.createQuery(anyString(), eq(SurfaceType.class))).thenReturn(query);
        when(query.setParameter(eq("id"), eq(surfaceTypeId))).thenReturn(query);
        when(query.setParameter(eq("isFalse"),eq(false))).thenReturn(query);
        when(query.getResultList()).thenReturn(surfaceTypes);

        // Act
        Optional<SurfaceType> actual = surfaceTypeDAO.findById(surfaceTypeId);

        // Assert
        assertTrue(actual.isEmpty());

    }

    @Test
    @DisplayName("Fetch all surface types")
    void findAll_returnListOfSurfaceTypesFromDatabase() {

        // Arrange
        SurfaceType surfaceType1 = new SurfaceType();
        surfaceType1.setName("Grass");

        SurfaceType surfaceType2 = new SurfaceType();
        surfaceType2.setName("Clay");

        List<SurfaceType> surfaceTypes = List.of(surfaceType1, surfaceType2);

        when(entityManager.createQuery(anyString(), eq(SurfaceType.class))).thenReturn(query);
        when(query.setParameter(eq("isFalse"), eq(false))).thenReturn(query);
        when(query.getResultList()).thenReturn(surfaceTypes);

        // Act
        List<SurfaceType> actual = surfaceTypeDAO.findAll();

        // Assert
        assertEquals(2, actual.size(), "Objets should be same");
        assertEquals(surfaceTypes, actual, "Objets should be same");

    }

    @Test
    void update() {

        // Arrange
        SurfaceType surfaceType = new SurfaceType();
        when(entityManager.merge(surfaceType)).thenReturn(surfaceType);

        // Act
        SurfaceType actual = surfaceTypeDAO.update(surfaceType);

        // Assert
        assertEquals(surfaceType, actual,"Objets should be same");

        //
        verify(entityManager, times(1)).merge(surfaceType);

    }
}