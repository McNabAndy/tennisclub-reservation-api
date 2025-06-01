package cz.vojtechsika.tennisclub.dao;


import cz.vojtechsika.tennisclub.entity.Reservation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationDAOImpl implements ReservationDAO {


    private final EntityManager entityManager;

    @Autowired
    public ReservationDAOImpl(EntityManager theEntityManager) {
        entityManager = theEntityManager;
    }


    @Override
    public Reservation create(Reservation reservation) {
        entityManager.persist(reservation);
        return reservation;
    }

    @Override
    public List<Reservation> findAllByDateAndCourtNumber(LocalDateTime date, int courtNumber, Long excludeId) {

        LocalDateTime from = date.toLocalDate().atStartOfDay();
        LocalDateTime to = from.plusDays(1);

        String baseQuery = "SELECT r FROM Reservation r WHERE r.startTime >= :from AND r.startTime < :to " +
                "AND r.deleted = :isFalse AND r.court.courtNumber = :courtNumber";

        if (excludeId != null) {
            baseQuery += " AND r.id <> :excludeId";
        }
        TypedQuery<Reservation> query = entityManager.createQuery(baseQuery, Reservation.class).
                setParameter("from", from).
                setParameter("to", to).
                setParameter("isFalse", false).
                setParameter("courtNumber", courtNumber);
        if (excludeId != null) {
            query.setParameter("excludeId", excludeId);
        }

        return query.getResultList();
    }



    @Override
    public Optional<Reservation> findById(Long id) {

        TypedQuery<Reservation> query = entityManager.createQuery("SELECT r FROM Reservation r WHERE " +
                        "r.id = :id AND r.deleted = :isFalse AND r.court.deleted = :isFalse AND" +
                        " r.user.deleted = :isFalse", Reservation.class).
                setParameter("id", id).
                setParameter("isFalse", false);

        List<Reservation> result = query.getResultList();
        if (result.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(result.get(0));
        }

    }




    @Override
    public List<Reservation> findAllByCourtNumber(int courtNumber) {

        TypedQuery<Reservation> query = entityManager.createQuery(
                     "SELECT r FROM Reservation r WHERE r.court.courtNumber = :courtNumber AND r.deleted = :isFalse " +
                        "ORDER BY r.createdAt DESC ", Reservation.class).
                setParameter("courtNumber", courtNumber).
                setParameter("isFalse", false);
        return query.getResultList();
    }



    @Override
    public List<Reservation> findAllByPhoneNumber(String phoneNumber, boolean futureOnly) {
        String baseQuery = "SELECT r FROM Reservation r WHERE r.user.phoneNumber = :phoneNumber";

        if (futureOnly) {
            baseQuery = baseQuery + " AND r.startTime >= :from";
        }
        baseQuery += " AND r.deleted = :isFalse ORDER BY r.startTime ASC";

        TypedQuery<Reservation> query = entityManager.createQuery(baseQuery, Reservation.class).
                setParameter("phoneNumber", phoneNumber).
                setParameter("isFalse", false);
        if (futureOnly) {
            query.setParameter("from", LocalDateTime.now());
        }
        return query.getResultList();
    }


    // false tu mám připravené pro budoucí filtraci
    @Override
    public List<Reservation> findAll() {

        TypedQuery<Reservation> query = entityManager.createQuery("SELECT r FROM Reservation r WHERE" +
                " r.deleted = :isFalse ORDER BY r.startTime ASC",Reservation.class).
                setParameter("isFalse", false);

        return query.getResultList();
    }


    @Override
    public Reservation update(Reservation reservation) {
        return entityManager.merge(reservation);
    }


}
