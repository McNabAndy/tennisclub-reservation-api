package cz.vojtechsika.tennisclub.service;


import cz.vojtechsika.tennisclub.dto.ReservationDTO;
import cz.vojtechsika.tennisclub.dto.ReservationResponseDTO;

public interface ReservationService {

    ReservationResponseDTO createReservation(ReservationDTO reservationDTO);
}

