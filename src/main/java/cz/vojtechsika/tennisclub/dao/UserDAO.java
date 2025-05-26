package cz.vojtechsika.tennisclub.dao;

import cz.vojtechsika.tennisclub.entity.User;

import java.util.Optional;

public interface UserDAO {

    User save(User user);

    Optional<User> findByPhone(String phone);
}
