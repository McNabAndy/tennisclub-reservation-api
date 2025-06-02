package cz.vojtechsika.tennisclub.dao;

import cz.vojtechsika.tennisclub.entity.Reservation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * ReservationDAO is an interface that defines the Data Access Object (DAO) for {@link Reservation} entities.
 * It provides methods for interacting with the database and performing CRUD operations for reservations.
 * This interface is typically implemented by a class that interacts with the underlying database (e.g., using JPA or JDBC).
 */
public interface ReservationDAO {


    /**
     * Retrieves a reservation by its ID.
     *
     * @param id The ID of the reservation to be retrieved.
     * @return An {@link Optional} containing the {@link Reservation} if found, or empty if not found.
     */
    Optional<Reservation> findById(Long id);


    /**
     * Retrieves all reservations for a specific court number.
     *
     * @param courtNumber The court number to search for.
     * @return A list of {@link Reservation} entities associated with the specified court number.
     */
    List<Reservation> findAllByCourtNumber(int courtNumber);


    /**
     * Retrieves all reservations for a specific phone number.
     * Optionally, it can filter reservations to include only future reservations based on the {@code futureOnly} flag.
     *
     * @param phoneNumber The phone number to search for.
     * @param futureOnly Whether to include only future reservations (true) or all reservations (false).
     * @return A list of {@link Reservation} entities for the specified phone number.
     */
    List<Reservation> findAllByPhoneNumber(String phoneNumber, boolean futureOnly);


    /**
     * Retrieves all reservations in the system.
     *
     * @return A list of all {@link Reservation} entities.
     */
    List<Reservation> findAll();


    /**
     * Creates a new reservation in the database.
     *
     * @param reservation The {@link Reservation} entity to be created.
     * @return The created {@link Reservation} entity, including any generated values (e.g., ID).
     */
    Reservation create(Reservation reservation);


    /**
     * Retrieves all reservations for a specific date and court number, excluding a reservation by its ID.
     *
     * @param date The date of the reservation.
     * @param courtNumber The court number for which reservations are to be retrieved.
     * @param excludeId The ID of the reservation to exclude from the results.
     * @return A list of {@link Reservation} entities for the specified date and court number, excluding the given reservation ID.
     */
    List<Reservation> findAllByDateAndCourtNumber(LocalDateTime date, int courtNumber, Long excludeId);


    /**
     * Updates an existing reservation in the database.
     *
     * @param reservation The {@link Reservation} entity with updated values.
     * @return The updated {@link Reservation} entity.
     */
    Reservation update(Reservation reservation);;
}
