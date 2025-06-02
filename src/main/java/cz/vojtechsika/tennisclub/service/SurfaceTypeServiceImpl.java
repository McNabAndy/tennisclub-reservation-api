package cz.vojtechsika.tennisclub.service;

import cz.vojtechsika.tennisclub.dao.CourtDAO;
import cz.vojtechsika.tennisclub.dao.SurfaceTypeDAO;
import cz.vojtechsika.tennisclub.dto.SurfaceTypeDTO;
import cz.vojtechsika.tennisclub.dto.response.SurfaceTypeResponseDTO;
import cz.vojtechsika.tennisclub.dto.mapper.SurfaceTypeMapper;
import cz.vojtechsika.tennisclub.entity.Court;
import cz.vojtechsika.tennisclub.entity.SurfaceType;
import cz.vojtechsika.tennisclub.exception.SurfaceTypeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing {@link SurfaceType} entities. Handles creation, retrieval,
 * updating, and deletion of surface types. When deleting a surface type, all associated courts
 * are also marked as deleted via calls to {@link CourtService}.
 * <p>
 * Business logic includes validation that the surface type exists before updating or deleting,
 * and cascading deletions to related courts.</p>
 *
 */
@Service
public class SurfaceTypeServiceImpl implements SurfaceTypeService {

    /**
     * DAO for surface type persistence operations.
     */
    private final SurfaceTypeDAO surfaceTypeDAO;

    /**
     * Mapper to convert between {@link SurfaceTypeDTO} and {@link SurfaceType}.
     */
    private final SurfaceTypeMapper surfaceTypeMapper;

    /**
     * DAO for court persistence operations.
     */
    private final CourtDAO courtDAO;

    /**
     * Service for court business logic and cascading deletions.
     */
    private final CourtService courtService;


    /**
     * Constructs a new SurfaceTypeServiceImpl with required DAOs, mappers, and services.
     *
     * @param theSurfaceTypeDAO    DAO for surface type persistence operations.
     * @param theSurfaceTypeMapper Mapper to convert between {@link SurfaceTypeDTO} and {@link SurfaceType}.
     * @param theCourtDAO          DAO for court persistence operations.
     * @param theCourtService      Service for court business logic and cascading deletions.
     */
    @Autowired
    public SurfaceTypeServiceImpl(SurfaceTypeDAO theSurfaceTypeDAO,
                                  SurfaceTypeMapper theSurfaceTypeMapper,
                                  CourtDAO theCourtDAO,
                                  CourtService theCourtService) {
        surfaceTypeDAO = theSurfaceTypeDAO;
        surfaceTypeMapper = theSurfaceTypeMapper;
        courtDAO = theCourtDAO;
        courtService = theCourtService;


    }


    /**
     * Creates a new surface type based on the provided {@link SurfaceTypeDTO}.
     * <p>
     * Maps the DTO to an entity, persists it, and returns a {@link SurfaceTypeResponseDTO}
     * representing the saved surface type.
     * </p>
     *
     * @param surfaceTypeDTO The {@link SurfaceTypeDTO} containing the name and minute price.
     * @return A {@link SurfaceTypeResponseDTO} with details of the newly created surface type.
     */
    @Transactional
    @Override
    public SurfaceTypeResponseDTO saveSurfaceType(SurfaceTypeDTO surfaceTypeDTO) {
        SurfaceType surfaceType = surfaceTypeMapper.toEntity(surfaceTypeDTO);
        SurfaceType saveSurfaceType = surfaceTypeDAO.save(surfaceType);

        return surfaceTypeMapper.toResponseDTO(saveSurfaceType);
    }


