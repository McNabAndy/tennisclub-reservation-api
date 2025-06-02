package cz.vojtechsika.tennisclub.service;


import cz.vojtechsika.tennisclub.dto.ReservationDTO;
import cz.vojtechsika.tennisclub.dto.response.ReservationResponseDTO;

import java.util.List;
/**
 * ReservationService defines the business operations for managing {@link cz.vojtechsika.tennisclub.entity.Reservation} entities.
 * It provides methods to create, retrieve, update, and delete reservations, returning data transfer objects for API responses.
 * <p>
 * Implementations of this interface handle validation, mapping, and coordination between controllers and data access layers (DAOs/repositories).</p>
 *
 * <p>All retrieval methods return {@link ReservationResponseDTO} instances, which contain formatted date/time and user/court information.</p>
 */
public interface ReservationService {

    /**
     * Creates a new reservation based on the provided {@link ReservationDTO}.
     * <p>
     * Validates reservation data (e.g., start/end times, overlapping reservations), creates or finds the associated user and court,
     * calculates the price, and persists the new reservation. Returns a {@link ReservationResponseDTO} for the created reservation.
     * </p>
     *
     * @param reservationDTO The {@link ReservationDTO} containing user name, phone number, start/end times, court number, and game type.
     * @return A {@link ReservationResponseDTO} representing the newly created reservation.
     * @throws cz.vojtechsika.tennisclub.exception.ReservationValidationException if validation fails (e.g., time conflicts, invalid duration).
     * @throws cz.vojtechsika.tennisclub.exception.CourtNotFoundException          if no court with the given court number exists.
     * @throws cz.vojtechsika.tennisclub.exception.UserNotFoundException           if a lookup of the user by phone number fails (and creation logic is not configured to create new users).
     */
    ReservationResponseDTO createReservation(ReservationDTO reservationDTO);


    /**
     * Retrieves a reservation by its ID.
     *
     * @param id The unique ID of the reservation to retrieve.
     * @return A {@link ReservationResponseDTO} representing the requested reservation.
     * @throws cz.vojtechsika.tennisclub.exception.ReservationNotFoundException if no reservation with the given ID exists.
     */
    ReservationResponseDTO getReservationById(Long id);


    /**
     * Retrieves all reservations for a specific court number.
     * <p>
     * Returns a list of {@link ReservationResponseDTO} instances ordered by creation date descending.
     * </p>
     *
     * @param courtNumber The court number to filter reservations by.
     * @return A list of {@link ReservationResponseDTO} for the specified court.
     * @throws cz.vojtechsika.tennisclub.exception.ReservationNotFoundException if no reservations exist for the given court number.
     */
    List<ReservationResponseDTO> getReservationByCourtNumber(int courtNumber);


    /**
     * Retrieves all reservations made by a specific phone number.
     * <p>
     * If {@code futureOnly} is true, only reservations with start times greater than or equal to now are returned.
     * The results are ordered by start time ascending.
     * </p>
     *
     * @param phoneNumber The phone number to filter reservations by.
     * @param futureOnly  Whether to return only future reservations (true) or all reservations (false).
     * @return A list of {@link ReservationResponseDTO} matching the phone number criteria.
     * @throws cz.vojtechsika.tennisclub.exception.ReservationNotFoundException if no reservations exist for the given phone number.
     */
    List<ReservationResponseDTO> getReservationByPhoneNumber(String phoneNumber, boolean futureOnly);


    /**
     * Retrieves all reservations in the system.
     * <p>
     * Returns a list of {@link ReservationResponseDTO} instances ordered by start time ascending.
     * </p>
     *
     * @return A list of all {@link ReservationResponseDTO}.
     * @throws cz.vojtechsika.tennisclub.exception.ReservationNotFoundException if no reservations exist.
     */
    List<ReservationResponseDTO> getAllReservations();


    /**
     * Updates an existing reservation identified by {@code id} using data from the provided {@link ReservationDTO}.
     * <p>
     * Validates new reservation data (e.g., time conflicts, valid duration), updates mutable fields (start/end times and game type),
     * recalculates the price if necessary, and persists the changes. Returns a {@link ReservationResponseDTO} for the updated reservation.
     * </p>
     *
     * @param reservationDTO The {@link ReservationDTO} containing updated reservation data.
     * @param id             The unique ID of the reservation to update.
     * @return A {@link ReservationResponseDTO} representing the updated reservation.
     * @throws cz.vojtechsika.tennisclub.exception.ReservationNotFoundException    if no reservation with the given ID exists.
     * @throws cz.vojtechsika.tennisclub.exception.ReservationValidationException  if validation fails for the updated data (e.g., time conflicts).
     */
    ReservationResponseDTO updateReservation(ReservationDTO reservationDTO, Long id);


    /**
     * Deletes a reservation by its ID.
     * <p>
     * Performs a soft deletion by marking the reservation's {@code deleted} flag as true. If the reservation does not exist,
     * a {@link cz.vojtechsika.tennisclub.exception.ReservationNotFoundException} is thrown.
     * </p>
     *
     * @param id The unique ID of the reservation to delete.
     * @throws cz.vojtechsika.tennisclub.exception.ReservationNotFoundException if no reservation with the given ID exists.
     */
    void deleteReservation(Long id);
}

