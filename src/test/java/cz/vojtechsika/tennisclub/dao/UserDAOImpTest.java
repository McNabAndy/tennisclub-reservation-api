package cz.vojtechsika.tennisclub.dao;

import cz.vojtechsika.tennisclub.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
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
class UserDAOImpTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<User> query;

    @InjectMocks
    private UserDAOImp userDAO;


    @Test
    @DisplayName("Save user to database")
    void save_returnSavedUserEntityFromDatabase() {

        // Arrange
        User user = new User();

        // Act
        User actual = userDAO.save(user);

        // Assert
        assertEquals(user, actual,"Objets should be same");

        //
        verify(entityManager, times(1)).persist(user);


    }

    @Test
    @DisplayName("Fetch user from database by valid phone number")
    void findByPhone_userExists_returnUsersEntityFromDatabase() {

        // Arrange
        String phone = "123456789";
        User user = new User();
        List<User> users = List.of(user);

        when(entityManager.createQuery(anyString(), eq(User.class))).thenReturn(query);
        when(query.setParameter(eq("phone"), eq(phone))).thenReturn(query);
        when(query.setParameter(eq("isFalse"), eq(false))).thenReturn(query);
        when(query.getResultList()).thenReturn(users);

        // Act
        Optional<User> actual = userDAO.findByPhone(phone);

        // Assert
        assertTrue(actual.isPresent());
        assertEquals(user, actual.get(),"Objets should be same");


    }

    @Test
    @DisplayName("Fetch user from database by invalid phone number")
    void findByPhone_userDoesNotExists_returnEmptyOptional() {

        // Arrange
        String phone = "123456789";
        User user = new User();
        List<User> users = List.of();

        when(entityManager.createQuery(anyString(), eq(User.class))).thenReturn(query);
        when(query.setParameter(eq("phone"), eq(phone))).thenReturn(query);
        when(query.setParameter(eq("isFalse"), eq(false))).thenReturn(query);
        when(query.getResultList()).thenReturn(users);

        // Act
        Optional<User> actual = userDAO.findByPhone(phone);

        // Assert
        assertTrue(actual.isEmpty());

    }
}