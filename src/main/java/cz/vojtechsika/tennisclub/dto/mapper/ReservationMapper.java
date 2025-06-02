package cz.vojtechsika.tennisclub.dto.mapper;

import cz.vojtechsika.tennisclub.dto.ReservationDTO;
import cz.vojtechsika.tennisclub.dto.response.ReservationResponseDTO;
import cz.vojtechsika.tennisclub.entity.Reservation;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
/**
 * ReservationMapper is responsible for converting between {@link ReservationDTO} and {@link Reservation} entities,
 * as well as building {@link ReservationResponseDTO} objects for API responses. It encapsulates the mapping logic
 * for reservation-related data structures, including formatting dates and times for the response.
 */
@Component
public class ReservationMapper {


    /**
     * Converts a {@link ReservationDTO} to a new {@link Reservation} entity. The created entity will have its
     * start time, end time, game type, and created timestamp set from the DTO or current time. The deleted flag
     * is initialized to {@code false}. Note that associated fields such as price, user, and court must be set
     * separately in the service layer.
     *
     * @param reservationDTO The {@link ReservationDTO} containing reservation data from the client.
     * @return A new {@link Reservation} entity populated with values from {@code reservationDTO}.
     */
    public Reservation toReservationEntity(ReservationDTO reservationDTO) {
        Reservation reservation = new Reservation();

        reservation.setStartTime(reservationDTO.getStartTime());
        reservation.setEndTime(reservationDTO.getEndTime());
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setGameType(reservationDTO.getGameType());
        reservation.setDeleted(false);

        return reservation;
    }


    /**
     * Builds a {@link ReservationResponseDTO} from a {@link Reservation} entity. The response DTO will include
     * reservation ID, court number, user name, phone number, formatted start and end times, formatted game date,
     * game type, price, and formatted creation date. Date and time fields are formatted using the SHORT style
     * based on the system's default locale.
     *
     * @param reservation The {@link Reservation} entity retrieved from the database.
     * @return A {@link ReservationResponseDTO} containing data formatted for client consumption.
     */
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


    /**
     * Updates the mutable fields of an existing {@link Reservation} entity based on a {@link ReservationDTO}.
     * This method modifies the start time, end time, and game type of the given reservation. Remaining fields
     * (e.g., price, user, court, deleted flag) remain unchanged.
     *
     * @param reservationDTO The {@link ReservationDTO} containing updated reservation data.
     * @param reservation    The existing {@link Reservation} entity to be updated.
     * @return The same {@link Reservation} entity instance, with updated start time, end time, and game type.
     */
    public Reservation updateReservationFromDTO(ReservationDTO reservationDTO, Reservation reservation) {;
        reservation.setStartTime(reservationDTO.getStartTime());
        reservation.setEndTime(reservationDTO.getEndTime());
        reservation.setGameType(reservationDTO.getGameType());
        return reservation;
    }


}
