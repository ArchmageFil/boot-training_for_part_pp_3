package io.github.archmagefil.boottraining.model;

import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Value
public class RoleDto {
    Long id;
    String roleTitle;

    public static List<RoleDto> listOf(List<Role> roles) {
        List<RoleDto> dtoRoles = new ArrayList<>();
        for (Role role : roles) {
            dtoRoles.add(new RoleDto(role.getId(), role.getRoleTitle()));
        }
        return dtoRoles;
    }
}
