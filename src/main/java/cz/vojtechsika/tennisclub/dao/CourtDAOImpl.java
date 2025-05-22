package cz.vojtechsika.tennisclub.dao;

import cz.vojtechsika.tennisclub.entity.Court;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
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
    public void save(Court court) {
        entityManager.persist(court);

    }

    @Override
    public List<Court> findAll() {

        TypedQuery<Court> query = entityManager.createQuery("SELECT c FROM Court c", Court.class);
        return query.getResultList();

    }

    @Override
    public Court findById(Long id) {
        return entityManager.find(Court.class, id);
    }

    @Override
    public Optional<Court> findByCourtNumber(int courtNumber) {
        try{
            TypedQuery<Court> query = entityManager.createQuery("SELECT c FROM Court c WHERE c.courtNumber = :courtNumber", Court.class);
            query.setParameter("courtNumber", courtNumber);
            return Optional.of(query.getSingleResult());
        }
        catch(NoResultException e){
            return Optional.empty();

        }



    }


}
