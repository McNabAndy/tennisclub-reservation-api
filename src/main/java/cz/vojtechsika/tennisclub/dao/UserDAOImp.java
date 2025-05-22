package cz.vojtechsika.tennisclub.dao;

import cz.vojtechsika.tennisclub.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDAOImp implements UserDAO {

    private EntityManager entityManager;

    @Autowired
    public UserDAOImp(EntityManager theEntityManager) {
        entityManager = theEntityManager;
    }


    @Override
    public void save(User user) {
        entityManager.persist(user);
    }


    // Tady používám Optional protože mi metoda getSingleResult muže vyhodit vyjímku když nic nenajde
    @Override
    public Optional<User> findByPhone(String phone) {

        try{
            TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE" +
                    " u.phoneNumber = :phone", User.class);

            query.setParameter("phone", phone);

            return Optional.of(query.getSingleResult());
        }
        catch(NoResultException e){
            return Optional.empty();
        }

    }
}
