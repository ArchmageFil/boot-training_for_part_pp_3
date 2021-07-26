package io.github.archmagefil.boottraining.service;

import io.github.archmagefil.boottraining.model.User;
import io.github.archmagefil.boottraining.model.UserDto;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    User addUser(UserDto tempUser) throws IllegalArgumentException;

    String updateUser(UserDto tempUser);

    String deleteUser(long id);

    User findById(long id);

    User findByUsername(String email);

    String clearDB();
}