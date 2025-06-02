package cz.vojtechsika.tennisclub.dao;

import cz.vojtechsika.tennisclub.entity.User;

import java.util.Optional;
/**
 * UserDAO is an interface defining data access methods for {@link User} entities.
 * It provides basic operations to save a new user and to retrieve a user by their phone number.
 * Implementations of this interface interact with the persistence layer (e.g., using JPA or JDBC).
 *
 */
public interface UserDAO {


    /**
     * Persists a new {@link User} entity in the database.
     *
     * @param user The {@link User} entity to be saved.
     * @return The persisted {@link User} entity, including any generated values (e.g., ID).
     */
    User save(User user);


    /**
     * Retrieves a {@link User} by their phone number.
     *
     * @param phone The phone number of the user to retrieve.
     * @return An {@link Optional} containing the {@link User} if found, or empty if no user exists with the given phone number.
     */
    Optional<User> findByPhone(String phone);
}
