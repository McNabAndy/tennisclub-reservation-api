package cz.vojtechsika.tennisclub.dto.mapper;

import cz.vojtechsika.tennisclub.dto.ReservationDTO;
import cz.vojtechsika.tennisclub.dto.response.ReservationResponseDTO;
import cz.vojtechsika.tennisclub.entity.Court;
import cz.vojtechsika.tennisclub.entity.Reservation;
import cz.vojtechsika.tennisclub.entity.User;
import cz.vojtechsika.tennisclub.enums.GameType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import static org.junit.jupiter.api.Assertions.*;

class ReservationMapperTest {

    private ReservationMapper reservationMapper;

    @BeforeEach
    void setUp() {
        reservationMapper = new ReservationMapper();

    }

    @Test
    @DisplayName("Map from ReservationDTO to Reservation")
    void toReservationEntity() {

        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setStartTime(LocalDateTime.now().plusHours(1));
        reservationDTO.setEndTime(LocalDateTime.now().plusHours(2));
        reservationDTO.setGameType(GameType.SINGLES);

        // Act
        Reservation actualReservation = reservationMapper.toReservationEntity(reservationDTO);

        // Assert
        assertEquals(reservationDTO.getGameType(), actualReservation.getGameType(),
                "Game type mismatch");

        assertEquals(reservationDTO.getStartTime(), actualReservation.getStartTime(),
                "Start time mismatch" );

        assertEquals(reservationDTO.getEndTime(), actualReservation.getEndTime(),
                "End time mismatch" );

        assertNotNull(actualReservation.getCreatedAt(),
                "Created at should not be null");

        assertFalse(actualReservation.isDeleted(),
                "Deleted should not be true");

    }

    @Test
    @DisplayName("Map from Reservation to ReservationResponseDTO")
    void toReservationResponseDTO() {



        // Arrange
        User user = new User();
        user.setUserName("John");
        user.setPhoneNumber("1234567890");

        Court court = new Court();
        court.setCourtNumber(101);

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setUser(user);
        reservation.setCourt(court);
        reservation.setStartTime(LocalDateTime.now().plusHours(1));
        reservation.setEndTime(LocalDateTime.now().plusHours(2));
        reservation.setGameType(GameType.SINGLES);
        reservation.setPrice(BigDecimal.valueOf(10));
        reservation.setCreatedAt(LocalDateTime.now());

        // Act
        ReservationResponseDTO actualReservationResponseDTO = reservationMapper.toReservationResponseDTO(reservation);

        // Assert
        assertEquals(reservation.getId(), actualReservationResponseDTO.getId(),
                "Id mismatch");

        assertEquals(reservation.getUser().getUserName(), actualReservationResponseDTO.getUserName(),
                "User mismatch");

        assertEquals(reservation.getPrice(), actualReservationResponseDTO.getPrice(),
                "Price mismatch");

        assertEquals(reservation.getGameType(), actualReservationResponseDTO.getGameType(),
                "Game type mismatch");

        assertEquals(reservation.getStartTime().toLocalTime().
                format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)), actualReservationResponseDTO.getStartTime(),
                "Start time mismatch");

        assertEquals(reservation.getEndTime().toLocalTime().
                format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)), actualReservationResponseDTO.getEndTime(),
                "Start time mismatch");

        assertEquals(reservation.getStartTime().toLocalDate().
                format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)), actualReservationResponseDTO.getGameDate(),
                "Game date mismatch");

        assertEquals(reservation.getCreatedAt().toLocalDate().
                format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)), actualReservationResponseDTO.getCreatedAt(),
                "Created at mismatch");




















    }

    @Test
    @DisplayName("Map ReservationDTO to updated Reservation")
    void updateReservationFromDTO_returnUpdatedReservation() {

        // Arrange
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setStartTime(LocalDateTime.now().plusHours(1));
        reservationDTO.setEndTime(LocalDateTime.now().plusHours(2));
        reservationDTO.setGameType(GameType.SINGLES);

        Reservation reservation = new Reservation();

        // Act
        Reservation actualReservation = reservationMapper.updateReservationFromDTO(reservationDTO,reservation);

        // Assert
        assertEquals(reservationDTO.getStartTime(), actualReservation.getStartTime(),
                "Start time mismatch");

        assertEquals(reservationDTO.getEndTime(), actualReservation.getEndTime(),
                "End time mismatch");

        assertEquals(reservationDTO.getGameType(), actualReservation.getGameType(),
                "Game type mismatch");


    }
}