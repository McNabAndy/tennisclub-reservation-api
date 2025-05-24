package cz.vojtechsika.tennisclub.dao;

import cz.vojtechsika.tennisclub.entity.SurfaceType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SurfaceTypeDAOImpl implements SurfaceTypeDAO {

    EntityManager entityManager;

    @Autowired
    public SurfaceTypeDAOImpl(EntityManager theEntityManager) {
        entityManager = theEntityManager;
    }


    @Override
    public SurfaceType save(SurfaceType surfaceType) {
        entityManager.persist(surfaceType);
        return surfaceType;
    }

    @Override
    public Optional<SurfaceType> findById(Long id) {
        TypedQuery<SurfaceType> query = entityManager.createQuery("SELECT s FROM SurfaceType s WHERE " +
                    "s.id = :id AND s.deleted = :isFalse", SurfaceType.class).
            setParameter("id", id).
            setParameter("isFalse", false);

        List<SurfaceType> result = query.getResultList();

        if (result.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(result.get(0));
        }
    }

    @Override
    public List<SurfaceType> findAll() {
        TypedQuery<SurfaceType> query = entityManager.createQuery("SELECT s FROM  SurfaceType s WHERE " +
                        "s.deleted = :isFalse", SurfaceType.class).
                setParameter("isFalse", false);

        return query.getResultList();
    }

    @Override
    public SurfaceType update(SurfaceType surfaceType) {
        return entityManager.merge(surfaceType);
    }


}

