package cz.vojtechsika.tennisclub.service;

import cz.vojtechsika.tennisclub.dto.CourtDTO;
import cz.vojtechsika.tennisclub.dto.response.CourtResponseDTO;

import java.util.List;
/**
 * CourtService defines the business operations for managing tennis courts.
 * It provides methods to create, retrieve, update, and delete courts, returning
 * data transfer objects for API responses.
 * <p>
 * Implementations of this interface handle validation, mapping, and coordination
 * between controllers and data access layers (DAOs/repositories).</p>
 *
 */
public interface CourtService {

    /**
     * Creates a new court based on the provided data.
     * Validates the {@link CourtDTO}, ensures the court number is unique, and persists
     * the entity. Returns a {@link CourtResponseDTO} representing the saved court.
     *
     * @param courtDTO The {@link CourtDTO} containing court number and surface type ID.
     * @return A {@link CourtResponseDTO} with details of the newly created court.
     */
    CourtResponseDTO save(CourtDTO courtDTO);


    /**
     * Retrieves a court by its ID.
     *
     * @param id The unique ID of the court to retrieve.
     * @return A {@link CourtResponseDTO} with details of the requested court.
     * @throws cz.vojtechsika.tennisclub.exception.CourtNotFoundException if no court with the given ID exists.
     */
    CourtResponseDTO getCourtById(Long id);


    /**
     * Retrieves all courts in the system.
     *
     * @return A list of {@link CourtResponseDTO}, one for each court.
     */
    List<CourtResponseDTO> getAllCourts();


    /**
     * Updates an existing court.
     * Validates that the court exists and that the new court number (if changed) is unique.
     * Applies updates from the provided {@link CourtDTO} and returns the updated court details.
     *
     * @param courtDTO The {@link CourtDTO} containing updated court data (court number and/or surface type ID).
     * @param id       The unique ID of the court to update.
     * @return A {@link CourtResponseDTO} with details of the updated court.
     * @throws cz.vojtechsika.tennisclub.exception.CourtNotFoundException           if no court with the given ID exists.
     * @throws cz.vojtechsika.tennisclub.exception.CourtNumberAlreadyExistsException if the new court number conflicts with an existing court.
     */
    CourtResponseDTO updateCourt(CourtDTO courtDTO, Long id);


    /**
     * Deletes a court by its ID.
     * Marks the court as deleted (soft-delete) to prevent future use while preserving historical data.
     *
     * @param id The unique ID of the court to delete.
     * @throws cz.vojtechsika.tennisclub.exception.CourtNotFoundException if no court with the given ID exists.
     */
    void deleteCourt(Long id);




}
