package cz.vojtechsika.tennisclub.dao;


import cz.vojtechsika.tennisclub.entity.Reservation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ReservationDAOImpl implements ReservationDAO {

    // @Trnsactional budu řešit na urovni servisn vrstvi - nezapomenout


    private EntityManager entityManager;

    @Autowired
    public ReservationDAOImpl(EntityManager theEntityManager) {
        entityManager = theEntityManager;
    }


    @Override
    public void save(Reservation reservation) {
        entityManager.persist(reservation);
    }

    @Override
    public List<Reservation> findAllByDate(LocalDateTime date) {

        LocalDateTime from = date.toLocalDate().atStartOfDay();
        LocalDateTime to = from.plusDays(1);

        TypedQuery<Reservation> query = entityManager.createQuery(
                "SELECT r FROM Reservation r WHERE r.startTime >= :from AND r.startTime < :to " +
                        "AND r.deleted = :isDeleted", Reservation.class);

        query.setParameter("from", from);
        query.setParameter("to", to);
        query.setParameter("isDeleted", false);

        return query.getResultList();
    }

    @Override
    public Reservation findById(Long id) {
        return entityManager.find(Reservation.class, id);
    }

    @Override
    public List<Reservation> findAllByCourtNumber(int courtNumber) {

        TypedQuery<Reservation> query = entityManager.createQuery(
                "SELECT r FROM Reservation r WHERE r.court.courtNumber = :courtNumber AND r.deleted = :isDeleted " +
                        "ORDER BY r.createdAt DESC ", Reservation.class);

        query.setParameter("courtNumber", courtNumber);
        query.setParameter("isDeleted", false);
        return query.getResultList();
    }

    @Override
    public List<Reservation> findAllByPhoneNumber(String phoneNumber, boolean futureOnly) {
        String baseQuery = "SELECT r FROM Reservation r WHERE r.user.phoneNumber = :phoneNumber";

        if (futureOnly) {
            baseQuery = baseQuery + " AND r.startTime >= :from";
        }
        baseQuery = baseQuery + " AND r.deleted = :isDeleted ORDER BY r.startTime ASC";

        TypedQuery<Reservation> query = entityManager.createQuery(baseQuery, Reservation.class);
        query.setParameter("phoneNumber", phoneNumber);
        query.setParameter("isDeleted", false);
        if (futureOnly) {
            query.setParameter("from", LocalDateTime.now());
        }
        return query.getResultList();
    }

    @Override
    public Reservation update(Reservation reservation) {
        return entityManager.merge(reservation);
    }


}
