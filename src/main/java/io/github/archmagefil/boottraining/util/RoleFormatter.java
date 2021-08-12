package io.github.archmagefil.boottraining.util;

import io.github.archmagefil.boottraining.model.Role;
import org.springframework.format.Formatter;

import java.util.Locale;

public class RoleFormatter implements Formatter<Role> {
    @Override
    public Role parse(String s, Locale locale) {
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
