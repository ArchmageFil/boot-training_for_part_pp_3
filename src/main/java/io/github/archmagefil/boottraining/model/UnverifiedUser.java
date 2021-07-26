package io.github.archmagefil.boottraining.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@NoArgsConstructor
public class UnverifiedUser {
    private Long id;
    private String name;
    private String surname;
    private Integer age;
    private String email;
    private String password;
    private Boolean goodAcc = true;
    private List<Role> roles = new ArrayList<>();

    public User createUser() {
        return new User(name.trim(), surname.trim(),
                age, email, password, goodAcc, roles);
    }
}