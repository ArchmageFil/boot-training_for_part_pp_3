package io.github.archmagefil.boottraining.service;

import io.github.archmagefil.boottraining.model.User;
import io.github.archmagefil.boottraining.model.UnverifiedUser;
import io.github.archmagefil.boottraining.model.UserDto;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    Long addUser(UnverifiedUser tempUser);

    Long updateUser(UnverifiedUser tempUser);

    boolean deleteUser(long id);

    User findById(long id);

    User findByUsername(String email);

    String clearDB();

    List<UserDto> getDtoUserList();

    UserDto getDtoUser(long id);
}