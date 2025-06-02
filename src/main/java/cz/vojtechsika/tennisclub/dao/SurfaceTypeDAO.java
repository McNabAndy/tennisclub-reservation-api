package cz.vojtechsika.tennisclub.dao;

import cz.vojtechsika.tennisclub.entity.SurfaceType;

import java.util.List;
import java.util.Optional;
/**
 * SurfaceTypeDAO is an interface defining data access methods for {@link SurfaceType} entities.
 * It provides CRUD operations to save, retrieve, update, and list surface types in the database.
 * Implementations of this interface interact with the persistence layer (e.g., using JPA or JDBC).
 */
public interface SurfaceTypeDAO {

    /**
     * Persists a new {@link SurfaceType} entity in the database.
     *
     * @param surfaceType The {@link SurfaceType} entity to be saved.
     * @return The saved {@link SurfaceType} entity, including any generated values (e.g., ID).
     */
    SurfaceType save(SurfaceType surfaceType);


    /**
     * Retrieves a {@link SurfaceType} by its ID.
     *
     * @param id The ID of the surface type to retrieve.
     * @return An {@link Optional} containing the {@link SurfaceType} if found, or empty if not found.
     */
    Optional<SurfaceType> findById(Long id);


    /**
     * Retrieves all {@link SurfaceType} entities from the database.
     *
     * @return A list of all {@link SurfaceType} entities.
     */
    List<SurfaceType> findAll();


    /**
     * Updates an existing {@link SurfaceType} entity in the database.
     * This method merges the provided {@link SurfaceType} with the existing one in the persistence context,
     * applying any changed fields while preserving other properties.
     *
     * @param surfaceType The {@link SurfaceType} entity with updated values to be merged.
     * @return The updated {@link SurfaceType} entity reflecting the changes.
     */
    SurfaceType update(SurfaceType surfaceType);
}
