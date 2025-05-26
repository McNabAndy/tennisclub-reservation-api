package cz.vojtechsika.tennisclub.service;


import cz.vojtechsika.tennisclub.dto.ReservationDTO;
import cz.vojtechsika.tennisclub.dto.response.ReservationResponseDTO;

import java.util.List;

public interface ReservationService {

    ReservationResponseDTO createReservation(ReservationDTO reservationDTO);

    ReservationResponseDTO getReservationById(Long id);

    List<ReservationResponseDTO> getReservationByCourtNumber(int courtNumber);

    List<ReservationResponseDTO> getReservationByPhoneNumber(String phoneNumber, boolean futureOnly);

    List<ReservationResponseDTO> getAllReservations();

    ReservationResponseDTO updateReservation(ReservationDTO reservationDTO, Long id);

    public void deleteReservation(Long id);
}

