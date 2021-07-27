package io.github.archmagefil.boottraining.service;

import io.github.archmagefil.boottraining.model.User;
import io.github.archmagefil.boottraining.model.UnverifiedUser;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    void addUser(UnverifiedUser tempUser);

    void updateUser(UnverifiedUser tempUser);

    String deleteUser(long id);

    User find(long id);

    User findByUsername(String email);

    String clearDB();
}