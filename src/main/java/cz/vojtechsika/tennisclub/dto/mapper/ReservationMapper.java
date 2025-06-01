package cz.vojtechsika.tennisclub.dto.mapper;

import cz.vojtechsika.tennisclub.dto.ReservationDTO;
import cz.vojtechsika.tennisclub.dto.response.ReservationResponseDTO;
import cz.vojtechsika.tennisclub.entity.Reservation;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Component
public class ReservationMapper {


    public Reservation toReservationEntity(ReservationDTO reservationDTO) {
        Reservation reservation = new Reservation();

        reservation.setStartTime(reservationDTO.getStartTime());
        reservation.setEndTime(reservationDTO.getEndTime());
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setGameType(reservationDTO.getGameType());
        reservation.setDeleted(false);

        return reservation; // price, user, court must be set in service layer
    }

    public ReservationResponseDTO toReservationResponseDTO(Reservation reservation) {
        ReservationResponseDTO reservationResponseDTO = new ReservationResponseDTO();

        reservationResponseDTO.setId(reservation.getId());
        reservationResponseDTO.setCourtNumber(reservation.getCourt().getCourtNumber());
        reservationResponseDTO.setUserName(reservation.getUser().getUserName());
        reservationResponseDTO.setPhoneNumber(reservation.getUser().getPhoneNumber());
        reservationResponseDTO.setStartTime(reservation.getStartTime().toLocalTime().
                format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)));
        reservationResponseDTO.setEndTime(reservation.getEndTime().toLocalTime().
                format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)));
        reservationResponseDTO.setGameDate(reservation.getStartTime().toLocalDate().
                format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
        reservationResponseDTO.setGameType(reservation.getGameType());
        reservationResponseDTO.setPrice(reservation.getPrice());
        reservationResponseDTO.setCreatedAt(reservation.getCreatedAt().toLocalDate().
                format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
        return reservationResponseDTO;
    }

    public Reservation updateReservationFromDTO(ReservationDTO reservationDTO, Reservation reservation) {;
        reservation.setStartTime(reservationDTO.getStartTime());
        reservation.setEndTime(reservationDTO.getEndTime());
        reservation.setGameType(reservationDTO.getGameType());
        return reservation;
    }


}
