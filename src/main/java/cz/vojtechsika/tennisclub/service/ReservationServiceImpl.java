package cz.vojtechsika.tennisclub.service;

import cz.vojtechsika.tennisclub.dao.CourtDAO;
import cz.vojtechsika.tennisclub.dao.ReservationDAO;
import cz.vojtechsika.tennisclub.dao.UserDAO;
import cz.vojtechsika.tennisclub.dto.ReservationDTO;
import cz.vojtechsika.tennisclub.dto.ReservationResponseDTO;
import cz.vojtechsika.tennisclub.dto.mapper.ReservationMapper;
import cz.vojtechsika.tennisclub.dto.mapper.UserMapper;
import cz.vojtechsika.tennisclub.entity.Court;
import cz.vojtechsika.tennisclub.entity.Reservation;
import cz.vojtechsika.tennisclub.entity.User;
import cz.vojtechsika.tennisclub.enums.GameType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {

    private ReservationDAO reservationDAO;

    private UserDAO userDAO;

    private CourtDAO courtDAO;

    private ReservationMapper reservationMapper;

    private UserMapper userMapper;


    @Autowired
    public ReservationServiceImpl(ReservationDAO theReservationDAO,
                                  UserDAO theUserDAO,
                                  CourtDAO theCourtDAO,
                                  ReservationMapper theReservationMapper,
                                  UserMapper theUserMapper) {
        reservationDAO = theReservationDAO;
        userDAO = theUserDAO;
        courtDAO = theCourtDAO;
        reservationMapper = theReservationMapper;
        userMapper = theUserMapper;

    }




    @Override
    public ReservationResponseDTO createReservation(ReservationDTO reservationDTO) {
        // Ověření rezervací když je validní začne to nastavovat další atributy
        List<Reservation> reservations = reservationDAO.findAllByDate(reservationDTO.getStartTime());
        if (isValidReservation(reservationDTO.getStartTime(), reservationDTO.getEndTime(), reservations)){

            Reservation reservation = reservationMapper.toReservationEntity(reservationDTO);

            // Najít uživatele dle telefonu a uložit jej pokud neexistuje vytvořit ho a uložit
            Optional<User> optionalUser = userDAO.findByPhone(reservationDTO.getPhoneNumber());

            if (optionalUser.isPresent()){
                reservation.setUser(optionalUser.get());
            } else {
                User newUser = userMapper.mapFromReservationDTO(reservationDTO);
                reservation.setUser(newUser);
            }

            // najít kurt dle čísla uložit jej do rezervace a vypočítat cenu za tento kurt
            Optional<Court> optionalCourt = courtDAO.findByCourtNumber(reservationDTO.getCourtNumber());
            if (optionalCourt.isPresent()){
                Court court = optionalCourt.get();

                BigDecimal pricePerMinute = court.getSurfaceType().getMinutePrice();
                Long minutes = getDurationInMinutes(reservationDTO.getStartTime(), reservationDTO.getEndTime());
                GameType gameType = reservationDTO.getGameType();

                BigDecimal finalPrice = priceCalculator(pricePerMinute, minutes, gameType );

                reservation.setCourt(court);
                reservation.setPrice(finalPrice);
            }

            reservationDAO.save(reservation);
            // persist sice nic nevrací ale mám už po metodě presist pridelene id a entita je v contextu takže s ní mohu dale pracovat
            return reservationMapper.toReservationResponseDTO(reservation);
        }
        return null;  // tady to musim vyřešit
    }

    // tady zvažit vyhození vyjímky a to samé u validačních metod
    private boolean isValidReservation(LocalDateTime startTime, LocalDateTime endTime, List<Reservation> existsReservations) {

        boolean isTwoHoursLimit = isTwoHoursLimit(startTime, endTime);
        boolean isValidReservationInterval = isValidReservationInterval(startTime, endTime);
        boolean hasOverlappingReservation = hasOverlappingReservation(startTime, endTime, existsReservations);

        return isTwoHoursLimit && isValidReservationInterval && !hasOverlappingReservation;
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

    private BigDecimal priceCalculator(BigDecimal pricePerMinute, long minutes, GameType gameType) {
        BigDecimal basePrice = pricePerMinute.multiply(BigDecimal.valueOf(minutes));
        if (gameType.equals(GameType.DOUBLES)){
            return basePrice.multiply(BigDecimal.valueOf(2));
        }
        return basePrice;
    }

}

