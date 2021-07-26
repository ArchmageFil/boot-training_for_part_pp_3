package io.github.archmagefil.boottraining.controller;

import io.github.archmagefil.boottraining.model.User;
import io.github.archmagefil.boottraining.model.UserDto;
import io.github.archmagefil.boottraining.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
class UsersRestController {
    UserService userService;

    public UsersRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
    }

    @GetMapping("/users/{id:[\\d]+}")
    public ResponseEntity<User> getUserById(@PathVariable long id) {
        User user = userService.findById(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/users")
    public ResponseEntity<User> putUserInDb(@RequestBody UserDto tempUser) {
        User user;
        try {
            user = userService.addUser(tempUser);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new MultiValueMapAdapter<>(
                    Map.of("reason", List.of(e.getMessage()))),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

//    @PatchMapping("/users/{id:[\\d]+}")
//    public ResponseEntity<User> patchUser(@RequestBody UserDto tempUser) {
//        User user;
//        try {
//            user = userService.updateUser(tempUser);
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity<>(new MultiValueMapAdapter<>(
//                    Map.of("reason", List.of(e.getMessage()))),
//                    HttpStatus.BAD_REQUEST);
//        }
//        return new ResponseEntity<>(user, HttpStatus.OK);
//    }
}
