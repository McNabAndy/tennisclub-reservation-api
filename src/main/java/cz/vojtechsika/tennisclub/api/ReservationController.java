package cz.vojtechsika.tennisclub.api;

import cz.vojtechsika.tennisclub.dto.ReservationDTO;
import cz.vojtechsika.tennisclub.dto.ReservationResponseDTO;
import cz.vojtechsika.tennisclub.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ReservationController {

    private ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService theReservationService) {
        reservationService = theReservationService;
    }

    @PostMapping("reservations")
    public ReservationResponseDTO create(@RequestBody ReservationDTO reservationDTO) {
        return reservationService.createReservation(reservationDTO);
    }



}
