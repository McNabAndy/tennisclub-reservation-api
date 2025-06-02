package cz.vojtechsika.tennisclub.service;

import cz.vojtechsika.tennisclub.dao.CourtDAO;
import cz.vojtechsika.tennisclub.dao.ReservationDAO;
import cz.vojtechsika.tennisclub.dao.UserDAO;
import cz.vojtechsika.tennisclub.dto.ReservationDTO;
import cz.vojtechsika.tennisclub.dto.response.ReservationResponseDTO;
import cz.vojtechsika.tennisclub.dto.mapper.ReservationMapper;
import cz.vojtechsika.tennisclub.dto.mapper.UserMapper;
import cz.vojtechsika.tennisclub.entity.Court;
import cz.vojtechsika.tennisclub.entity.Reservation;
import cz.vojtechsika.tennisclub.entity.User;
import cz.vojtechsika.tennisclub.enums.GameType;
import cz.vojtechsika.tennisclub.exception.CourtNotFoundException;
import cz.vojtechsika.tennisclub.exception.ReservationNotFoundException;
import cz.vojtechsika.tennisclub.exception.ReservationValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


/**
 * ReservationServiceImpl is the service implementation for managing {@link Reservation} entities.
 * It handles business logic for creating, retrieving, updating, and deleting reservations,
 * delegating persistence operations to {@link ReservationDAO}, {@link UserDAO}, and {@link CourtDAO}.
 * Mapping between DTOs and entities is performed by {@link ReservationMapper} and {@link UserMapper}.
 * <p>
 * This implementation enforces validation rules (time conflicts, duration limits, and allowed hours),
 * calculates pricing based on court surface and game type, and performs soft deletion of reservations.
 * </p>
 */
@Service
public class ReservationServiceImpl implements ReservationService {

    /**
     * DAO for reservation persistence operations.
     */
    private ReservationDAO reservationDAO;

    /**
     * DAO for user persistence operations.
     */
    private UserDAO userDAO;

    /**
     * DAO for court persistence operations.
     */
    private CourtDAO courtDAO;

    /**
     * Mapper to convert between {@link ReservationDTO} and {@link Reservation}.
     */
    private ReservationMapper reservationMapper;

    /**
     * Mapper to convert from {@link ReservationDTO} to {@link User}
     */
    private UserMapper userMapper;


    /**
     * Constructs a new ReservationServiceImpl with required dependencies.
     *
     * @param theReservationDAO   DAO for reservation persistence operations.
     * @param theUserDAO          DAO for user persistence operations.
     * @param theCourtDAO         DAO for court persistence operations.
     * @param theReservationMapper Mapper to convert between {@link ReservationDTO} and {@link Reservation}.
     * @param theUserMapper       Mapper to convert from {@link ReservationDTO} to {@link User}.
     */
    @Autowired
    public ReservationServiceImpl(ReservationDAO theReservationDAO,
                                  UserDAO theUserDAO,
                                  CourtDAO theCourtDAO,
                                  ReservationMapper theReservationMapper,
                                  UserMapper theUserMapper) {
        reservationDAO = theReservationDAO;
        userDAO = theUserDAO;
        courtDAO = theCourtDAO;
        reservationMapper = theReservationMapper;
        userMapper = theUserMapper;

    }


