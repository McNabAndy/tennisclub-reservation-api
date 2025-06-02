package cz.vojtechsika.tennisclub.service;

import cz.vojtechsika.tennisclub.dto.SurfaceTypeDTO;
import cz.vojtechsika.tennisclub.dto.response.SurfaceTypeResponseDTO;

import java.util.List;

/**
 * SurfaceTypeService defines the business operations for managing {@link cz.vojtechsika.tennisclub.entity.SurfaceType} entities.
 * It provides methods to create, retrieve, update, and delete surface types, returning data transfer objects for API responses.
 * <p>
 * Implementations of this interface handle validation, mapping, and coordination between controllers and data access layers (DAOs/repositories).</p>
 */
public interface SurfaceTypeService {

    /**
     * Creates a new surface type based on the provided {@link SurfaceTypeDTO}.
     * <p>
     * Validates the DTO, persists the entity, and returns a {@link SurfaceTypeResponseDTO} representing the saved surface type.
     * </p>
     *
     * @param surfaceTypeDTO The {@link SurfaceTypeDTO} containing name and minute price.
     * @return A {@link SurfaceTypeResponseDTO} with details of the newly created surface type.
     */
    SurfaceTypeResponseDTO saveSurfaceType(SurfaceTypeDTO surfaceTypeDTO);


    /**
     * Retrieves a surface type by its ID.
     *
     * @param id The unique ID of the surface type to retrieve.
     * @return A {@link SurfaceTypeResponseDTO} representing the requested surface type.
     * @throws cz.vojtechsika.tennisclub.exception.SurfaceTypeNotFoundException if no surface type with the given ID exists.
     */
    SurfaceTypeResponseDTO getSurfaceTypeById(Long id);


    /**
     * Retrieves all surface types in the system.
     *
     * @return A list of {@link SurfaceTypeResponseDTO} for all existing surface types.
     * @throws cz.vojtechsika.tennisclub.exception.SurfaceTypeNotFoundException if no surface types are found.
     */
    List<SurfaceTypeResponseDTO> getAllSurfaceTypes();


    /**
     * Updates an existing surface type identified by {@code id} using data from the provided {@link SurfaceTypeDTO}.
     * <p>
     * Validates that the surface type exists, applies updates to name and minute price, and returns the updated details.
     * </p>
     *
     * @param surfaceTypeDTO The {@link SurfaceTypeDTO} containing updated name and/or minute price.
     * @param id             The unique ID of the surface type to update.
     * @return A {@link SurfaceTypeResponseDTO} representing the updated surface type.
     * @throws cz.vojtechsika.tennisclub.exception.SurfaceTypeNotFoundException if no surface type with the given ID exists.
     */
    SurfaceTypeResponseDTO updateSurfaceType(SurfaceTypeDTO surfaceTypeDTO, Long id);


    /**
     * Deletes a surface type by its ID.
     * <p>
     * Performs a soft deletion by marking the surface type's {@code deleted} flag as true.
     * If the surface type does not exist, a {@link cz.vojtechsika.tennisclub.exception.SurfaceTypeNotFoundException} is thrown.
     * </p>
     *
     * @param id The unique ID of the surface type to delete.
     * @throws cz.vojtechsika.tennisclub.exception.SurfaceTypeNotFoundException if no surface type with the given ID exists.
     */
    void deleteSurfaceType(Long id);
}
