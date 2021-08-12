package io.github.archmagefil.boottraining.controller;

import io.github.archmagefil.boottraining.model.RoleDto;
import io.github.archmagefil.boottraining.model.UnverifiedUser;
import io.github.archmagefil.boottraining.model.User;
import io.github.archmagefil.boottraining.model.UserDto;
import io.github.archmagefil.boottraining.service.RoleService;
import io.github.archmagefil.boottraining.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * РЕСТ контроллер для КРУД операций со списком пользователей
 * <p>Пути:             запрос  разрешено      пояснение
 * <p>/api/users -      GET     A     получить список всех пользователей
 * <p>/api/users -      POST    A     публикация нового пользователя
 * <p>/api/users -      PUT     A     публикация измененной информации о пользователе
 * <p>/api/users/{id} - GET     U,A   получить пользователя по ид
 * <p>/api/users/{id} - DELETE  A     удаление пользователя по ид
 * <p>/api/roles -      GET     X     получение списка ролей
 * <p>/api/userlogin -  GET     X     получение данных идентифицированного пользователя
 * <p>
 * где А - ROLE_ADMIN, U - ROLE_USER, X - без ограничений.
 */
@RestController
@RequestMapping("/api")
class UsersRestController {
    UserService userService;
    RoleService roleService;

    public UsersRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getDtoUserList();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
    }

    @GetMapping("/users/{id:[\\d]+}")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<UserDto> getUserById(@PathVariable long id) {
        if (id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            UserDto user = userService.getDtoUser(id);
            return new ResponseEntity<>(user, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new MultiValueMapAdapter<>(
                    Map.of("reason", List.of(e.getMessage()))),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/users")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<UserDto> putUserInDb(@RequestBody UnverifiedUser tempUser) {
        try {

            long id = userService.addUser(tempUser);
            return new ResponseEntity<>(userService.getDtoUser(id), HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new MultiValueMapAdapter<>(
                    Map.of("reason", List.of(e.getMessage()))),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/users")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<UserDto> putUser(@RequestBody UnverifiedUser tempUser) {
        try {

            long id = userService.updateUser(tempUser);
            return new ResponseEntity<>(userService.getDtoUser(id), HttpStatus.OK);

        } catch (IllegalArgumentException e) {

            return new ResponseEntity<>(new MultiValueMapAdapter<>(
                    Map.of("reason", List.of(e.getMessage()))),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/users/{id:[\\d]+}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<String> deleteUser(@PathVariable long id) {
        if (id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (userService.deleteUser(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/roles")
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        List<RoleDto> roles = roleService.getAllRoles();
        if (roles.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(roles, HttpStatus.OK);
        }
    }

    @GetMapping("userlogin")
    public ResponseEntity<UserDto> authorize() {
        User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ResponseEntity<>(userService.getDtoUser(u.getId()), HttpStatus.OK);
    }
}
