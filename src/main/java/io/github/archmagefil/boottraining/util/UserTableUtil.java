package io.github.archmagefil.boottraining.util;

import io.github.archmagefil.boottraining.model.UserDto;
import lombok.Getter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserTableUtil {
    @Getter
    private final Properties words = new Properties();
    private final Pattern p = Pattern.compile(
            "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&’*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$");
    @Getter
    private String message;

    public UserTableUtil() {
        try {
            words.loadFromXML(Files.newInputStream(
                    new ClassPathResource("words.xml").getFile().toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isInvalidUser(UserDto user) {
        if (isInvalidEmail(user.getEmail())) {
            message = words.getProperty("wrong_email");
            return true;
        } else if (user.getPassword() == null) {
            message = words.getProperty("password_empty");
            return true;
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
}