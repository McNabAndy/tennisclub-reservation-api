package cz.vojtechsika.tennisclub.api;

import cz.vojtechsika.tennisclub.dto.CourtDTO;
import cz.vojtechsika.tennisclub.dto.response.CourtResponseDTO;
import cz.vojtechsika.tennisclub.service.CourtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * CourtController is a REST controller that manages requests related to tennis courts.
 * It provides endpoints for creating, retrieving, updating, and deleting courts.
 * The controller uses the {@link CourtService} to handle the business logic for court management.
 */
@RestController
@RequestMapping("api/courts")
public class CourtController {

    /**
     * The service responsible for managing court-related logic.
     */
    private final CourtService courtService;


    /**
     * Constructs a new CourtController with the provided {@link CourtService}.
     *
     * @param theCourtService The service responsible for managing court-related logic.
     */
    @Autowired
    public CourtController(CourtService theCourtService) {
        courtService = theCourtService;
    }


    /**
     * Creates a new tennis court.
     * The court details are provided in the request body as a {@link CourtDTO}.
     *
     * @param courtDTO The details of the court to be created.
     * @return ResponseEntity containing the created {@link CourtResponseDTO} and HTTP status 201 (Created).
     */
    @PostMapping("/create")
    public ResponseEntity<CourtResponseDTO> createCourt(@RequestBody CourtDTO courtDTO) {
        return new ResponseEntity<>(courtService.save(courtDTO), HttpStatus.CREATED);
    }

    /**
     * Retrieves a tennis court by its ID.
     * The court ID is passed as a path variable.
     *
     * @param id The ID of the court to be retrieved.
     * @return ResponseEntity containing the {@link CourtResponseDTO} of the requested court and HTTP status 200 (OK).
     */
    @GetMapping("/{id}")
    public ResponseEntity<CourtResponseDTO> getCourt(@PathVariable Long id) {
        return new ResponseEntity<>(courtService.getCourtById(id), HttpStatus.OK);
    }

    /**
     * Retrieves all tennis courts.
     * Returns a list of {@link CourtResponseDTO} for all courts in the system.
     *
     * @return ResponseEntity containing a list of all {@link CourtResponseDTO} and HTTP status 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<CourtResponseDTO>> getAllCourts() {
        return new ResponseEntity<>(courtService.getAllCourts(), HttpStatus.OK);
    }


    /**
     * Updates an existing tennis court.
     * The court ID is provided as a path variable, and the updated court details are provided in the request body.
     *
     * @param id The ID of the court to be updated.
     * @param courtDTO The updated details of the court.
     * @return ResponseEntity containing the updated {@link CourtResponseDTO} and HTTP status 200 (OK).
     */
    @PutMapping("/{id}")
    public ResponseEntity<CourtResponseDTO> updateCourt(@PathVariable Long id, @RequestBody CourtDTO courtDTO) {
        return new ResponseEntity<>(courtService.updateCourt(courtDTO, id), HttpStatus.OK);
    }


    /**
     * Deletes a tennis court by its ID.
     * The court ID is passed as a path variable, and the court is removed from the system.
     *
     * @param id The ID of the court to be deleted.
     * @return ResponseEntity containing a message confirming the deletion and HTTP status 200 (OK).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCourt(@PathVariable Long id) {
        courtService.deleteCourt(id);
        Map<String, String> response = Map.of("message", "Court with id " + id + " was deleted");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }







}
