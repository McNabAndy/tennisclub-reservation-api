package cz.vojtechsika.tennisclub.dao;

import cz.vojtechsika.tennisclub.dto.response.CourtResponseDTO;
import cz.vojtechsika.tennisclub.entity.Court;
import cz.vojtechsika.tennisclub.entity.SurfaceType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * CourtDAOImpl is an implementation of the {@link CourtDAO} interface that provides database access for managing {@link Court} entities.
 * This implementation uses JPA (Jakarta Persistence API) and the {@link EntityManager} to interact with the underlying database.
 * It includes methods for performing CRUD operations on courts, including finding courts by various criteria (ID, court number, surface type).
 */
@Repository
public class CourtDAOImpl implements CourtDAO {

    /**
     * The entityManager used to interact with the database.
     */
    private final EntityManager entityManager;


    /**
     * Constructs a new CourtDAOImpl with the provided {@link EntityManager}.
     *
     * @param theEntityManager The {@link EntityManager} used to interact with the database.
     */
    @Autowired
    public CourtDAOImpl(EntityManager theEntityManager) {
        entityManager = theEntityManager;
    }


    /**
     * Saves a new or updated {@link Court} entity in the database.
     *
     * @param court The {@link Court} entity to be saved.
     * @return The saved {@link Court} entity, including any generated values (e.g., ID).
     */
    @Override
    public Court save(Court court)  {
        entityManager.persist(court);
        return court;
    }


    /**
     * Retrieves a {@link Court} by its ID.
     * This method only retrieves courts that are not marked as deleted.
     *
     * @param id The ID of the court to be retrieved.
     * @return An {@link Optional} containing the {@link Court} if found, or empty if not found.
     */
    @Override
    public Optional<Court> findById(Long id) {
        TypedQuery<Court> query = entityManager.createQuery("SELECT c FROM Court c WHERE " +
                        "c.id = :id AND c.deleted = :isFalse", Court.class)
                .setParameter("id", id)
                .setParameter("isFalse", false);

        List<Court> result = query.getResultList();     // opakuje se asi přepsat do nějaké pomocné metody do nějaké utils
        if (result.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(result.get(0));
        }

    }


    /**
     * Retrieves a {@link Court} by its court number.
     * This method only retrieves courts that are not marked as deleted.
     *
     * @param courtNumber The court number to search for.
     * @return An {@link Optional} containing the {@link Court} if found, or empty if not found.
     */
    @Override
    public Optional<Court> findByCourtNumber(int courtNumber) {
        TypedQuery<Court> query = entityManager.createQuery("SELECT c FROM Court c WHERE " +
                        "c.courtNumber = :courtNumber AND c.deleted = :isFalse", Court.class)
                .setParameter("courtNumber", courtNumber)
                .setParameter("isFalse", false);

        List<Court> result = query.getResultList();
        if (result.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(result.get(0));
        }

    }


    /**
     * Retrieves all courts that are not marked as deleted.
     *
     * @return A list of all {@link Court} entities.
     */
    @Override
    public List<Court> findAll() {
        TypedQuery<Court> query = entityManager.createQuery("SELECT c FROM Court c WHERE " +
                        "c.deleted = :isFalse", Court.class)
                .setParameter("isFalse", false);

        return query.getResultList();
    }


    /**
     * Retrieves all courts associated with a specific surface type ID.
     * This method only retrieves courts that are not marked as deleted.
     *
     * @param id The ID of the surface type to filter courts by.
     * @return A list of {@link Court} entities associated with the given surface type ID.
     */
    @Override
    public List<Court> findAllBySurfaceTypeId(Long id) {
        TypedQuery<Court> query = entityManager.createQuery("SELECT c FROM Court c WHERE " +
                        "c.deleted = :isFalse AND c.surfaceType.id = :id", Court.class)
                .setParameter("id", id)
                .setParameter("isFalse", false);

        return query.getResultList();
    }


    /**
     * Updates an existing {@link Court} entity in the database.
     * This method merges the provided {@link Court} entity with the existing one.
     *
     * @param court The {@link Court} entity with updated values.
     * @return The updated {@link Court} entity.
     */
    @Override
    public Court update(Court court) {
        return entityManager.merge(court);
    }


}
