package cz.vojtechsika.tennisclub.dao;


import cz.vojtechsika.tennisclub.entity.Reservation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


/**
 * ReservationDAOImpl is an implementation of the {@link ReservationDAO} interface that provides
 * database access for managing {@link Reservation} entities. This class uses JPA (Jakarta Persistence API)
 * and an {@link EntityManager} to interact with the underlying database. It includes methods for creating,
 * updating, and retrieving reservations, as well as specialized queries to filter by date, court number,
 * and phone number.
 */
@Repository
public class ReservationDAOImpl implements ReservationDAO {

    /**
     * The entityManager used to interact with the database
     */
    private final EntityManager entityManager;


    /**
     * Constructs a new ReservationDAOImpl with the provided {@link EntityManager}.
     *
     * @param theEntityManager The {@link EntityManager} used to interact with the database.
     */
    @Autowired
    public ReservationDAOImpl(EntityManager theEntityManager) {
        entityManager = theEntityManager;
    }


    /**
     * Persists a new {@link Reservation} entity in the database.
     *
     * @param reservation The {@link Reservation} entity to be created.
     * @return The persisted {@link Reservation} entity, including any generated values (e.g., ID).
     */
    @Override
    public Reservation create(Reservation reservation) {
        entityManager.persist(reservation);
        return reservation;
    }


    /**
     * Retrieves a list of {@link Reservation} entities for a specific date and court number,
     * excluding a reservation by its ID if provided. This method filters reservations whose
     * {@code startTime} falls within the specified date (from start of day to end of day),
     * belongs to a given court number, and are not marked as deleted. Optionally excludes a
     * reservation with the given {@code excludeId} to avoid conflicts when updating.
     *
     * @param date        The {@link LocalDateTime} representing the date for which reservations are to be retrieved.
     *                    Only the date portion is used (from start of day to next-day start).
     * @param courtNumber The court number to filter reservations by.
     * @param excludeId   The ID of the reservation to exclude from the results; may be {@code null}.
     * @return A list of {@link Reservation} entities matching the date and court number criteria,
     *         excluding the reservation with {@code excludeId} if it was provided.
     */
    @Override
    public List<Reservation> findAllByDateAndCourtNumber(LocalDateTime date, int courtNumber, Long excludeId) {

        LocalDateTime from = date.toLocalDate().atStartOfDay();
        LocalDateTime to = from.plusDays(1);

        String baseQuery = "SELECT r FROM Reservation r WHERE r.startTime >= :from AND r.startTime < :to " +
                "AND r.deleted = :isFalse AND r.court.courtNumber = :courtNumber";

        if (excludeId != null) {
            baseQuery += " AND r.id <> :excludeId";
        }
        TypedQuery<Reservation> query = entityManager.createQuery(baseQuery, Reservation.class)
                .setParameter("from", from)
                .setParameter("to", to)
                .setParameter("isFalse", false)
                .setParameter("courtNumber", courtNumber);
        if (excludeId != null) {
            query.setParameter("excludeId", excludeId);
        }

        return query.getResultList();
    }


    /**
     * Retrieves a {@link Reservation} by its ID, only if the reservation and its associated court and user
     * are not marked as deleted.
     *
     * @param id The ID of the reservation to be retrieved.
     * @return An {@link Optional} containing the {@link Reservation} entity if found and not deleted;
     *         otherwise, {@link Optional#empty()}.
     */
    @Override
    public Optional<Reservation> findById(Long id) {

        TypedQuery<Reservation> query = entityManager.createQuery("SELECT r FROM Reservation r WHERE " +
                        "r.id = :id AND r.deleted = :isFalse AND r.court.deleted = :isFalse AND" +
                        " r.user.deleted = :isFalse", Reservation.class).
                setParameter("id", id).
                setParameter("isFalse", false);

        List<Reservation> result = query.getResultList();
        if (result.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(result.get(0));
        }

    }


    /**
     * Retrieves all {@link Reservation} entities for a specific court number, ordered by creation time descending.
     * Only reservations not marked as deleted are returned.
     *
     * @param courtNumber The court number to filter reservations by.
     * @return A list of {@link Reservation} entities for the given court number, ordered from newest to oldest.
     */
    @Override
    public List<Reservation> findAllByCourtNumber(int courtNumber) {

        TypedQuery<Reservation> query = entityManager.createQuery(
                     "SELECT r FROM Reservation r WHERE r.court.courtNumber = :courtNumber AND r.deleted = :isFalse " +
                        "ORDER BY r.createdAt DESC ", Reservation.class).
                setParameter("courtNumber", courtNumber).
                setParameter("isFalse", false);
        return query.getResultList();
    }


    /**
     * Retrieves all {@link Reservation} entities for a specific phone number. If {@code futureOnly} is true,
     * only reservations with {@code startTime} greater than or equal to the current date-time are returned.
     * Results are ordered by {@code startTime} ascending. Only reservations not marked as deleted are returned.
     *
     * @param phoneNumber The phone number to filter reservations by.
     * @param futureOnly  Whether to include only reservations occurring in the future; if {@code true},
     *                    only reservations with {@code startTime} >= now are returned.
     * @return A list of {@link Reservation} entities matching the phone number, optionally filtered to future-only,
     *         ordered by {@code startTime} ascending.
     */
    @Override
    public List<Reservation> findAllByPhoneNumber(String phoneNumber, boolean futureOnly) {
        String baseQuery = "SELECT r FROM Reservation r WHERE r.user.phoneNumber = :phoneNumber";

        if (futureOnly) {
            baseQuery = baseQuery + " AND r.startTime >= :from";
        }
        baseQuery += " AND r.deleted = :isFalse ORDER BY r.startTime ASC";

        TypedQuery<Reservation> query = entityManager.createQuery(baseQuery, Reservation.class)
                .setParameter("phoneNumber", phoneNumber)
                .setParameter("isFalse", false);
        if (futureOnly) {
            query.setParameter("from", LocalDateTime.now());
        }
        return query.getResultList();
    }


    /**
     * Retrieves all {@link Reservation} entities in the system, ordered by {@code startTime} ascending.
     * Only reservations not marked as deleted are returned.
     *
     * @return A list of all non-deleted {@link Reservation} entities, ordered by {@code startTime}.
     */
    @Override
    public List<Reservation> findAll() {

        TypedQuery<Reservation> query = entityManager.createQuery("SELECT r FROM Reservation r WHERE" +
                " r.deleted = :isFalse ORDER BY r.startTime ASC",Reservation.class)
                .setParameter("isFalse", false);

        return query.getResultList();
    }


    /**
     * Updates an existing {@link Reservation} entity in the database. This method merges the provided
     * {@link Reservation} with the existing one in the persistence context, applying any changed fields
     * while preserving other properties.
     *
     * @param reservation The {@link Reservation} entity with updated values to be merged.
     * @return The merged {@link Reservation} entity reflecting the updates.
     */
    @Override
    public Reservation update(Reservation reservation) {
        return entityManager.merge(reservation);
    }


}
