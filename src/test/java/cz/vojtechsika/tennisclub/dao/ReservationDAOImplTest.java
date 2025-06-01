package cz.vojtechsika.tennisclub.dao;

import cz.vojtechsika.tennisclub.entity.Court;
import cz.vojtechsika.tennisclub.entity.Reservation;
import cz.vojtechsika.tennisclub.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationDAOImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Reservation> query;

    @InjectMocks
    private ReservationDAOImpl reservationDAO;



    @Test
    @DisplayName("Create Reservation")
    void create_returnSavedReservationEntityFromDatabase() {

        // Arrange
        Reservation reservation = new Reservation();

        // Act
        Reservation actual = reservationDAO.create(reservation);

        // Assert
        assertEquals(reservation, actual,"Object should be the same");

        // Verify
        verify(entityManager, times(1)).persist(reservation);
    }

    @Test
    @DisplayName("Fetch all reservation by court excluding itself")
    void findAllByDateAndCourtNumber_validCourtNumberExcludeItself_returnListOfReservationsFromDatabaseExcludeItself() {

        // Arrange
        LocalDateTime date = LocalDateTime.now();
        int courtNumber = 101;
        Long excludeId = null;

        Court court = new Court();
        court.setCourtNumber(courtNumber);

        Reservation reservation1 = new Reservation();
        reservation1.setCourt(court);

        Reservation reservation2 = new Reservation();
        reservation2.setCourt(court);

        List<Reservation> reservations = List.of(reservation1, reservation2);

        when(entityManager.createQuery(anyString(), eq(Reservation.class))).thenReturn(query);
        when(query.setParameter(eq("from"),any(LocalDateTime.class))).thenReturn(query);
        when(query.setParameter(eq("to"),any(LocalDateTime.class))).thenReturn(query);
        when(query.setParameter(eq("isFalse"), eq(false))).thenReturn(query);
        when(query.setParameter(eq("courtNumber"), eq(courtNumber))).thenReturn(query);
        when(query.getResultList()).thenReturn(reservations);

        // Act
        List<Reservation> actual = reservationDAO.findAllByDateAndCourtNumber(date, courtNumber, excludeId);

        // Assert
        assertEquals(reservations, actual,"Object should be the same");
        assertEquals(reservations.size(), actual.size(),"Object should be the same");


    }

    @Test
    @DisplayName("Fetch all reservation by court")
    void findAllByDateAndCourtNumber_validDateAndCourtNumber_returnListOfReservationsFromDatabase() {

        // Arrange
        LocalDateTime date = LocalDateTime.now();
        int courtNumber = 101;
        Long excludeId = 1L;

        Court court = new Court();
        court.setCourtNumber(courtNumber);

        Reservation reservation1 = new Reservation();
        reservation1.setCourt(court);

        Reservation reservation2 = new Reservation();
        reservation2.setCourt(court);

        List<Reservation> reservations = List.of(reservation1, reservation2);

        when(entityManager.createQuery(anyString(), eq(Reservation.class))).thenReturn(query);
        when(query.setParameter(eq("from"),any(LocalDateTime.class))).thenReturn(query);
        when(query.setParameter(eq("to"),any(LocalDateTime.class))).thenReturn(query);
        when(query.setParameter(eq("isFalse"), eq(false))).thenReturn(query);
        when(query.setParameter(eq("courtNumber"), eq(courtNumber))).thenReturn(query);
        when(query.setParameter(eq("excludeId"), eq(excludeId))).thenReturn(query);
        when(query.getResultList()).thenReturn(reservations);

        // Act
        List<Reservation> actual = reservationDAO.findAllByDateAndCourtNumber(date, courtNumber, excludeId);

        // Assert
        assertEquals(reservations, actual,"Object should be the same");
        assertEquals(reservations.size(), actual.size(),"Object should be the same");


    }

    @Test
    @DisplayName("Fetch reservation by valid Id")
    void findById_validId_returnReservationFromDatabase() {

        // Arrange
        Long reservationId = 1L;

        Reservation reservation = new Reservation();
        reservation.setId(reservationId);

        when(entityManager.createQuery(anyString(), eq(Reservation.class))).thenReturn(query);
        when(query.setParameter(eq("id"), eq(reservationId))).thenReturn(query);
        when(query.setParameter(eq("isFalse"), eq(false))).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(reservation));

        // Act
        Optional<Reservation> actual = reservationDAO.findById(reservationId);

        // Assert
        assertTrue(actual.isPresent(), "Object should be the same");
        assertEquals(reservation, actual.get(),"Object should be the same");

    }

    @Test
    @DisplayName("Fetch reservation by invalid Id")
    void findById_inValidId_returnOptional() {

        // Arrange
        Long reservationId = 1L;

        Reservation reservation = new Reservation();
        reservation.setId(reservationId);

        when(entityManager.createQuery(anyString(), eq(Reservation.class))).thenReturn(query);
        when(query.setParameter(eq("id"), eq(reservationId))).thenReturn(query);
        when(query.setParameter(eq("isFalse"), eq(false))).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of());

        // Act
        Optional<Reservation> actual = reservationDAO.findById(reservationId);

        // Assert
        assertTrue(actual.isEmpty(), "Object should be the same");

    }

    @Test
    @DisplayName("Fetch all reservation by court number")
    void findAllByCourtNumber_validCourtNumber_returnListOfReservationsFromDatabase() {

        // Arrange
        int courtNumber = 101;

        User user = new User();

        Court court = new Court();
        court.setCourtNumber(courtNumber);

        Reservation reservation1 = new Reservation();
        reservation1.setCourt(court);
        reservation1.setUser(user);

        Reservation reservation2 = new Reservation();
        reservation2.setCourt(court);
        reservation2.setUser(user);

        List<Reservation> reservations = List.of(reservation1, reservation2);

        when(entityManager.createQuery(anyString(), eq(Reservation.class))).thenReturn(query);
        when(query.setParameter(eq("courtNumber"), eq(courtNumber))).thenReturn(query);
        when(query.setParameter(eq("isFalse"), eq(false))).thenReturn(query);
        when(query.getResultList()).thenReturn(reservations);

        // Act
        List<Reservation> actual = reservationDAO.findAllByCourtNumber(courtNumber);

        // Assert
        assertEquals(reservations, actual,"Object should be the same");
        assertEquals(reservations.size(), actual.size(),"Object should be the same");


    }

    @Test
    @DisplayName("Fetch all reservation in future by phone number")
    void findAllByPhoneNumber_validPhoneNumberFutureOnly_returnListOfReservationsFromDatabase() {

        // Arrange
        String phoneNumber = "123456789";
        int courtNumber = 101;
        boolean futureOnly = true;

        User user = new User();
        user.setPhoneNumber(phoneNumber);

        Court court = new Court();
        court.setCourtNumber(courtNumber);

        Reservation reservation1 = new Reservation();
        reservation1.setCourt(court);
        reservation1.setUser(user);

        Reservation reservation2 = new Reservation();
        reservation2.setCourt(court);
        reservation2.setUser(user);

        List<Reservation> reservations = List.of(reservation1, reservation2);

        when(entityManager.createQuery(anyString(), eq(Reservation.class))).thenReturn(query);
        when(query.setParameter(eq("phoneNumber"), eq(phoneNumber))).thenReturn(query);
        when(query.setParameter(eq("isFalse"), eq(false))).thenReturn(query);
        when(query.setParameter(eq("from"), any(LocalDateTime.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(reservations);

        // Act
        List<Reservation> actual = reservationDAO.findAllByPhoneNumber(phoneNumber, futureOnly);

        // Assert
        assertEquals(reservations, actual,"Object should be the same");
        assertEquals(reservations.size(), actual.size(),"Object should be the same");
        assertEquals(reservation1.getUser().getPhoneNumber(),
                actual.get(0).getUser().getPhoneNumber(),"Object should be the same");
    }

    @Test
    @DisplayName("Fetch all reservation by phone number")
    void findAllByPhoneNumber_validPhoneNumber_returnListOfReservationsFromDatabase() {

        // Arrange
        String phoneNumber = "123456789";
        int courtNumber = 101;
        boolean futureOnly = false;

        User user = new User();
        user.setPhoneNumber(phoneNumber);

        Court court = new Court();
        court.setCourtNumber(courtNumber);

        Reservation reservation1 = new Reservation();
        reservation1.setCourt(court);
        reservation1.setUser(user);

        Reservation reservation2 = new Reservation();
        reservation2.setCourt(court);
        reservation2.setUser(user);

        List<Reservation> reservations = List.of(reservation1, reservation2);

        when(entityManager.createQuery(anyString(), eq(Reservation.class))).thenReturn(query);
        when(query.setParameter(eq("phoneNumber"), eq(phoneNumber))).thenReturn(query);
        when(query.setParameter(eq("isFalse"), eq(false))).thenReturn(query);
        when(query.getResultList()).thenReturn(reservations);

        // Act
        List<Reservation> actual = reservationDAO.findAllByPhoneNumber(phoneNumber, futureOnly);

        // Assert
        assertEquals(reservations, actual,"Object should be the same");
        assertEquals(reservations.size(), actual.size(),"Object should be the same");
        assertEquals(reservation1.getUser().getPhoneNumber(),
                actual.get(0).getUser().getPhoneNumber(),"Object should be the same");
    }

    @Test
    @DisplayName("Fetch All existing reservation from database")
    void findAll_returnListOfReservationsFromDatabase() {

        // Arrange
        int courtNumber = 101;

        Court court = new Court();
        court.setCourtNumber(courtNumber);

        Reservation reservation1 = new Reservation();
        reservation1.setCourt(court);

        Reservation reservation2 = new Reservation();
        reservation2.setCourt(court);

        List<Reservation> reservations = List.of(reservation1, reservation2);

        when(entityManager.createQuery(anyString(), eq(Reservation.class))).thenReturn(query);
        when(query.setParameter(eq("isFalse"), eq(false))).thenReturn(query);
        when(query.getResultList()).thenReturn(reservations);

        // Act
        List<Reservation> actual = reservationDAO.findAll();

        // Assert
        assertEquals(reservations, actual,"Object should be the same");

    }

    @Test
    void update() {

        // Arrange
        Reservation reservation = new Reservation();
        when(entityManager.merge(reservation)).thenReturn(reservation);

        // Act
        Reservation actual = reservationDAO.update(reservation);

        // Assert
        assertEquals(reservation, actual,"Object should be the same");

        // Verify
        verify(entityManager, times(1)).merge(reservation);


    }
}