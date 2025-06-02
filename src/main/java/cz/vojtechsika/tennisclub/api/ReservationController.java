package cz.vojtechsika.tennisclub.api;

import cz.vojtechsika.tennisclub.dto.ReservationDTO;
import cz.vojtechsika.tennisclub.dto.response.ReservationResponseDTO;
import cz.vojtechsika.tennisclub.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * ReservationController is a REST controller that manages reservations for tennis courts.
 * It provides endpoints for creating, retrieving, updating, and deleting reservations.
 * The controller interacts with the {@link ReservationService} to handle the business logic for reservation management.
 */
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    /**
     * The service responsible for managing reservation-related logic.
     */
    private final ReservationService reservationService;


    /**
     * Constructs a new ReservationController with the provided {@link ReservationService}.
     *
     * @param theReservationService The service responsible for managing reservation-related logic.
     */
    @Autowired
    public ReservationController(ReservationService theReservationService) {
        reservationService = theReservationService;
    }


    /**
     * Retrieves a reservation by its ID.
     * The reservation ID is passed as a path variable.
     *
     * @param id The ID of the reservation to be retrieved.
     * @return ResponseEntity containing the {@link ReservationResponseDTO} of the requested reservation and HTTP status 200 (OK).
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> getReservation(@PathVariable Long id) {
        return new ResponseEntity<>(reservationService.getReservationById(id), HttpStatus.OK);
    }


    /**
     * Retrieves all reservations for a specific court number.
     * The court number is passed as a path variable.
     *
     * @param courtNumber The court number for which reservations are to be retrieved.
     * @return ResponseEntity containing a list of {@link ReservationResponseDTO} for the specified court and HTTP status 200 (OK).
     */
    @GetMapping("/court/{courtNumber}")
    public ResponseEntity<List<ReservationResponseDTO>> getReservationByCourtNumber(@PathVariable int courtNumber) {
        return new ResponseEntity<>(reservationService.getReservationByCourtNumber(courtNumber), HttpStatus.OK);
    }


    /**
     * Retrieves all reservations for a specific phone number.
     * Optionally, future reservations only can be filtered via the `futureOnly` query parameter.
     *
     * @param phoneNumber The phone number for which reservations are to be retrieved.
     * @param futureOnly Whether to only return future reservations (default is false).
     * @return ResponseEntity containing a list of {@link ReservationResponseDTO} and HTTP status 200 (OK).
     */
    @GetMapping("/phone/{phoneNumber}")
    public ResponseEntity<List<ReservationResponseDTO>> getReservationByPhoneNumber(@PathVariable String phoneNumber,
                                                                                    @RequestParam(defaultValue = "false") boolean futureOnly) {
        return new ResponseEntity<>(reservationService.
                getReservationByPhoneNumber(phoneNumber, futureOnly), HttpStatus.OK);
    }

    /**
     * Retrieves all reservations.
     * Returns a list of all reservations in the system.
     *
     * @return ResponseEntity containing a list of all {@link ReservationResponseDTO} and HTTP status 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<ReservationResponseDTO>> getAllReservations() {
        return new ResponseEntity<>(reservationService.getAllReservations(), HttpStatus.OK);
    }


    /**
     * Deletes a reservation by its ID.
     * The reservation ID is passed as a path variable, and the reservation is removed from the system.
     *
     * @param id The ID of the reservation to be deleted.
     * @return ResponseEntity containing a message confirming the deletion and HTTP status 200 (OK).
     */
    @DeleteMapping({"/{id}"})
    public ResponseEntity<Map<String, String>> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        Map<String, String> response = Map.of("message", "Reservation with id " + id + " was deleted");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * Creates a new reservation.
     * The reservation details are provided in the request body as a {@link ReservationDTO}.
     *
     * @param reservationDTO The details of the reservation to be created.
     * @return ResponseEntity containing the created {@link ReservationResponseDTO} and HTTP status 201 (Created).
     */
    @PostMapping("create")
    public ResponseEntity<ReservationResponseDTO> createReservation(@RequestBody ReservationDTO reservationDTO) {
        return new ResponseEntity<>(reservationService.createReservation(reservationDTO), HttpStatus.CREATED);
    }


    /**
     * Updates an existing reservation.
     * The reservation ID is provided as a path variable, and the updated reservation details are provided in the request body.
     *
     * @param id The ID of the reservation to be updated.
     * @param reservationDTO The updated details of the reservation.
     * @return ResponseEntity containing the updated {@link ReservationResponseDTO} and HTTP status 200 (OK).
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> updateReservation(@PathVariable Long id, @RequestBody ReservationDTO reservationDTO) {
        return  new ResponseEntity<>(reservationService.updateReservation(reservationDTO, id), HttpStatus.OK);
    }


}
