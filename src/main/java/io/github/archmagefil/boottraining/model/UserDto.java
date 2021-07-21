package io.github.archmagefil.boottraining.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@NoArgsConstructor
public class UserDto {
    private String name = null;
    private String surname = null;
    private String email;
    private String password;
    private Boolean goodAcc = true;
    private String role;
    private List<Role> roles = new ArrayList<>();

    public User createUser() {
        return new User(name, surname, email, password, goodAcc, roles);
    }
}