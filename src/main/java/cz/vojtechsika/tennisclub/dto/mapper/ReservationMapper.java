package cz.vojtechsika.tennisclub.dto.mapper;

import cz.vojtechsika.tennisclub.dto.ReservationDTO;
import cz.vojtechsika.tennisclub.dto.ReservationResponseDTO;
import cz.vojtechsika.tennisclub.entity.Reservation;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReservationMapper {


    public Reservation toReservationEntity(ReservationDTO reservationDTO) {
        Reservation reservation = new Reservation();
        reservation.setStartTime(reservationDTO.getStartTime());
        reservation.setEndTime(reservationDTO.getEndTime());
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setGameType(reservationDTO.getGameType());
        reservation.setDeleted(false);  // default value
        return reservation; // price, User, Court must be set in Service layer
    }

    public ReservationResponseDTO toReservationResponseDTO(Reservation reservation) {
        ReservationResponseDTO reservationResponseDTO = new ReservationResponseDTO();
        reservationResponseDTO.setPrice(reservation.getPrice());
        return reservationResponseDTO;
    }


}
