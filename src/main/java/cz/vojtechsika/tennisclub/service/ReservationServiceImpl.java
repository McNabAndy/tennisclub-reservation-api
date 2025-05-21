package cz.vojtechsika.tennisclub.service;

import cz.vojtechsika.tennisclub.dao.ReservationDAO;
import cz.vojtechsika.tennisclub.dto.ReservationDTO;
import cz.vojtechsika.tennisclub.dto.ReservationResponseDTO;
import cz.vojtechsika.tennisclub.dto.mapper.ReservationMapper;
import cz.vojtechsika.tennisclub.entity.Reservation;
import cz.vojtechsika.tennisclub.enums.GameType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    private ReservationDAO reservationDAO;

    private ReservationMapper reservationMapper;

    @Autowired
    public ReservationServiceImpl(ReservationDAO theReservationDAO, ReservationMapper theReservationMapper) {
        reservationDAO = theReservationDAO;
        reservationMapper = theReservationMapper;
    }


    private BigDecimal priceCalculator(BigDecimal pricePerMinute, long minutes, GameType gameType) {
        BigDecimal basePrice = pricePerMinute.multiply(BigDecimal.valueOf(minutes));
        if (gameType.equals(GameType.DOUBLES)){
            return basePrice.multiply(BigDecimal.valueOf(2));
        }
        return basePrice;
    }


    private Long getDurationInMinutes(LocalDateTime startTime, LocalDateTime endTime) {
        return Duration.between(startTime, endTime).toMinutes();
    }


    private boolean isTwoHoursLimit(LocalDateTime startTime, LocalDateTime endTime) {
        long minutes = getDurationInMinutes(startTime, endTime);
        return minutes > 0 && minutes <= 120;
    }


    private boolean isValidReservationInterval(LocalDateTime startTime, LocalDateTime endTime) {
        LocalTime allowedStartTime = LocalTime.of(10,0);
        LocalTime allowedEndTime = LocalTime.of(22,0);

        LocalTime start = startTime.toLocalTime();
        LocalTime end = endTime.toLocalTime();

        return !start.isBefore(allowedStartTime) && !end.isAfter(allowedEndTime);
    }

    private boolean hasOverlappingReservation(LocalDateTime startTime, LocalDateTime endTime, List<Reservation> reservations) {
        for (Reservation reservation : reservations) {
            if (reservation.getStartTime().isBefore(endTime) && reservation.getEndTime().isAfter(startTime)) {
                return true;
            }
        } return false;
    }

    @Override
    public ReservationResponseDTO createReservation(ReservationDTO reservationDTO) {

        return null;
    }
}
