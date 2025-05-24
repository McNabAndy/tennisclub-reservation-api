package cz.vojtechsika.tennisclub.dao;

import cz.vojtechsika.tennisclub.entity.SurfaceType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        return surfaceType;  // presist sice nic nevrací ale už mi přidělí ID k entitě kterou uloží
    }

    @Override
    public SurfaceType findById(int id) {
        return entityManager.find(SurfaceType.class, id);
    }

    @Override
    public List<SurfaceType> findAll() {
        TypedQuery<SurfaceType> query = entityManager.createQuery("SELECT s FROM  SurfaceType s ORDER BY s.id DESC", SurfaceType.class);
        return query.getResultList();
    }


    // ještě sem přidat možnost editovat  tento "čísleník" povrchů
}
