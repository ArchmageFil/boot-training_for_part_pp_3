package io.github.archmagefil.boottraining.dao;

import io.github.archmagefil.boottraining.model.User;

import java.util.List;
import java.util.Optional;

public interface DaoUser {

    long add(User user);

    void update(User user);

    void deleteById(long id);

    List<User> getAll();

    Optional<User> findByEmail(String email);

    boolean isEmailExist(String email);

    User findById(long id);

    String clearDB();
}