package cz.vojtechsika.tennisclub.service;

import cz.vojtechsika.tennisclub.dao.CourtDAO;
import cz.vojtechsika.tennisclub.dao.ReservationDAO;
import cz.vojtechsika.tennisclub.dao.UserDAO;
import cz.vojtechsika.tennisclub.dto.ReservationDTO;
import cz.vojtechsika.tennisclub.dto.mapper.ReservationMapper;
import cz.vojtechsika.tennisclub.dto.mapper.UserMapper;
import cz.vojtechsika.tennisclub.dto.response.ReservationResponseDTO;
import cz.vojtechsika.tennisclub.entity.Court;
import cz.vojtechsika.tennisclub.entity.Reservation;
import cz.vojtechsika.tennisclub.entity.SurfaceType;
import cz.vojtechsika.tennisclub.entity.User;
import cz.vojtechsika.tennisclub.enums.GameType;
import cz.vojtechsika.tennisclub.exception.ReservationNotFoundException;
import cz.vojtechsika.tennisclub.exception.ReservationValidationException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReservationServiceImplTest {

    @Mock
    private ReservationDAO reservationDAO;

    @Mock
    private UserDAO userDAO;

    @Mock
    private CourtDAO courtDAO;

    @Mock
    private ReservationMapper reservationMapper;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    ReservationServiceImpl reservationService;



    @Test
    @DisplayName("Create new Reservation and User in valid date for (SINGLES)")
    void createReservation_newReservationForNewUserWithValidDateAndTime_ShouldReturnNewReservationResponseDTO() {

        // Arrange
        int courtNumber = 101;

        LocalDateTime startTime = LocalDateTime.of(LocalDate.now().
                plusDays(1), LocalTime.of(10,0));
        LocalDateTime endTime = startTime.plusHours(2);

        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setStartTime(startTime);
        reservationDTO.setEndTime(endTime);
        reservationDTO.setCourtNumber(courtNumber);
        reservationDTO.setGameType(GameType.SINGLES);

        Reservation reservation1 = new Reservation();

        Reservation reservation2 = new Reservation();
        reservation2.setStartTime(startTime.plusHours(2));
        reservation2.setEndTime(endTime.plusHours(2));

        Reservation reservation3 = new Reservation();
        reservation3.setStartTime(startTime.plusHours(4));
        reservation3.setEndTime(endTime.plusHours(4));

        Reservation newReservation = new Reservation();
        newReservation.setStartTime(startTime);
        newReservation.setEndTime(endTime);
        newReservation.setPrice(BigDecimal.valueOf(120));

        List<Reservation> reservations = List.of(reservation2, reservation3);

        SurfaceType surfaceType = new SurfaceType();
        surfaceType.setMinutePrice(BigDecimal.valueOf(1));

        Court court = new Court();
        court.setCourtNumber(courtNumber);
        court.setSurfaceType(surfaceType);

        Optional<Court> optionalCourt = Optional.of(court);

        User user = new User();
        Optional<User> optionalUser = Optional.empty();

        ReservationResponseDTO reservationResponseDTO = new ReservationResponseDTO();
        reservationResponseDTO.setPrice(BigDecimal.valueOf(120));

        when(reservationDAO.findAllByDateAndCourtNumber(startTime,courtNumber,null)).thenReturn(reservations);
        when(courtDAO.findByCourtNumber(courtNumber)).thenReturn(optionalCourt);

        when(userDAO.findByPhone(reservationDTO.getPhoneNumber())).thenReturn(optionalUser);
        when(reservationMapper.toReservationEntity(reservationDTO)).thenReturn(reservation1);

        when(userMapper.mapFromReservationDTO(reservationDTO)).thenReturn(user);
        when(reservationDAO.create(reservation1)).thenReturn(newReservation);
        when(reservationMapper.toReservationResponseDTO(newReservation)).thenReturn(reservationResponseDTO);

        // Act
        ReservationResponseDTO actual = reservationService.createReservation(reservationDTO);
        BigDecimal actualCalculatePrice = reservationResponseDTO.getPrice();

        // Assert
        assertEquals(reservationResponseDTO, actual, "Should return same object");
        assertEquals(reservationResponseDTO.getPrice(), actualCalculatePrice, "120 * 1 should be ");

        // Verify
        verify(userDAO, times(1)).save(user);
    }

    @Test
    @DisplayName("Throw exception if reservation duration is longer than 2 hours (SINGLES)")
    void createReservation_newReservationForNewUserWithInvalidDateAndTime_ShouldReturnReservationValidationException() {

        // Arrange
        int courtNumber = 101;

        // Duration between start time and end time is more than 120 minutes
        LocalDateTime startTime = LocalDateTime.of(LocalDate.now().
                plusDays(1), LocalTime.of(10,0));
        LocalDateTime endTime = startTime.plusHours(3);

        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setStartTime(startTime);     // 10:00
        reservationDTO.setEndTime(endTime);         // 13:00
        reservationDTO.setCourtNumber(courtNumber);
        reservationDTO.setGameType(GameType.SINGLES);

        Reservation reservation = new Reservation();

        Reservation reservation2 = new Reservation();
        reservation2.setStartTime(startTime.plusHours(2));
        reservation2.setEndTime(endTime.plusHours(2));

        Reservation reservation3 = new Reservation();
        reservation3.setStartTime(startTime.plusHours(4));
        reservation3.setEndTime(endTime.plusHours(4));

        List<Reservation> reservations = List.of(reservation2, reservation3);

        User user = new User();

        when(reservationDAO.findAllByDateAndCourtNumber(startTime,courtNumber,null)).thenReturn(reservations);

        // Act and Assert
        assertThrows(ReservationValidationException.class, () -> reservationService.createReservation(reservationDTO),
                "Should throw ReservationValidationException");

        // Verify
        verify(userDAO, never()).save(user);
        verify(reservationDAO, never()).create(reservation);

    }

    @Test
    @DisplayName("Throw exception if reservation date and time is in past (SINGLES)")
    void createReservation_newReservationForNewUserWithPastDateAndTime_ShouldReturnReservationValidationException() {

        int courtNumber = 101;

        // Past Date and Time
        LocalDateTime startTime = LocalDateTime.of(LocalDate.now().
                minusDays(1), LocalTime.of(10,0));
        LocalDateTime endTime = startTime.plusHours(2);

        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setStartTime(startTime);
        reservationDTO.setEndTime(endTime);
        reservationDTO.setCourtNumber(courtNumber);
        reservationDTO.setGameType(GameType.SINGLES);

        Reservation reservation = new Reservation();

        Reservation reservation2 = new Reservation();
        reservation2.setStartTime(startTime.plusHours(2));
        reservation2.setEndTime(endTime.plusHours(2));

        Reservation reservation3 = new Reservation();
        reservation3.setStartTime(startTime.plusHours(4));
        reservation3.setEndTime(endTime.plusHours(4));

        List<Reservation> reservations = List.of(reservation2, reservation3);

        User user = new User();

        when(reservationDAO.findAllByDateAndCourtNumber(startTime,courtNumber,null)).thenReturn(reservations);

        // Act and Assert
        assertThrows(ReservationValidationException.class, () -> reservationService.createReservation(reservationDTO),
                "Should throw ReservationValidationException");

        // Verify
        verify(userDAO, never()).save(user);
        verify(reservationDAO, never()).create(reservation);

    }

    @Test
    @DisplayName("Throw exception if reservation has overlapping Reservations (SINGLES)")
    void createReservation_newReservationForNewUserWithOverLappingReservations_ShouldReturnReservationValidationException() {

        int courtNumber = 101;

        LocalDateTime startTime = LocalDateTime.of(LocalDate.now().
                minusDays(1), LocalTime.of(10,0));  // 10:00
        LocalDateTime endTime = startTime.plusHours(2);                       // 12:00


        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setStartTime(startTime);  // 10:00
        reservationDTO.setEndTime(endTime);      // 12:00
        reservationDTO.setCourtNumber(courtNumber);
        reservationDTO.setGameType(GameType.SINGLES);

        Reservation reservation = new Reservation();

        // Over lapping reservation
        Reservation reservation2 = new Reservation();
        reservation2.setStartTime(startTime.plusMinutes(30));  // 10:30
        reservation2.setEndTime(endTime);                      // 12:00

        Reservation reservation3 = new Reservation();
        reservation3.setStartTime(startTime.plusHours(4));  // 14:00
        reservation3.setEndTime(endTime.plusHours(4));      // 16:00

        List<Reservation> reservations = List.of(reservation2, reservation3);

        User user = new User();

        when(reservationDAO.findAllByDateAndCourtNumber(startTime,courtNumber,null)).thenReturn(reservations);

        // Act and Assert
        assertThrows(ReservationValidationException.class, () -> reservationService.createReservation(reservationDTO),
                "Should throw ReservationValidationException");

        // Verify
        verify(userDAO, never()).save(user);
        verify(reservationDAO, never()).create(reservation);

    }

    @Test
    @DisplayName("Create new Reservation for existing User with valid date for (SINGLES)")
    void createReservation_newReservationForExistingUserWithValidDateAndTime_ShouldReturnNewReservationResponseDTO() {

        // Arrange
        int courtNumber = 101;

        LocalDateTime startTime = LocalDateTime.of(LocalDate.now().
                plusDays(1), LocalTime.of(10,0));
        LocalDateTime endTime = startTime.plusHours(2);

        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setStartTime(startTime);
        reservationDTO.setEndTime(endTime);
        reservationDTO.setCourtNumber(courtNumber);
        reservationDTO.setGameType(GameType.SINGLES);
        reservationDTO.setUserName("John");

        Reservation reservation1 = new Reservation();

        Reservation reservation2 = new Reservation();
        reservation2.setStartTime(startTime.plusHours(2));
        reservation2.setEndTime(endTime.plusHours(2));

        Reservation reservation3 = new Reservation();
        reservation3.setStartTime(startTime.plusHours(4));
        reservation3.setEndTime(endTime.plusHours(4));

        Reservation newReservation = new Reservation();
        newReservation.setStartTime(startTime);
        newReservation.setEndTime(endTime);
        newReservation.setPrice(BigDecimal.valueOf(120));

        List<Reservation> reservations = List.of(reservation2, reservation3);

        SurfaceType surfaceType = new SurfaceType();
        surfaceType.setMinutePrice(BigDecimal.valueOf(1));

        Court court = new Court();
        court.setCourtNumber(courtNumber);
        court.setSurfaceType(surfaceType);

        Optional<Court> optionalCourt = Optional.of(court);

        User user = new User();
        user.setUserName("John");



        Optional<User> optionalUser = Optional.of(user);

        ReservationResponseDTO reservationResponseDTO = new ReservationResponseDTO();
        reservationResponseDTO.setPrice(BigDecimal.valueOf(120));

        when(reservationDAO.findAllByDateAndCourtNumber(startTime,courtNumber,null)).thenReturn(reservations);
        when(courtDAO.findByCourtNumber(courtNumber)).thenReturn(optionalCourt);

        when(userDAO.findByPhone(reservationDTO.getPhoneNumber())).thenReturn(optionalUser);
        when(reservationMapper.toReservationEntity(reservationDTO)).thenReturn(reservation1);

        when(reservationDAO.create(reservation1)).thenReturn(newReservation);
        when(reservationMapper.toReservationResponseDTO(newReservation)).thenReturn(reservationResponseDTO);

        // Act
        ReservationResponseDTO actual = reservationService.createReservation(reservationDTO);
        BigDecimal actualCalculatePrice = reservationResponseDTO.getPrice();

        // Assert
        assertEquals(reservationResponseDTO, actual, "Should return same object");
        assertEquals(reservationResponseDTO.getPrice(), actualCalculatePrice, "120 * 1 should be ");

        // Verify
        verify(userDAO, never()).save(user);
    }

    @Test
    @DisplayName("Create new Reservation for existing User with different and with valid date for (SINGLES)")
    void createReservation_newReservationForExistingUserWithDifferentNameWithValidDateAndTime_ShouldReturnNewReservationResponseDTO() {

        // Arrange
        int courtNumber = 101;

        LocalDateTime startTime = LocalDateTime.of(LocalDate.now().
                plusDays(1), LocalTime.of(10,0));
        LocalDateTime endTime = startTime.plusHours(2);

        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setStartTime(startTime);
        reservationDTO.setEndTime(endTime);
        reservationDTO.setCourtNumber(courtNumber);
        reservationDTO.setGameType(GameType.SINGLES);
        reservationDTO.setUserName("John");

        Reservation reservation1 = new Reservation();

        Reservation reservation2 = new Reservation();
        reservation2.setStartTime(startTime.plusHours(2));
        reservation2.setEndTime(endTime.plusHours(2));

        Reservation reservation3 = new Reservation();
        reservation3.setStartTime(startTime.plusHours(4));
        reservation3.setEndTime(endTime.plusHours(4));

        Reservation newReservation = new Reservation();
        newReservation.setStartTime(startTime);
        newReservation.setEndTime(endTime);
        newReservation.setPrice(BigDecimal.valueOf(120));

        List<Reservation> reservations = List.of(reservation2, reservation3);

        SurfaceType surfaceType = new SurfaceType();
        surfaceType.setMinutePrice(BigDecimal.valueOf(1));

        Court court = new Court();
        court.setCourtNumber(courtNumber);
        court.setSurfaceType(surfaceType);

        Optional<Court> optionalCourt = Optional.of(court);

        User user = new User();
        user.setUserName("Tomas");

        Optional<User> optionalUser = Optional.of(user);

        ReservationResponseDTO reservationResponseDTO = new ReservationResponseDTO();
        reservationResponseDTO.setPrice(BigDecimal.valueOf(120));

        when(reservationDAO.findAllByDateAndCourtNumber(startTime,courtNumber,null)).thenReturn(reservations);
        when(courtDAO.findByCourtNumber(courtNumber)).thenReturn(optionalCourt);

        when(userDAO.findByPhone(reservationDTO.getPhoneNumber())).thenReturn(optionalUser);
        when(reservationMapper.toReservationEntity(reservationDTO)).thenReturn(reservation1);

        when(reservationDAO.create(reservation1)).thenReturn(newReservation);
        when(reservationMapper.toReservationResponseDTO(newReservation)).thenReturn(reservationResponseDTO);

        // Act
        ReservationResponseDTO actual = reservationService.createReservation(reservationDTO);
        BigDecimal actualCalculatePrice = reservationResponseDTO.getPrice();

        // Assert
        assertEquals(reservationResponseDTO, actual, "Should return same object");
        assertEquals(reservationResponseDTO.getPrice(), actualCalculatePrice, "120 * 1 should be ");

        // Verify
        verify(userDAO, times(1)).save(user);
    }

    @Test
    @DisplayName("Get Reservation by Id")
    void getReservationById_validId_ShouldReturnReservationResponseDTO() {

        // Arrange
        Long reservationId = 1L;

        Reservation reservation = new Reservation();
        reservation.setId(reservationId);

        Optional<Reservation> optionalReservation = Optional.of(reservation);

        ReservationResponseDTO reservationResponseDTO = new ReservationResponseDTO();
        reservationResponseDTO.setId(reservationId);

        when(reservationDAO.findById(reservationId)).thenReturn(optionalReservation);
        when(reservationMapper.toReservationResponseDTO(reservation)).thenReturn(reservationResponseDTO);

        // Act
        ReservationResponseDTO actual = reservationService.getReservationById(reservationId);

        // Assert
        assertEquals(reservationResponseDTO, actual, "Should be same object");

    }

    @Test
    @DisplayName("Get Reservation by non exist Id")
    void getReservationById_inValidId_ShouldReturnReservationNotFoundException() {

        // Arrange
        Long reservationId = 1L;

        Reservation reservation = new Reservation();
        reservation.setId(reservationId);

        Optional<Reservation> optionalReservation = Optional.empty();

        when(reservationDAO.findById(reservationId)).thenReturn(optionalReservation);

        // Act and Assert

        assertThrows(ReservationNotFoundException.class, () ->
                        reservationService.getReservationById(reservationId),
                "Should throw ReservationNotFoundException");

    }

    @Test
    @DisplayName("Get all Reservations by valid court number")
    void getReservationByCourtNumber_validCourtNumber_ShouldReturnReservationResponseDTOList() {

        // Arrange
        int courtNumber = 101;

        LocalDateTime startTime = LocalDateTime.of(LocalDate.now().
                plusDays(1), LocalTime.of(10, 0));
        LocalDateTime endTime = startTime.plusHours(2);

        Reservation reservation2 = new Reservation();
        reservation2.setStartTime(startTime.plusHours(2));
        reservation2.setEndTime(endTime.plusHours(2));

        Reservation reservation3 = new Reservation();
        reservation3.setStartTime(startTime.plusHours(4));
        reservation3.setEndTime(endTime.plusHours(4));

        List<Reservation> reservations = List.of(reservation2, reservation3);

        ReservationResponseDTO reservationResponseDTO2 = new ReservationResponseDTO();
        reservationResponseDTO2.setStartTime(startTime.plusHours(2).toString());
        reservationResponseDTO2.setEndTime(endTime.plusHours(2).toString());

        ReservationResponseDTO reservationResponseDTO3 = new ReservationResponseDTO();
        reservationResponseDTO3.setStartTime(startTime.plusHours(4).toString());
        reservationResponseDTO3.setEndTime(endTime.plusHours(4).toString());

        List<ReservationResponseDTO> reservationResponseDTOS = List.of(
                reservationResponseDTO2, reservationResponseDTO3);

        when(reservationDAO.findAllByCourtNumber(courtNumber)).thenReturn(reservations);
        when(reservationMapper.toReservationResponseDTO(reservation2)).thenReturn(reservationResponseDTO2);
        when(reservationMapper.toReservationResponseDTO(reservation3)).thenReturn(reservationResponseDTO3);

        // Act
        List<ReservationResponseDTO> actual = reservationService.getReservationByCourtNumber(courtNumber);

        // Assert
        assertIterableEquals(reservationResponseDTOS, actual, "Should be same object");

    }

    @Test
    @DisplayName("Get all Reservations by invalid court number")
    void getReservationByCourtNumber_inValidCourtNumber_ShouldReturnReservationNotFoundException() {

        // Arrange
        int courtNumber = 101;

        List<Reservation> reservations = List.of();

        when(reservationDAO.findAllByCourtNumber(courtNumber)).thenReturn(reservations);

        // Act and Assert
        assertThrows(ReservationNotFoundException.class, () ->
                        reservationService.getReservationByCourtNumber(courtNumber),
                "Should throw ReservationNotFoundException");

    }

    @Test
    @DisplayName("Get all Reservations by phone number")
    void getReservationByPhoneNumber_validPhoneNumber_ShouldReturnReservationNotFoundException() {

        // Arrange
        String phoneNumber = "123456789";
        boolean futureOnly = true;

        LocalDateTime startTime = LocalDateTime.of(LocalDate.now().
                plusDays(1), LocalTime.of(10, 0));
        LocalDateTime endTime = startTime.plusHours(2);


        Reservation reservation2 = new Reservation();
        reservation2.setStartTime(startTime.plusHours(2));
        reservation2.setEndTime(endTime.plusHours(2));

        Reservation reservation3 = new Reservation();
        reservation3.setStartTime(startTime.plusHours(4));
        reservation3.setEndTime(endTime.plusHours(4));

        List<Reservation> reservations = List.of(reservation2, reservation3);

        ReservationResponseDTO reservationResponseDTO2 = new ReservationResponseDTO();
        reservationResponseDTO2.setStartTime(startTime.plusHours(2).toString());
        reservationResponseDTO2.setEndTime(endTime.plusHours(2).toString());

        ReservationResponseDTO reservationResponseDTO3 = new ReservationResponseDTO();
        reservationResponseDTO3.setStartTime(startTime.plusHours(4).toString());
        reservationResponseDTO3.setEndTime(endTime.plusHours(4).toString());

        List<ReservationResponseDTO> reservationResponseDTOS = List.of(
                reservationResponseDTO2, reservationResponseDTO3);

        when(reservationDAO.findAllByPhoneNumber(phoneNumber,futureOnly)).thenReturn(reservations);
        when(reservationMapper.toReservationResponseDTO(reservation2)).thenReturn(reservationResponseDTO2);
        when(reservationMapper.toReservationResponseDTO(reservation3)).thenReturn(reservationResponseDTO3);

        // Act
        List<ReservationResponseDTO> actual = reservationService.getReservationByPhoneNumber(phoneNumber,futureOnly);

        // Assert
        assertIterableEquals(reservationResponseDTOS, actual, "Should be same object");
    }

    @Test
    @DisplayName("Get all Reservations by invalid phone number")
    void getReservationByPhoneNumber_inValidPhoneNumber_ShouldReturnReservationNotFoundException() {

        // Arrange
        String phoneNumber = "123456789";
        boolean futureOnly = true;

        List<Reservation> reservations = List.of();

        when(reservationDAO.findAllByPhoneNumber(phoneNumber,futureOnly)).thenReturn(reservations);


        // Assert
        ReservationNotFoundException e = assertThrows(ReservationNotFoundException.class, () ->
                        reservationService.getReservationByPhoneNumber(phoneNumber,futureOnly),
                "Should throw ReservationNotFoundException");

        assertEquals("No Reservations with phone number 123456789 in future", e.getMessage(),
                "Message should match" );
    }

    @Test
    @DisplayName("Get List of existing reservation")
    void getAllReservations_shouldReturnAllReservationsResponseDTO() {

        // Arrange
        LocalDateTime startTime = LocalDateTime.of(LocalDate.now().
                plusDays(1), LocalTime.of(10, 0));
        LocalDateTime endTime = startTime.plusHours(2);


        Reservation reservation2 = new Reservation();
        reservation2.setStartTime(startTime.plusHours(2));
        reservation2.setEndTime(endTime.plusHours(2));

        Reservation reservation3 = new Reservation();
        reservation3.setStartTime(startTime.plusHours(4));
        reservation3.setEndTime(endTime.plusHours(4));

        List<Reservation> reservations = List.of(reservation2, reservation3);

        ReservationResponseDTO reservationResponseDTO2 = new ReservationResponseDTO();
        reservationResponseDTO2.setStartTime(startTime.plusHours(2).toString());
        reservationResponseDTO2.setEndTime(endTime.plusHours(2).toString());

        ReservationResponseDTO reservationResponseDTO3 = new ReservationResponseDTO();
        reservationResponseDTO3.setStartTime(startTime.plusHours(4).toString());
        reservationResponseDTO3.setEndTime(endTime.plusHours(4).toString());

        List<ReservationResponseDTO> reservationResponseDTOS = List.of(
                reservationResponseDTO2, reservationResponseDTO3);

        when(reservationDAO.findAll()).thenReturn(reservations);
        when(reservationMapper.toReservationResponseDTO(reservation2)).thenReturn(reservationResponseDTO2);
        when(reservationMapper.toReservationResponseDTO(reservation3)).thenReturn(reservationResponseDTO3);

        // Act
        List<ReservationResponseDTO> actual = reservationService.getAllReservations();

        // Assert
        assertIterableEquals(reservationResponseDTOS, actual, "Should be same object");

    }

    @Test
    @DisplayName("Get all Reservations should return ReservationNotFoundException ")
    void getAllReservations_shouldReturnReservationNotFoundException() {

        // Arrange
        List<Reservation> reservations = List.of();

        when(reservationDAO.findAll()).thenReturn(reservations);

        // Act and Assert
        assertThrows(ReservationNotFoundException.class, () -> reservationService.getAllReservations(),
                "Should throw ReservationNotFoundException");

    }

    @Test
    @DisplayName("Update existing Reservation wit valid ReservationDTO")
    void updateReservation_withValidDTO_ShouldReturnUpdatedReservationResponseDTO() {

        // Arrange
        Long reservationId = 1L;
        int courtNumber = 101;

        LocalDateTime startTime = LocalDateTime.of(LocalDate.now().
                plusDays(1), LocalTime.of(10,0));
        LocalDateTime endTime = startTime.plusHours(2);

        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setStartTime(startTime);
        reservationDTO.setEndTime(endTime);
        reservationDTO.setCourtNumber(courtNumber);
        reservationDTO.setGameType(GameType.SINGLES);

        Reservation reservation2 = new Reservation();
        reservation2.setStartTime(startTime.plusHours(2));
        reservation2.setEndTime(endTime.plusHours(2));

        Reservation reservation3 = new Reservation();
        reservation3.setStartTime(startTime.plusHours(4));
        reservation3.setEndTime(endTime.plusHours(4));

        Reservation findReservation = new Reservation();
        findReservation.setStartTime(startTime);
        findReservation.setEndTime(endTime);
        findReservation.setPrice(BigDecimal.valueOf(120));


        Optional<Reservation> optionalReservation = Optional.of(findReservation);

        List<Reservation> reservations = List.of(reservation2, reservation3);

        SurfaceType surfaceType = new SurfaceType();
        surfaceType.setMinutePrice(BigDecimal.valueOf(1));

        Court court = new Court();
        court.setCourtNumber(courtNumber);
        court.setSurfaceType(surfaceType);

        Optional<Court> optionalCourt = Optional.of(court);

        User user = new User();
        Optional<User> optionalUser = Optional.empty();

        Reservation updateReservation = new Reservation();
        updateReservation.setStartTime(startTime);
        updateReservation.setEndTime(endTime);
        updateReservation.setPrice(BigDecimal.valueOf(120));
        updateReservation.setCourt(court);


        ReservationResponseDTO reservationResponseDTO = new ReservationResponseDTO();
        reservationResponseDTO.setPrice(BigDecimal.valueOf(120));

        when(reservationDAO.findAllByDateAndCourtNumber(startTime,courtNumber,reservationId)).thenReturn(reservations);

        when(reservationDAO.findById(reservationId)).thenReturn(optionalReservation);

        when(courtDAO.findByCourtNumber(courtNumber)).thenReturn(optionalCourt);

        when(userDAO.findByPhone(reservationDTO.getPhoneNumber())).thenReturn(optionalUser);

        when(reservationMapper.updateReservationFromDTO(reservationDTO, findReservation)).thenReturn(updateReservation);
        when(userMapper.mapFromReservationDTO(reservationDTO)).thenReturn(user);
        when(reservationDAO.update(updateReservation)).thenReturn(updateReservation);
        when(reservationMapper.toReservationResponseDTO(updateReservation)).thenReturn(reservationResponseDTO);

        // Act
        ReservationResponseDTO actual = reservationService.updateReservation(reservationDTO, reservationId );
        BigDecimal actualCalculatePrice = reservationResponseDTO.getPrice();

        // Assert
        assertEquals(reservationResponseDTO, actual, "Should return same object");
        assertEquals(reservationResponseDTO.getPrice(), actualCalculatePrice, "120 * 1 should be ");

        // Verify
        verify(userDAO, times(1)).save(user);

    }

    @Test
    @DisplayName("Update non exist Reservation")
    void updateReservation_withValidDateAndTimeAndNonExistReservationById_ShouldReturnReservationNotFoundException() {

        // Arrange
        Long reservationId = 1L;
        int courtNumber = 101;

        LocalDateTime startTime = LocalDateTime.of(LocalDate.now().
                plusDays(1), LocalTime.of(10,0));
        LocalDateTime endTime = startTime.plusHours(2);

        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setStartTime(startTime);
        reservationDTO.setEndTime(endTime);
        reservationDTO.setCourtNumber(courtNumber);
        reservationDTO.setGameType(GameType.SINGLES);

        Reservation reservation = new Reservation();

        Reservation reservation2 = new Reservation();
        reservation2.setStartTime(startTime.plusHours(2));
        reservation2.setEndTime(endTime.plusHours(2));

        Reservation reservation3 = new Reservation();
        reservation3.setStartTime(startTime.plusHours(4));
        reservation3.setEndTime(endTime.plusHours(4));

        Optional<Reservation> optionalReservation = Optional.empty();

        List<Reservation> reservations = List.of(reservation2, reservation3);

        SurfaceType surfaceType = new SurfaceType();
        surfaceType.setMinutePrice(BigDecimal.valueOf(1));

        User user = new User();

        ReservationResponseDTO reservationResponseDTO = new ReservationResponseDTO();
        reservationResponseDTO.setPrice(BigDecimal.valueOf(120));

        when(reservationDAO.findAllByDateAndCourtNumber(startTime,courtNumber,reservationId)).thenReturn(reservations);

        when(reservationDAO.findById(reservationId)).thenReturn(optionalReservation);


        // Act and Assert
        assertThrows(ReservationNotFoundException.class, () ->
                reservationService.updateReservation(reservationDTO, reservationId),
                "Should throw ReservationNotFoundException" );

        // Verify
        verify(userDAO, never()).save(user);
        verify(reservationDAO, never()).update(reservation);


    }

    @Test
    @DisplayName("Update Reservation with non valid time range")
    void updateReservation_withInValidDateAndTime_ShouldReturnReservationValidationException() {

        // Arrange
        Long reservationId = 1L;
        int courtNumber = 101;

        // Duration between start time and end time is more than 120 minutes
        LocalDateTime startTime = LocalDateTime.of(LocalDate.now().
                plusDays(1), LocalTime.of(10, 0));  // 10:00
        LocalDateTime endTime = startTime.plusHours(3);

        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setStartTime(startTime);   // 10:00
        reservationDTO.setEndTime(endTime);       // 13:00
        reservationDTO.setCourtNumber(courtNumber);
        reservationDTO.setGameType(GameType.SINGLES);

        Reservation reservation = new Reservation();

        Reservation reservation2 = new Reservation();
        reservation2.setStartTime(startTime.plusHours(2));
        reservation2.setEndTime(endTime.plusHours(2));

        Reservation reservation3 = new Reservation();
        reservation3.setStartTime(startTime.plusHours(4));
        reservation3.setEndTime(endTime.plusHours(4));

        List<Reservation> reservations = List.of(reservation2, reservation3);

        SurfaceType surfaceType = new SurfaceType();
        surfaceType.setMinutePrice(BigDecimal.valueOf(1));

        User user = new User();

        ReservationResponseDTO reservationResponseDTO = new ReservationResponseDTO();
        reservationResponseDTO.setPrice(BigDecimal.valueOf(120));

        when(reservationDAO.findAllByDateAndCourtNumber(startTime, courtNumber, reservationId)).thenReturn(reservations);

        // Act and Assert
        assertThrows(ReservationValidationException.class, () ->
                        reservationService.updateReservation(reservationDTO, reservationId),
                "Should throw ReservationValidationException");

        // Verify
        verify(userDAO, never()).save(user);
        verify(reservationDAO, never()).update(reservation);
    }

    @Test
    @DisplayName("Update Reservation with Date in past")
    void updateReservation_withDateInPast_ShouldReturnReservationValidationException() {

        // Arrange
        Long reservationId = 1L;
        int courtNumber = 101;

        // Date in past
        LocalDateTime startTime = LocalDateTime.of(LocalDate.now().
                minusDays(1), LocalTime.of(10, 0));  // 10:00
        LocalDateTime endTime = startTime.plusHours(2);

        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setStartTime(startTime);   // 10:00
        reservationDTO.setEndTime(endTime);       // 12:00
        reservationDTO.setCourtNumber(courtNumber);
        reservationDTO.setGameType(GameType.SINGLES);

        Reservation reservation = new Reservation();

        Reservation reservation2 = new Reservation();
        reservation2.setStartTime(startTime.plusHours(2));
        reservation2.setEndTime(endTime.plusHours(2));

        Reservation reservation3 = new Reservation();
        reservation3.setStartTime(startTime.plusHours(4));
        reservation3.setEndTime(endTime.plusHours(4));

        List<Reservation> reservations = List.of(reservation2, reservation3);

        SurfaceType surfaceType = new SurfaceType();
        surfaceType.setMinutePrice(BigDecimal.valueOf(1));

        User user = new User();

        ReservationResponseDTO reservationResponseDTO = new ReservationResponseDTO();
        reservationResponseDTO.setPrice(BigDecimal.valueOf(120));

        when(reservationDAO.findAllByDateAndCourtNumber(startTime, courtNumber, reservationId)).thenReturn(reservations);

        // Act and Assert
        assertThrows(ReservationValidationException.class, () ->
                        reservationService.updateReservation(reservationDTO, reservationId),
                "Should throw ReservationValidationException");

        // Verify
        verify(userDAO, never()).save(user);
        verify(reservationDAO, never()).update(reservation);
    }

    @Test
    @DisplayName("Update Reservation with overLapping Reservation")
    void updateReservation_withOverLappingReservation_ShouldReturnReservationValidationException() {

        // Arrange
        Long reservationId = 1L;
        int courtNumber = 101;

        LocalDateTime startTime = LocalDateTime.of(LocalDate.now().
                plusDays(1), LocalTime.of(10, 0));  // 10:00
        LocalDateTime endTime = startTime.plusHours(2);

        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setStartTime(startTime);   // 10:00
        reservationDTO.setEndTime(endTime);       // 12:00
        reservationDTO.setCourtNumber(courtNumber);
        reservationDTO.setGameType(GameType.SINGLES);

        Reservation reservation = new Reservation();

        // Over lapping reservation
        Reservation reservation2 = new Reservation();
        reservation2.setStartTime(startTime.plusMinutes(30));  // 10:30
        reservation2.setEndTime(endTime);                      // 12:00

        Reservation reservation3 = new Reservation();
        reservation3.setStartTime(startTime.plusHours(4));
        reservation3.setEndTime(endTime.plusHours(4));

        List<Reservation> reservations = List.of(reservation2, reservation3);

        SurfaceType surfaceType = new SurfaceType();
        surfaceType.setMinutePrice(BigDecimal.valueOf(1));

        User user = new User();

        ReservationResponseDTO reservationResponseDTO = new ReservationResponseDTO();
        reservationResponseDTO.setPrice(BigDecimal.valueOf(120));

        when(reservationDAO.findAllByDateAndCourtNumber(startTime, courtNumber, reservationId)).thenReturn(reservations);

        // Act and Assert
        assertThrows(ReservationValidationException.class, () ->
                        reservationService.updateReservation(reservationDTO, reservationId),
                "Should throw ReservationValidationException");

        // Verify
        verify(userDAO, never()).save(user);
        verify(reservationDAO, never()).update(reservation);
    }

    @Test
    @DisplayName("Deleted Reservation by valid Id")
    void deleteReservation_validReservationId_shouldSetDeletedToTrue() {
        // Arrange
        Long reservationId = 1L;

        Reservation reservation = new Reservation();
        reservation.setId(reservationId);
        reservation.setDeleted(false);

        Optional<Reservation> optionalReservation = Optional.of(reservation);

        when(reservationDAO.findById(reservationId)).thenReturn(optionalReservation);
        when(reservationDAO.update(reservation)).thenReturn(reservation);

        // Act
        reservationService.deleteReservation(reservationId);

        // Assert
        assertTrue(reservation.isDeleted(), "Field deleted should be set to true");

        // Verify
        verify(reservationDAO, times(1)).update(reservation);

    }

    @Test
    @DisplayName("Deleted Reservation by invalid Id")
    void deleteReservation_inValidReservationId_shouldThrowReservationNotFoundException() {
        // Arrange
        Long reservationId = 1L;

        Optional<Reservation> optionalReservation = Optional.empty();

        when(reservationDAO.findById(reservationId)).thenReturn(optionalReservation);


        // Act and Assert
       assertThrows(ReservationNotFoundException.class, () -> reservationService.deleteReservation(reservationId),
               "Should throw ReservationNotFoundException");

        // Verify
        verify(reservationDAO, never()).update(any(Reservation.class));

    }
}