package cz.vojtechsika.tennisclub.dao;

import cz.vojtechsika.tennisclub.entity.SurfaceType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * SurfaceTypeDAOImpl is an implementation of the {@link SurfaceTypeDAO} interface that provides
 * database access for managing {@link SurfaceType} entities. This class uses JPA (Jakarta Persistence API)
 * and an {@link EntityManager} to interact with the underlying database. It includes methods for saving,
 * updating, and retrieving surface types, filtering out any entities marked as deleted.
 */
@Repository
public class SurfaceTypeDAOImpl implements SurfaceTypeDAO {

    /**
     * The entityManager used to interact with the database.
     */
    EntityManager entityManager;

    /**
     * Constructs a new SurfaceTypeDAOImpl with the provided {@link EntityManager}.
     *
     * @param theEntityManager The {@link EntityManager} used to interact with the database.
     */
    @Autowired
    public SurfaceTypeDAOImpl(EntityManager theEntityManager) {
        entityManager = theEntityManager;
    }


    /**
     * Persists a new {@link SurfaceType} entity in the database.
     *
     * @param surfaceType The {@link SurfaceType} entity to be saved.
     * @return The persisted {@link SurfaceType} entity, including any generated values (e.g., ID).
     */
    @Override
    public SurfaceType save(SurfaceType surfaceType) {
        entityManager.persist(surfaceType);
        return surfaceType;
    }


    /**
     * Retrieves a {@link SurfaceType} by its ID, only if it is not marked as deleted.
     *
     * @param id The ID of the surface type to retrieve.
     * @return An {@link Optional} containing the {@link SurfaceType} if found and not deleted;
     *         otherwise, {@link Optional#empty()}.
     */
    @Override
    public Optional<SurfaceType> findById(Long id) {
        TypedQuery<SurfaceType> query = entityManager.createQuery("SELECT s FROM SurfaceType s WHERE " +
                    "s.id = :id AND s.deleted = :isFalse", SurfaceType.class)
                .setParameter("id", id)
                .setParameter("isFalse", false);

        List<SurfaceType> result = query.getResultList();

        if (result.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(result.get(0));
        }
    }


    /**
     * Retrieves all {@link SurfaceType} entities that are not marked as deleted.
     *
     * @return A list of all non-deleted {@link SurfaceType} entities.
     */
    @Override
    public List<SurfaceType> findAll() {
        TypedQuery<SurfaceType> query = entityManager.createQuery("SELECT s FROM  SurfaceType s WHERE " +
                        "s.deleted = :isFalse", SurfaceType.class)
                .setParameter("isFalse", false);

        return query.getResultList();
    }


    /**
     * Updates an existing {@link SurfaceType} entity in the database. This method merges the provided
     * {@link SurfaceType} entity with the existing one in the persistence context, applying any changed fields
     * while preserving other properties.
     *
     * @param surfaceType The {@link SurfaceType} entity with updated values to be merged.
     * @return The merged {@link SurfaceType} entity reflecting the updates.
     */
    @Override
    public SurfaceType update(SurfaceType surfaceType) {
        return entityManager.merge(surfaceType);
    }


}

