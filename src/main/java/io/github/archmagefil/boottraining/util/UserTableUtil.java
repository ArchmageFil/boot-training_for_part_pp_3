package io.github.archmagefil.boottraining.util;

import io.github.archmagefil.boottraining.model.UnverifiedUser;
import io.github.archmagefil.boottraining.model.VisitorMessages;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
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

    public boolean isInvalidUser(UnverifiedUser user) {
        if (isInvalidEmail(user.getEmail())) {
            messages.setResult(words.getProperty("wrong_email"));
            return true;
        } else if (user.getPassword() == null) {
            messages.setResult(words.getProperty("password_empty"));
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

    @Autowired
    public void setMessages(VisitorMessages messages) {
        this.messages = messages;
    }

    public void result(String message) {
        messages.setResult(words.getProperty(message));
    }
}