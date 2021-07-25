package io.github.archmagefil.boottraining.service;

import io.github.archmagefil.boottraining.model.User;
import io.github.archmagefil.boottraining.model.UserDto;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    String addUser(UserDto tempUser);

    String updateUser(UserDto tempUser);

    String deleteUser(long id);

    User find(long id);

    User findByUsername(String email);

    String clearDB();
}