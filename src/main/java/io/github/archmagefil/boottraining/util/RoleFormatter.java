package io.github.archmagefil.boottraining.util;

import io.github.archmagefil.boottraining.model.Role;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class RoleFormatter implements Formatter<Role> {
    @Override
    public Role parse(String s, Locale locale) throws ParseException {
        for (int i = 0; i < 100; i++) {
            System.out.println(s);
            System.out.println(locale.getCountry());
        }
        Role role = new Role();
        if (s.length() > 5) {
            role.setRoleTitle(s);
        } else {
            role.setId(Long.valueOf(s));
        }
        return role;
    }

    @Override
    public String print(Role role, Locale locale) {
        return String.valueOf(role.getId());
    }
}
