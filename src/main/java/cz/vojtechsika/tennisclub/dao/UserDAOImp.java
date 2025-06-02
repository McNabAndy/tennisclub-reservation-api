package cz.vojtechsika.tennisclub.dao;

import cz.vojtechsika.tennisclub.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * UserDAOImp is an implementation of the {@link UserDAO} interface that provides
 * database access for managing {@link User} entities. This class uses JPA (Jakarta Persistence API)
 * and an {@link EntityManager} to interact with the underlying database. It includes methods to
 * save a new user and to retrieve a user by their phone number, filtering out any entities marked as deleted.
 */
@Repository
public class UserDAOImp implements UserDAO {


    /**
     * The entityManager used to interact with the database.
     */
    private EntityManager entityManager;


    /**
     * Constructs a new UserDAOImp with the provided {@link EntityManager}.
     *
     * @param theEntityManager The {@link EntityManager} used to interact with the database.
     */
    @Autowired
    public UserDAOImp(EntityManager theEntityManager) {
        entityManager = theEntityManager;
    }


    /**
     * Persists a new {@link User} entity in the database.
     *
     * @param user The {@link User} entity to be saved.
     * @return The persisted {@link User} entity, including any generated values (e.g., ID).
     */
    @Override
    public User save(User user) {
        entityManager.persist(user);
        return user;
    }


    /**
     * Retrieves a {@link User} by their phone number, only if the user is not marked as deleted.
     *
     * @param phone The phone number of the user to retrieve.
     * @return An {@link Optional} containing the {@link User} if found and not deleted;
     *         otherwise, {@link Optional#empty()}.
     */
    @Override
    public Optional<User> findByPhone(String phone) {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE" +
                    " u.phoneNumber = :phone AND u.deleted = :isFalse", User.class)
                .setParameter("phone", phone)
                .setParameter("isFalse", false);

        List<User> result = query.getResultList();
        if (result.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(result.get(0));
        }
    }


}
