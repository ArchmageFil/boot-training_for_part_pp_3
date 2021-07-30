package io.github.archmagefil.boottraining.util;

import io.github.archmagefil.boottraining.model.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserTableUtil {
    @Getter
    private final Properties words = new Properties();
    private final Pattern p = Pattern.compile(
            "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&’*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$");
    @Getter
    private VisitorMessages messages;

    public UserTableUtil(VisitorMessages messages) {
        this.messages = messages;
        try {
            words.loadFromXML(Files.newInputStream(
                    new ClassPathResource("words.xml").getFile().toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Function<User, UserDto> transformUserDto() {
        return u -> {
            if (u == null) throw new IllegalArgumentException("no_id_in_db");
            return UserDto.builder()
                    .id(u.getId())
                    .name(u.getName())
                    .surname(u.getSurname())
                    .age(u.getAge())
                    .email(u.getEmail())
                    .roles(RoleDto.listOf(u.getRoles()))
                    .build();
        };
    }

    public boolean isInvalidUser(UnverifiedUser user) {
        if (isInvalidEmail(user.getEmail())) {
            throw new IllegalArgumentException("wrong_email");

        } else if (user.getPassword() == null) {
            throw new IllegalArgumentException("password_empty");
        }
        return false;
    }

    public boolean isInvalidEmail(String email) {
        if (email == null) {
            return true;
        }
        Matcher syntax = p.matcher(email);
        return !syntax.matches();
    }

    @Autowired
    public void setMessages(VisitorMessages messages) {
        this.messages = messages;
    }

    public void result(String message) {
        messages.setResult(words.getProperty(message));
    }
}