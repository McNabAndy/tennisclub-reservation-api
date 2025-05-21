package cz.vojtechsika.tennisclub.dao;

import cz.vojtechsika.tennisclub.entity.Reservation;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationDAO {

    void save(Reservation reservation);

    List<Reservation> findAllByDate(LocalDateTime date);

    Reservation findById(Long id);

    List<Reservation> findAllByCourtNumber(int courtNumber);

    List<Reservation> findAllByPhoneNumber(String phoneNumber, boolean futureOnly);

    Reservation update(Reservation reservation);
}
