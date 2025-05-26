package cz.vojtechsika.tennisclub.service;

import cz.vojtechsika.tennisclub.dao.CourtDAO;
import cz.vojtechsika.tennisclub.dao.ReservationDAO;
import cz.vojtechsika.tennisclub.dao.UserDAO;
import cz.vojtechsika.tennisclub.dto.ReservationDTO;
import cz.vojtechsika.tennisclub.dto.response.ReservationResponseDTO;
import cz.vojtechsika.tennisclub.dto.mapper.ReservationMapper;
import cz.vojtechsika.tennisclub.dto.mapper.UserMapper;
import cz.vojtechsika.tennisclub.entity.Court;
import cz.vojtechsika.tennisclub.entity.Reservation;
import cz.vojtechsika.tennisclub.entity.User;
import cz.vojtechsika.tennisclub.enums.GameType;
import cz.vojtechsika.tennisclub.exception.CourtNotFoundException;
import cz.vojtechsika.tennisclub.exception.ReservationNotFoundException;
import cz.vojtechsika.tennisclub.exception.ReservationValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    @Override
    public ReservationResponseDTO createReservation(ReservationDTO reservationDTO) {
        LocalDateTime startTime = reservationDTO.getStartTime();
        LocalDateTime endTime = reservationDTO.getEndTime();
        int courtNumber = reservationDTO.getCourtNumber();

        List<Reservation> reservations = findAllReservationsByDateAndCourtNumber(startTime,courtNumber, null);

        if (isValidReservation(startTime, endTime, reservations)) {

            Court court = findCourtByCourtNumber(reservationDTO.getCourtNumber());

            Reservation reservation = reservationMapper.toReservationEntity(reservationDTO);
            reservation.setUser(getOrCreateUser(reservationDTO));
            reservation.setPrice(courtPriceCalculator(court,reservationDTO));
            reservation.setCourt(court);

            Reservation newReservation = reservationDAO.create(reservation);
            return reservationMapper.toReservationResponseDTO(newReservation);
        } else {
            throw new ReservationValidationException("Provided time range is invalid");
        }
    }

    @Transactional
    @Override
    public ReservationResponseDTO updateReservation(ReservationDTO reservationDTO, Long id) {
        LocalDateTime startTime = reservationDTO.getStartTime();
        LocalDateTime endTime = reservationDTO.getEndTime();
        int courtNumber = reservationDTO.getCourtNumber();

        List<Reservation> reservations = findAllReservationsByDateAndCourtNumber(startTime, courtNumber, id);
        if (isValidReservation(startTime, endTime, reservations)) {

            Optional <Reservation>  optionalReservation = reservationDAO.findById(id);
            if (optionalReservation.isEmpty()) {
                throw new ReservationNotFoundException("Reservation with id " + id + " not found");
            }
            Court court = findCourtByCourtNumber(courtNumber);
            Reservation findReservation = optionalReservation.get();

            Reservation reservation = reservationMapper.updateReservationFromDTO(reservationDTO, findReservation);
            reservation.setUser(getOrCreateUser(reservationDTO));
            reservation.setPrice(courtPriceCalculator(court,reservationDTO));
            reservation.setCourt(court);

            Reservation updatedReservation = reservationDAO.update(reservation);
            return reservationMapper.toReservationResponseDTO(updatedReservation);
        } else {
            throw new ReservationValidationException("Provided time range is invalid");
        }
    }




    @Override
    public ReservationResponseDTO getReservationById(Long id) {

        Optional<Reservation> optionalReservation = reservationDAO.findById(id);
        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();

            return reservationMapper.toReservationResponseDTO(reservation);
        } else {
            throw new ReservationNotFoundException("Reservation with id " + id + " not found");
        }
    }


    @Override
    public List<ReservationResponseDTO> getReservationByCourtNumber(int courtNumber) {

        List<Reservation> reservations = reservationDAO.findAllByCourtNumber(courtNumber);
        if (reservations.isEmpty()) {
            throw new ReservationNotFoundException("Reservation with court number " + courtNumber + " not found");
        }
        return reservations.stream().
                map( reservation ->
                        reservationMapper.toReservationResponseDTO(reservation)).
                toList();
    }

    @Override
    public List<ReservationResponseDTO> getReservationByPhoneNumber(String phoneNumber, boolean futureOnly) {

        List<Reservation> reservations = reservationDAO.findAllByPhoneNumber(phoneNumber,futureOnly);
        if (reservations.isEmpty()) {
            String message = "No Reservations with phone number " + phoneNumber;
            if (futureOnly) {
               message += " in future";
            }
            throw new ReservationNotFoundException(message);
        }

        return reservations.stream().
                map(reservation ->
                        reservationMapper.toReservationResponseDTO(reservation)).
                toList();
    }

    @Override
    public List<ReservationResponseDTO> getAllReservations() {

        List<Reservation> reservations = reservationDAO.findAll();
        if (reservations.isEmpty()) {
            throw new ReservationNotFoundException("No Reservations found");
        }
        return reservations.stream().
                map(reservation ->
                        reservationMapper.toReservationResponseDTO(reservation)).
                toList();
    }


    @Transactional
    @Override
    public void deleteReservation(Long id) {
        Optional<Reservation> optionalReservation = reservationDAO.findById(id);
        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();
            reservation.setDeleted(true);
            reservationDAO.update(reservation);
        } else {
            throw new ReservationNotFoundException("Delete failed: Reservation with id " + id + " not found");
        }
    }


    private boolean isValidReservation(LocalDateTime startTime, LocalDateTime endTime, List<Reservation> existsReservations) {

        boolean isInFuture = isInFuture(startTime);
        boolean isTwoHoursLimit = isTwoHoursLimit(startTime, endTime);
        boolean isValidReservationInterval = isValidReservationInterval(startTime, endTime);
        boolean hasOverlappingReservation = hasOverlappingReservation(startTime, endTime, existsReservations);

        return isInFuture && isTwoHoursLimit && isValidReservationInterval && !hasOverlappingReservation;
    }

    private boolean isInFuture(LocalDateTime startTime) {
        return startTime.isAfter(LocalDateTime.now());
    }

    private Long getDurationInMinutes(LocalDateTime startTime, LocalDateTime endTime) {
        return Duration.between(startTime, endTime).toMinutes();
    }


    private boolean isTwoHoursLimit(LocalDateTime startTime, LocalDateTime endTime) {
        long minutes = getDurationInMinutes(startTime, endTime);
        return minutes > 0 && minutes <= 120; // tady to dát asi do properti souboru abych to na jednom místě mohl změnit okno
    }


    private boolean isValidReservationInterval(LocalDateTime startTime, LocalDateTime endTime) {
        LocalTime allowedStartTime = LocalTime.of(10,0); // tady to dát asi do properti souboru abych to na jednom místě mohl změnit okno
        LocalTime allowedEndTime = LocalTime.of(22,0);   // tady to dát asi do properti souboru abych to na jednom místě mohl změnit okno

        LocalTime start = startTime.toLocalTime();
        LocalTime end = endTime.toLocalTime();

        return !start.isBefore(allowedStartTime) && !end.isAfter(allowedEndTime);
    }


    private boolean hasOverlappingReservation(LocalDateTime startTime, LocalDateTime endTime, List<Reservation> reservations) {
        return reservations.stream().
                anyMatch(reservation ->
                        reservation.getStartTime().isBefore(endTime) &&
                                reservation.getEndTime().isAfter(startTime)
                );
    }


    private BigDecimal courtPriceCalculator (Court court, ReservationDTO reservationDTO){
        Long minutes = getDurationInMinutes(reservationDTO.getStartTime(), reservationDTO.getEndTime());
        BigDecimal minutePrice = court.getSurfaceType().getMinutePrice();
        BigDecimal finalPrice = minutePrice.multiply(BigDecimal.valueOf(minutes));
        if (reservationDTO.getGameType().equals(GameType.DOUBLES)){
            return finalPrice.multiply(BigDecimal.valueOf(2));
        }
        return finalPrice;
    }


    private User getOrCreateUser(ReservationDTO reservationDTO) {
        Optional<User> optionalUser = userDAO.findByPhone(reservationDTO.getPhoneNumber());
        if (optionalUser.isPresent()){
            User findUser = optionalUser.get();

            if(!findUser.getUserName().equals(reservationDTO.getUserName())){
                findUser.setUserName(reservationDTO.getUserName());
                return userDAO.save(findUser);
            }
            return findUser;

        } else {
            User newUser = userMapper.mapFromReservationDTO(reservationDTO);
            return userDAO.save(newUser);
        }
    }

    private Court findCourtByCourtNumber(int courtNumber) {
        Optional<Court> optionalCourt = courtDAO.findByCourtNumber(courtNumber);
        if (optionalCourt.isPresent()){
            return optionalCourt.get();
        } else {
            throw new CourtNotFoundException("Court number " + courtNumber + " not found in database");
        }
    }

    private List<Reservation> findAllReservationsByDateAndCourtNumber(LocalDateTime date, int courtNumber, Long excludeId) {
        return reservationDAO.findAllByDateAndCourtNumber(date,courtNumber,excludeId);
    }

}

