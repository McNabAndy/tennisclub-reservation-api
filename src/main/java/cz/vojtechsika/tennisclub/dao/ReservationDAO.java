package cz.vojtechsika.tennisclub.dao;

import cz.vojtechsika.tennisclub.entity.Reservation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationDAO {



    Optional<Reservation> findById(Long id);

    List<Reservation> findAllByCourtNumber(int courtNumber);

    List<Reservation> findAllByPhoneNumber(String phoneNumber, boolean futureOnly);

    List<Reservation> findAll();

    Reservation create(Reservation reservation);

    List<Reservation> findAllByDateAndCourtNumber(LocalDateTime date, int courtNumber, Long excludeId);

    Reservation update(Reservation reservation);;
}
