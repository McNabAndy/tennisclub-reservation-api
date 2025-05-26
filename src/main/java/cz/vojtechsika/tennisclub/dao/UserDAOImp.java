package cz.vojtechsika.tennisclub.dao;

import cz.vojtechsika.tennisclub.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDAOImp implements UserDAO {

    private EntityManager entityManager;

    @Autowired
    public UserDAOImp(EntityManager theEntityManager) {
        entityManager = theEntityManager;
    }


    @Override
    public User save(User user) {
        entityManager.persist(user);
        return user;
    }


    // Tady používám Optional protože mi metoda getSingleResult muže vyhodit vyjímku když nic nenajde
    @Override
    public Optional<User> findByPhone(String phone) {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE" +
                    " u.phoneNumber = :phone AND u.deleted = :isFalse", User.class).
                setParameter("phone", phone).
                setParameter("isFalse", false);

        List<User> result = query.getResultList();
        if (result.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(result.get(0));
        }
    }




}
