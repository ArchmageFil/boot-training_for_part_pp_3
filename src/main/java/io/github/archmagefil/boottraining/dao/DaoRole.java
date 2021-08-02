package io.github.archmagefil.boottraining.dao;

import io.github.archmagefil.boottraining.model.Role;

import java.util.List;
import java.util.Optional;

public interface DaoRole {
    Optional<Role> findByName(String role);

    Optional<Role> findById(Long id);

    List<Role> getAll();
}