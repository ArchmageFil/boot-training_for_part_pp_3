package io.github.archmagefil.boottraining.service;

import io.github.archmagefil.boottraining.model.Role;
import io.github.archmagefil.boottraining.model.RoleDto;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<RoleDto> getAllRoles();

    Optional<Role> findById(Long id);

    List<Role> findListByIds(List<Role> ids);
}
