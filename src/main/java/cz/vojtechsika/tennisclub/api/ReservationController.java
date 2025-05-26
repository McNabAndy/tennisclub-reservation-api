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

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService theReservationService) {
        reservationService = theReservationService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> getReservation(@PathVariable Long id) {
        return new ResponseEntity<>(reservationService.getReservationById(id), HttpStatus.OK);
    }

    @GetMapping("/court/{courtNumber}")
    public ResponseEntity<List<ReservationResponseDTO>> getReservationByCourtNumber(@PathVariable int courtNumber) {
        return new ResponseEntity<>(reservationService.getReservationByCourtNumber(courtNumber), HttpStatus.OK);
    }

    @GetMapping("/phone/{phoneNumber}")
    public ResponseEntity<List<ReservationResponseDTO>> getReservationByPhoneNumber(@PathVariable String phoneNumber,
                                                                                    @RequestParam(defaultValue = "false") boolean futureOnly) {
        return new ResponseEntity<>(reservationService.
                getReservationByPhoneNumber(phoneNumber, futureOnly), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponseDTO>> getAllReservations() {
        return new ResponseEntity<>(reservationService.getAllReservations(), HttpStatus.OK);
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<Map<String, String>> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        Map<String, String> response = Map.of("message", "Reservation with id " + id + " was deleted");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("create")
    public ResponseEntity<ReservationResponseDTO> createReservation(@RequestBody ReservationDTO reservationDTO) {
        return new ResponseEntity<>(reservationService.createReservation(reservationDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> updateReservation(@PathVariable Long id, @RequestBody ReservationDTO reservationDTO) {
        return  new ResponseEntity<>(reservationService.updateReservation(reservationDTO, id), HttpStatus.OK);
    }



}
