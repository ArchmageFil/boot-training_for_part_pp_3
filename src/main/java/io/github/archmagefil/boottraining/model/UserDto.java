package io.github.archmagefil.boottraining.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class UserDto {
    Long id;
    String name;
    String surname;
    Integer age;
    String email;
    List<RoleDto> roles;
}