    /**
     * Creates a new reservation based on the provided {@link ReservationDTO}.
     * <p>
     * Validates that the time range is in the future, does not exceed two hours,
     * falls within allowed hours (10:00–22:00), and does not overlap existing reservations.
     * Finds or creates the associated {@link User}, calculates the price using {@link GameType}
     * and the court’s minute price, and persists the new reservation. Returns a
     * {@link ReservationResponseDTO} for the created reservation.
     * </p>
     *
     * @param reservationDTO The {@link ReservationDTO} containing user name, phone number,
     *                       start/end times, court number, and game type.
     * @return A {@link ReservationResponseDTO} representing the newly created reservation.
     * @throws ReservationValidationException if the provided time range is invalid or conflicts occur.
     * @throws CourtNotFoundException          if no court with the given court number exists.
     */
    @Transactional
    @Override
    public ReservationResponseDTO createReservation(ReservationDTO reservationDTO) {
        LocalDateTime startTime = reservationDTO.getStartTime();
        LocalDateTime endTime = reservationDTO.getEndTime();
        int courtNumber = reservationDTO.getCourtNumber();

        List<Reservation> reservations = findAllReservationsByDateAndCourtNumber(startTime,courtNumber, null);

        if (isValidReservation(startTime, endTime, reservations)) {

            Court court = findCourtByCourtNumber(reservationDTO.getCourtNumber());

            Reservation reservation = reservationMapper.toReservationEntity(reservationDTO);
            reservation.setUser(getOrCreateUser(reservationDTO));
            reservation.setPrice(courtPriceCalculator(court,reservationDTO));
            reservation.setCourt(court);

            Reservation newReservation = reservationDAO.create(reservation);
            return reservationMapper.toReservationResponseDTO(newReservation);
        } else {
            throw new ReservationValidationException("Provided time range is invalid");
        }
    }


    /**
     * Updates an existing reservation identified by {@code id} using data from the provided {@link ReservationDTO}.
     * <p>
     * Validates that the updated time range is in the future, does not exceed two hours,
     * falls within allowed hours (10:00–22:00), and does not overlap other reservations (excluding itself).
     * Finds the existing reservation, applies updates, recalculates price, and persists changes. Returns a
     * {@link ReservationResponseDTO} for the updated reservation.
     * </p>
     *
     * @param reservationDTO The {@link ReservationDTO} containing updated user name, phone number,
     *                       start/end times, court number, and game type.
     * @param id             The unique ID of the reservation to update.
     * @return A {@link ReservationResponseDTO} representing the updated reservation.
     * @throws ReservationNotFoundException    if no reservation with the given ID exists.
     * @throws ReservationValidationException if the provided time range is invalid or conflicts occur.
     * @throws CourtNotFoundException          if no court with the given court number exists.
     */
    // MUsím zde fakt upozornit na to null, když je přítomno používá to metoda pro create když ne používa ho update
    @Transactional
    @Override
    public ReservationResponseDTO updateReservation(ReservationDTO reservationDTO, Long id) {
        LocalDateTime startTime = reservationDTO.getStartTime();
        LocalDateTime endTime = reservationDTO.getEndTime();
        int courtNumber = reservationDTO.getCourtNumber();

        List<Reservation> reservations = findAllReservationsByDateAndCourtNumber(startTime, courtNumber, id);
        if (isValidReservation(startTime, endTime, reservations)) {

            Optional <Reservation>  optionalReservation = reservationDAO.findById(id);
            if (optionalReservation.isEmpty()) {
                throw new ReservationNotFoundException("Reservation with id " + id + " not found");
            }
            Court court = findCourtByCourtNumber(courtNumber);
            Reservation findReservation = optionalReservation.get();

            Reservation reservation = reservationMapper.updateReservationFromDTO(reservationDTO, findReservation);
            reservation.setUser(getOrCreateUser(reservationDTO));
            reservation.setPrice(courtPriceCalculator(court,reservationDTO));
            reservation.setCourt(court);

            Reservation updatedReservation = reservationDAO.update(reservation);
            return reservationMapper.toReservationResponseDTO(updatedReservation);
        } else {
            throw new ReservationValidationException("Provided time range is invalid");
        }
    }


