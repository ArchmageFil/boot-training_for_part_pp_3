package io.github.archmagefil.boottraining.service;

import io.github.archmagefil.boottraining.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<Role> getAllRoles();

    Optional<Role> findByName(String role);

    Optional<Role> findById(Long id);
}
