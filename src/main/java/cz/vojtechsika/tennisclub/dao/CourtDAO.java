package cz.vojtechsika.tennisclub.dao;


import cz.vojtechsika.tennisclub.entity.Court;


import java.util.List;
import java.util.Optional;

/**
 * CourtDAO is an interface that defines the Data Access Object (DAO) for the {@link Court} entity.
 * It provides methods to interact with the database and perform CRUD (Create, Read, Update, Delete) operations for tennis courts.
 * This interface is typically implemented by a class that interacts with the underlying database.
 */
public interface CourtDAO {

    /**
     * Saves a new court entity to the database.
     *
     * @param court The {@link Court} entity to be saved.
     * @return The saved {@link Court} entity, including any generated values (e.g., ID).
     */
    Court save(Court court);

    /**
     * Retrieves a court by its ID.
     *
     * @param id The ID of the court to be retrieved.
     * @return An {@link Optional} containing the {@link Court} entity if found, or empty if not found.
     */
    Optional <Court> findById(Long id);

    /**
     * Retrieves all courts in the system.
     *
     * @return A list of all {@link Court} entities in the database.
     */
    List<Court> findAll();

    /**
     * Retrieves all courts that belong to a specific surface type by surface type ID.
     *
     * @param id The surface type ID to filter courts by.
     * @return A list of {@link Court} entities associated with the given surface type.
     */
    List<Court> findAllBySurfaceTypeId(Long id);

    /**
     * Updates an existing court in the database.
     *
     * @param court The {@link Court} entity with updated values.
     * @return The updated {@link Court} entity.
     */
    Court update(Court court);

    /**
     * Retrieves a court by its court number.
     *
     * @param courtNumber The court number to search for.
     * @return An {@link Optional} containing the {@link Court} entity if found, or empty if not found.
     */
    Optional<Court> findByCourtNumber(int courtNumber);

}
