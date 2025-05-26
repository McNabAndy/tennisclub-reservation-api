package cz.vojtechsika.tennisclub.dao;

import cz.vojtechsika.tennisclub.dto.response.CourtResponseDTO;
import cz.vojtechsika.tennisclub.entity.Court;
import cz.vojtechsika.tennisclub.entity.SurfaceType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CourtDAOImpl implements CourtDAO {

    private EntityManager entityManager;

    @Autowired
    public CourtDAOImpl(EntityManager theEntityManager) {
        entityManager = theEntityManager;
    }

    @Override
    public Court save(Court court) {
        entityManager.persist(court);
        return court;
    }


    @Override
    public Optional<Court> findById(Long id) {
        TypedQuery<Court> query = entityManager.createQuery("SELECT c FROM Court c WHERE " +
                        "c.id = :id AND c.deleted = :isFalse", Court.class).
                setParameter("id", id).
                setParameter("isFalse", false);

        List<Court> result = query.getResultList();     // opakuje se asi přepsat do nějaké pomocné metody do nějaké utils
        if (result.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(result.get(0));
        }

    }

    @Override
    public Optional<Court> findByCourtNumber(int courtNumber) {
        TypedQuery<Court> query = entityManager.createQuery("SELECT c FROM Court c WHERE " +
                        "c.courtNumber = :courtNumber AND c.deleted = :isFalse", Court.class).
                setParameter("courtNumber", courtNumber).
                setParameter("isFalse", false);

        List<Court> result = query.getResultList();
        if (result.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(result.get(0));
        }

    }

    @Override
    public List<Court> findAll() {
        TypedQuery<Court> query = entityManager.createQuery("SELECT c FROM Court c WHERE " +
                        "c.deleted = :isFalse", Court.class).
                setParameter("isFalse", false);

        return query.getResultList();
    }

    @Override
    public List<Court> findAllBySurfaceTypeId(Long id) {
        TypedQuery<Court> query = entityManager.createQuery("SELECT c FROM Court c WHERE " +
                        "c.deleted = :isFalse AND c.surfaceType.id = :id", Court.class).
                setParameter("id", id).
                setParameter("isFalse", false);

        return query.getResultList();
    }


    @Override
    public Court update(Court court) {
        return entityManager.merge(court);
    }


}