    /**
     * Updates an existing surface type identified by {@code id} using data from the provided {@link SurfaceTypeDTO}.
     * <p>
     * Verifies that a surface type with the given ID exists; if not, throws {@link SurfaceTypeNotFoundException}.
     * Maps the DTO to an entity (setting its ID), merges it, and returns a {@link SurfaceTypeResponseDTO}
     * representing the updated surface type.
     * </p>
     *
     * @param surfaceTypeDTO The {@link SurfaceTypeDTO} containing updated name and minute price.
     * @param id             The unique ID of the surface type to update.
     * @return A {@link SurfaceTypeResponseDTO} with details of the updated surface type.
     * @throws SurfaceTypeNotFoundException if no surface type with the given ID exists.
     */
    @Transactional
    @Override
    public SurfaceTypeResponseDTO updateSurfaceType(SurfaceTypeDTO surfaceTypeDTO, Long id) {
        Optional<SurfaceType> optionalSurfaceType = surfaceTypeDAO.findById(id);

        if (optionalSurfaceType.isEmpty()) {
            throw new SurfaceTypeNotFoundException("Update failed: Surface type with ID " + id + " does not exist");
        }

        SurfaceType surfaceType = surfaceTypeMapper.toEntity(surfaceTypeDTO);
        surfaceType.setId(id);

        SurfaceType updateSurfaceType = surfaceTypeDAO.update(surfaceType);
        return surfaceTypeMapper.toResponseDTO(updateSurfaceType);
    }


    /**
     * Deletes a surface type by its ID. Marks the surface type as deleted (soft-delete),
     * then retrieves all {@link Court} entities associated with that surface type and delegates
     * their deletion to {@link CourtService#deleteCourt(Long)}.
     * <p>
     * If no surface type with the given ID exists, throws {@link SurfaceTypeNotFoundException}.
     * </p>
     *
     * @param id The unique ID of the surface type to delete.
     * @throws SurfaceTypeNotFoundException if no surface type with the given ID exists.
     */
    @Transactional
    @Override
    public void deleteSurfaceType(Long id) {

        Optional <SurfaceType> optionalSurfaceType = surfaceTypeDAO.findById(id);
        if (optionalSurfaceType.isPresent()) {
            SurfaceType surfaceType = optionalSurfaceType.get();
            surfaceType.setDeleted(true);
            surfaceTypeDAO.update(surfaceType);

            List<Court> courts = courtDAO.findAllBySurfaceTypeId(id);

            if (!courts.isEmpty()) {
                courts.stream()
                        .forEach(court -> {
                            courtService.deleteCourt(court.getId());
                        });
            }

        } else {
            throw new SurfaceTypeNotFoundException("Delete failed: Surface type with ID " + id + " was not found.");
        }
    }


    /**
     * Retrieves a surface type by its ID.
     * <p>
     * If a surface type with the given ID exists and is not marked deleted, returns a
     * {@link SurfaceTypeResponseDTO} representing it. Otherwise, throws {@link SurfaceTypeNotFoundException}.
     * </p>
     *
     * @param id The unique ID of the surface type to retrieve.
     * @return A {@link SurfaceTypeResponseDTO} with details of the requested surface type.
     * @throws SurfaceTypeNotFoundException if no surface type with the given ID exists.
     */
    @Override
    public SurfaceTypeResponseDTO getSurfaceTypeById(Long id) {
        Optional<SurfaceType> optionalSurfaceType = surfaceTypeDAO.findById(id);
        if (optionalSurfaceType.isPresent()) {
            return surfaceTypeMapper.toResponseDTO(optionalSurfaceType.get());
        } else {
            throw new SurfaceTypeNotFoundException("Surface type with id " + id + " not found");
        }
    }


    /**
     * Retrieves all surface types in the system.
     * <p>
     * Returns a list of {@link SurfaceTypeResponseDTO} for all existing (non-deleted) surface types.
     * If no surface types are found, throws {@link SurfaceTypeNotFoundException}.
     * </p>
     *
     * @return A list of {@link SurfaceTypeResponseDTO}.
     * @throws SurfaceTypeNotFoundException if no surface types exist in the database.
     */
    @Override
    public List<SurfaceTypeResponseDTO> getAllSurfaceTypes() {
        List<SurfaceType> surfaceTypes = surfaceTypeDAO.findAll();
        if (surfaceTypes.isEmpty()){
            throw new SurfaceTypeNotFoundException("No Surface types found in the database");
        }
        return surfaceTypes.stream()
                .map(surfaceType -> surfaceTypeMapper.toResponseDTO(surfaceType))
                .toList();
    }


}