    /**
     * Retrieves a reservation by its ID.
     *
     * @param id The unique ID of the reservation to retrieve.
     * @return A {@link ReservationResponseDTO} representing the requested reservation.
     * @throws ReservationNotFoundException if no reservation with the given ID exists.
     */
    @Override
    public ReservationResponseDTO getReservationById(Long id) {

        Optional<Reservation> optionalReservation = reservationDAO.findById(id);
        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();

            return reservationMapper.toReservationResponseDTO(reservation);
        } else {
            throw new ReservationNotFoundException("Reservation with id " + id + " not found");
        }
    }


    /**
     * Retrieves all reservations for a specific court number.
     * <p>
     * Returns a list of {@link ReservationResponseDTO} instances ordered by creation date descending.
     * </p>
     *
     * @param courtNumber The court number to filter reservations by.
     * @return A list of {@link ReservationResponseDTO} for the specified court.
     * @throws ReservationNotFoundException if no reservations exist for the given court number.
     */
    @Override
    public List<ReservationResponseDTO> getReservationByCourtNumber(int courtNumber) {

        List<Reservation> reservations = reservationDAO.findAllByCourtNumber(courtNumber);
        if (reservations.isEmpty()) {
            throw new ReservationNotFoundException("Reservation with court number " + courtNumber + " not found");
        }
        return reservations.stream()
                .map( reservation ->
                        reservationMapper.toReservationResponseDTO(reservation))
                .toList();
    }


    /**
     * Retrieves all reservations made by a specific phone number.
     * <p>
     * If {@code futureOnly} is true, only reservations with start times ≥ now are returned.
     * Results are ordered by start time ascending.
     * </p>
     *
     * @param phoneNumber The phone number to filter reservations by.
     * @param futureOnly  Whether to return only future reservations (true) or all reservations (false).
     * @return A list of {@link ReservationResponseDTO} matching the phone number criteria.
     * @throws ReservationNotFoundException if no reservations exist for the given phone number.
     */
    @Override
    public List<ReservationResponseDTO> getReservationByPhoneNumber(String phoneNumber, boolean futureOnly) {

        List<Reservation> reservations = reservationDAO.findAllByPhoneNumber(phoneNumber,futureOnly);
        if (reservations.isEmpty()) {
            String message = "No Reservations with phone number " + phoneNumber;
            if (futureOnly) {
               message += " in future";
            }
            throw new ReservationNotFoundException(message);
        }

        return reservations.stream()
                .map(reservation ->
                        reservationMapper.toReservationResponseDTO(reservation))
                .toList();
    }


    /**
     * Retrieves all reservations in the system.
     * <p>
     * Returns a list of {@link ReservationResponseDTO} instances ordered by start time ascending.
     * </p>
     *
     * @return A list of all {@link ReservationResponseDTO}.
     * @throws ReservationNotFoundException if no reservations exist.
     */
    @Override
    public List<ReservationResponseDTO> getAllReservations() {

        List<Reservation> reservations = reservationDAO.findAll();
        if (reservations.isEmpty()) {
            throw new ReservationNotFoundException("No Reservations found");
        }
        return reservations.stream()
                .map(reservation ->
                        reservationMapper.toReservationResponseDTO(reservation))
                .toList();
    }


    /**
     * Deletes a reservation by its ID.
     * <p>
     * Performs a soft deletion by marking the reservation's {@code deleted} flag as true.
     * If the reservation does not exist, a {@link ReservationNotFoundException} is thrown.
     * </p>
     *
     * @param id The unique ID of the reservation to delete.
     * @throws ReservationNotFoundException if no reservation with the given ID exists.
     */
    @Transactional
    @Override
    public void deleteReservation(Long id) {
        Optional<Reservation> optionalReservation = reservationDAO.findById(id);
        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();
            reservation.setDeleted(true);
            reservationDAO.update(reservation);
        } else {
            throw new ReservationNotFoundException("Delete failed: Reservation with id " + id + " not found");
        }
    }


    // The following private helper methods encapsulate validation and lookup logic:


    // Validates that a reservation is in the future, within two hours, during allowed hours, and non-overlapping
    private boolean isValidReservation(LocalDateTime startTime, LocalDateTime endTime, List<Reservation> existsReservations) {

        boolean isInFuture = isInFuture(startTime);
        boolean isTwoHoursLimit = isTwoHoursLimit(startTime, endTime);
        boolean isValidReservationInterval = isValidReservationInterval(startTime, endTime);
        boolean hasOverlappingReservation = hasOverlappingReservation(startTime, endTime, existsReservations);

        return isInFuture && isTwoHoursLimit && isValidReservationInterval && !hasOverlappingReservation;
    }

    // Checks if the given startTime is after the current time
    private boolean isInFuture(LocalDateTime startTime) {
        return startTime.isAfter(LocalDateTime.now());
    }

    // Calculates the duration between startTime and endTime in minutes
    private Long getDurationInMinutes(LocalDateTime startTime, LocalDateTime endTime) {
        return Duration.between(startTime, endTime).toMinutes();
    }

    // Ensures the reservation duration is greater than 0 minutes and at most 120 minutes
    private boolean isTwoHoursLimit(LocalDateTime startTime, LocalDateTime endTime) {
        long minutes = getDurationInMinutes(startTime, endTime);
        return minutes > 0 && minutes <= 120; // tady to dát asi do properti souboru abych to na jednom místě mohl změnit okno
    }

    // Verifies that startTime and endTime fall between 10:00 and 22:00
    private boolean isValidReservationInterval(LocalDateTime startTime, LocalDateTime endTime) {
        LocalTime allowedStartTime = LocalTime.of(10,0); // tady to dát asi do properti souboru abych to na jednom místě mohl změnit okno
        LocalTime allowedEndTime = LocalTime.of(22,0);   // tady to dát asi do properti souboru abych to na jednom místě mohl změnit okno

        LocalTime start = startTime.toLocalTime();
        LocalTime end = endTime.toLocalTime();

        return !start.isBefore(allowedStartTime) && !end.isAfter(allowedEndTime);
    }

    // Checks if any existing reservation overlaps with the given time range
    private boolean hasOverlappingReservation(LocalDateTime startTime, LocalDateTime endTime, List<Reservation> reservations) {
        return reservations.stream().
                anyMatch(reservation ->
                        reservation.getStartTime().isBefore(endTime) &&
                                reservation.getEndTime().isAfter(startTime)
                );
    }

    // Calculates the price based on court’s minute rate and doubles multiplier if applicable
    private BigDecimal courtPriceCalculator (Court court, ReservationDTO reservationDTO){
        Long minutes = getDurationInMinutes(reservationDTO.getStartTime(), reservationDTO.getEndTime());
        BigDecimal minutePrice = court.getSurfaceType().getMinutePrice();
        BigDecimal finalPrice = minutePrice.multiply(BigDecimal.valueOf(minutes));
        if (reservationDTO.getGameType().equals(GameType.DOUBLES)){
            return finalPrice.multiply(BigDecimal.valueOf(2));
        }
        return finalPrice;
    }

    // Retrieves an existing user by phone or creates and saves a new one
    private User getOrCreateUser(ReservationDTO reservationDTO) {
        Optional<User> optionalUser = userDAO.findByPhone(reservationDTO.getPhoneNumber());
        if (optionalUser.isPresent()){
            User findUser = optionalUser.get();

            if(!findUser.getUserName().equals(reservationDTO.getUserName())){
                findUser.setUserName(reservationDTO.getUserName());
                return userDAO.save(findUser);
            }
            return findUser;

        } else {
            User newUser = userMapper.mapFromReservationDTO(reservationDTO);
            return userDAO.save(newUser);
        }
    }

    // Finds a court by its number or throws if not found
    private Court findCourtByCourtNumber(int courtNumber) {
        Optional<Court> optionalCourt = courtDAO.findByCourtNumber(courtNumber);
        if (optionalCourt.isPresent()){
            return optionalCourt.get();
        } else {
            throw new CourtNotFoundException("Court number " + courtNumber + " not found in database");
        }
    }

    /**
     * Retrieves all reservations for a specific date and court number, optionally excluding a reservation by its ID.
     * <p>
     * The {@code excludeId} parameter may be {@code null}. When {@code excludeId} is {@code null}, no reservation is excluded—
     * this is used during creation to check all existing reservations. When updating, the current reservation's ID is passed
     * as {@code excludeId}, so that it is not considered in the overlap check.
     * </p>
     *
     * @param date        The date (date portion of {@link LocalDateTime}) for which to retrieve reservations.
     * @param courtNumber The court number to filter reservations by.
     * @param excludeId   The ID of the reservation to exclude from results; may be {@code null} when creating.
     * @return A list of {@link Reservation} entities matching the date and court number, excluding the one with {@code excludeId} if provided.
     */
    private List<Reservation> findAllReservationsByDateAndCourtNumber(LocalDateTime date, int courtNumber, Long excludeId) {
        return reservationDAO.findAllByDateAndCourtNumber(date,courtNumber,excludeId);
    }

}

