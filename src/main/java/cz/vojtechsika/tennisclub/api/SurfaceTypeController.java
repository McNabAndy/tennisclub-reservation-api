package cz.vojtechsika.tennisclub.api;

import cz.vojtechsika.tennisclub.dto.SurfaceTypeDTO;
import cz.vojtechsika.tennisclub.dto.response.SurfaceTypeResponseDTO;
import cz.vojtechsika.tennisclub.service.SurfaceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
/**
 * SurfaceTypeController is a REST controller responsible for managing surface types for tennis courts.
 * It provides endpoints to create, retrieve, update, and delete surface types.
 * The controller interacts with the {@link SurfaceTypeService} to handle the business logic for surface type management.
 */
@RestController
@RequestMapping("/api/surfaces")
public class SurfaceTypeController {

    /**
     * The service responsible for managing surface type-related logic.
     */
    private final SurfaceTypeService surfaceTypeService;


    /**
     * Constructs a new SurfaceTypeController with the provided {@link SurfaceTypeService}.
     *
     * @param theSurfaceTpeService The service responsible for managing surface type-related logic.
     */
    @Autowired
    public SurfaceTypeController(SurfaceTypeService theSurfaceTpeService) {
        surfaceTypeService = theSurfaceTpeService;
    }


    /**
     * Creates a new surface type.
     * The surface type details are provided in the request body as a {@link SurfaceTypeDTO}.
     *
     * @param surfaceTypeDTO The details of the surface type to be created.
     * @return ResponseEntity containing the created {@link SurfaceTypeResponseDTO} and HTTP status 201 (Created).
     */
    @PostMapping(value= "/create",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SurfaceTypeResponseDTO> createSurfaceType(@RequestBody SurfaceTypeDTO surfaceTypeDTO) {
        return new ResponseEntity<>(surfaceTypeService.saveSurfaceType(surfaceTypeDTO), HttpStatus.CREATED);
    }


    /**
     * Retrieves a surface type by its ID.
     * The surface type ID is passed as a path variable.
     *
     * @param id The ID of the surface type to be retrieved.
     * @return ResponseEntity containing the {@link SurfaceTypeResponseDTO} of the requested surface type and HTTP status 200 (OK).
     */
    @GetMapping("/{id}")
    public ResponseEntity<SurfaceTypeResponseDTO> getSurfaceType(@PathVariable Long id) {
        return new ResponseEntity<>(surfaceTypeService.getSurfaceTypeById(id),HttpStatus.OK);
    }


    /**
     * Retrieves all surface types.
     * Returns a list of all surface types in the system.
     *
     * @return ResponseEntity containing a list of all {@link SurfaceTypeResponseDTO} and HTTP status 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<SurfaceTypeResponseDTO>> getAllSurfaceTypes() {
        return new ResponseEntity<>(surfaceTypeService.getAllSurfaceTypes(), HttpStatus.OK);
    }


    /**
     * Updates an existing surface type.
     * The surface type ID is provided as a path variable, and the updated surface type details are provided in the request body.
     *
     * @param id The ID of the surface type to be updated.
     * @param surfaceTypeDTO The updated details of the surface type.
     * @return ResponseEntity containing the updated {@link SurfaceTypeResponseDTO} and HTTP status 200 (OK).
     */
    @PutMapping("/{id}")
    public ResponseEntity<SurfaceTypeResponseDTO> updateSurfaceType(@PathVariable Long id,
                                                                    @RequestBody SurfaceTypeDTO surfaceTypeDTO) {
        return new ResponseEntity<>(surfaceTypeService.updateSurfaceType(surfaceTypeDTO, id), HttpStatus.OK);
    }


    /**
     * Deletes a surface type by its ID.
     * The surface type ID is passed as a path variable, and the surface type is removed from the system.
     *
     * @param id The ID of the surface type to be deleted.
     * @return ResponseEntity containing a message confirming the deletion and HTTP status 200 (OK).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteSurfaceType(@PathVariable Long id) {
        surfaceTypeService.deleteSurfaceType(id);
        Map<String, String> response = Map.of("message", "Surface Type with ID " + id + " was deleted.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
